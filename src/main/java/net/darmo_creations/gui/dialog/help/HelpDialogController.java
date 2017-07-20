/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.gui.dialog.help;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JToggleButton;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.DefaultDialogController;

/**
 * This controller handles actions of the HelpDialog class.
 *
 * @author Damien Vergnet
 */
class HelpDialogController extends DefaultDialogController<HelpDialog> implements TreeSelectionListener, HyperlinkListener {
  private static final String ERROR_LINE = "<html><body><h1>Error: this page is not available!</h1></body></html>";

  private GlobalConfig config;
  private boolean syncTree;
  private boolean collapsingTree;
  private String currentPage;

  /**
   * Creates a controller.
   * 
   * @param dialog the dialog
   * @param config the global config
   */
  HelpDialogController(HelpDialog dialog, GlobalConfig config) {
    super(dialog);
    this.config = config;
    this.collapsingTree = false;
    this.currentPage = null;
  }

  /**
   * Initializes the dialog.
   * 
   * @param syncTree tells if the tree should be synced to the current page
   */
  void init(boolean syncTree) {
    this.syncTree = syncTree;
    loadPage(getUrl("index"));
  }

  private static final Pattern PAGE_NAME_PATTERN = Pattern.compile("/(\\w+(?:\\.\\w+)*).html");

  /**
   * Loads the page at the given URL.
   * 
   * @param url the url
   */
  private void loadPage(String url) {
    Matcher m = PAGE_NAME_PATTERN.matcher(url);

    if (m.find())
      this.currentPage = m.group(1);

    this.dialog.setLoadingIconVisible(true);

    Runnable run = () -> {
      try {
        this.dialog.displayHtmlDocument(getPage(new URL(url)));
      }
      catch (IOException __) {
        // Avoid infinite loop
        if ("error".equals(url)) {
          this.dialog.displayHtmlDocument(ERROR_LINE);
          this.dialog.setLoadingIconVisible(false);
        }
        else {
          loadPage("error");
        }
      }
    };

    new Thread(run).start();
  }

  private static final Pattern LINK_PATTERN = Pattern.compile("(src|href)=([\"'])((?!https?://).+?)\\2");

  /**
   * Returns the page's content for the given URL.
   * 
   * @param url the url
   * @return the page's content
   * @throws IOException
   */
  private String getPage(URL url) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
      StringJoiner joiner = new StringJoiner("\n");
      String inputLine;

      while ((inputLine = in.readLine()) != null) {
        boolean match = false;

        // Replace all relative links with absolute ones.
        do {
          match = false;
          Matcher m = LINK_PATTERN.matcher(inputLine);

          if (m.find()) {
            String absoluteUrl = new URL(url, m.group(3)).toString();
            absoluteUrl = String.format("%1$s=%2$s%3$s%2$s", m.group(1), m.group(2), absoluteUrl);
            // Every special regex character between \Q and \E is escaped.
            inputLine = inputLine.replaceAll("\\Q" + m.group(0) + "\\E", absoluteUrl);
            match = true;
          }
        } while (match);

        joiner.add(inputLine);
      }

      return joiner.toString();
    }
  }

  /**
   * Returns the URL corresponding to the given help file name.
   * 
   * @param name the name (without '.html')
   * @return the URL
   */
  private String getUrl(String name) {
    String lang = this.config.getLanguage().getCode();
    return String.format("http://darmo-creations.net/jenealogio/help-doc/%s/%s.html", lang, name);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    switch (e.getActionCommand()) {
      case "sync":
        JToggleButton syncBtn = (JToggleButton) e.getSource();
        this.syncTree = syncBtn.isSelected();
        if (this.syncTree)
          this.dialog.selectNode(this.currentPage);
        break;
      case "collapse":
        this.collapsingTree = true;
        this.dialog.collapseAllNodes();
        this.collapsingTree = false;
        break;
      case "home":
        this.dialog.clearSelection();
        loadPage(getUrl("index"));
        break;
    }
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    if (!this.collapsingTree) {
      NamedTreeNode node = (NamedTreeNode) e.getPath().getLastPathComponent();
      loadPage(getUrl(node.getName()));
    }
  }

  @Override
  public void hyperlinkUpdate(HyperlinkEvent e) {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      String url = e.getDescription();
      loadPage(url);
      if (this.syncTree)
        this.dialog.selectNode(this.currentPage);
    }
  }
}

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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JToggleButton;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.darmo_creations.Start;
import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.DefaultDialogController;
import net.darmo_creations.util.JarUtil;

public class HelpDialogController extends DefaultDialogController<HelpDialog> implements TreeSelectionListener, HyperlinkListener {
  private static final String ERROR_LINE = "<html><body><h1>Error: this page is not available!</h1></body></html>";

  private GlobalConfig config;
  private boolean syncTree;
  private boolean collapsingTree;
  private String currentPage;

  public HelpDialogController(HelpDialog dialog, GlobalConfig config) {
    super(dialog);
    this.config = config;
    this.collapsingTree = false;
    this.currentPage = null;
  }

  public void init(boolean syncTree) {
    this.syncTree = syncTree;
    loadPage("index");
  }

  private void loadPage(String name) {
    this.currentPage = name;
    String lang = this.config.getLanguage().getCode();
    String path = JarUtil.getJarDir('/') + "help-doc/" + lang + "/" + name + ".html";

    if (Start.DEBUG)
      path = path.replace("bin/", "");

    try {
      List<String> lines = Files.readAllLines(Paths.get(path));
      for (int i = 0; i < lines.size(); i++) {
        String result = String.format("src=\"file:/%shelp-doc/%s/$1\"", JarUtil.getJarDir('/'), lang);
        String absolutePath = lines.get(i).replaceAll("src=[\"'](.*)[\"']", result);

        if (Start.DEBUG)
          absolutePath = absolutePath.replace("bin/", "");
        lines.set(i, absolutePath);
      }
      this.dialog.loadHtmlPage(String.join("\n", lines));
    }
    catch (IOException __) {
      // Avoid infinite loop
      if ("error".equals(name)) {
        this.dialog.loadHtmlPage(ERROR_LINE);
      }
      else {
        loadPage("error");
      }
    }
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
        loadPage("index");
        break;
    }
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    if (!this.collapsingTree) {
      NamedTreeNode node = (NamedTreeNode) e.getPath().getLastPathComponent();
      loadPage(node.getName());
    }
  }

  @Override
  public void hyperlinkUpdate(HyperlinkEvent e) {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      String url = e.getDescription();
      url = url.substring(0, url.lastIndexOf('.'));
      loadPage(url);
      if (this.syncTree)
        this.dialog.selectNode(url);
    }
  }
}

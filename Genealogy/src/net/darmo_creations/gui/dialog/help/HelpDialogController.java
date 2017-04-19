package net.darmo_creations.gui.dialog.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.DefaultDialogController;

public class HelpDialogController extends DefaultDialogController<HelpDialog> implements TreeSelectionListener {
  private static final String ERROR_LINE = "<html><body><h1>Error: this page is not available!</h1></body></html>";

  private GlobalConfig config;

  public HelpDialogController(HelpDialog dialog, GlobalConfig config) {
    super(dialog);
    this.config = config;
  }

  // TEMP
  public void init() {
    loadPage("index");
  }

  private void loadPage(String name) {
    String path = "/doc/" + this.config.getLanguage().getCode() + "/" + name + ".html";

    try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)))) {
      String page = "", line;

      while ((line = br.readLine()) != null) {
        page += line + "\n";
      }

      this.dialog.loadHtmlPage(page);
    }
    catch (NullPointerException | IOException __) {
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
  public void valueChanged(TreeSelectionEvent e) {
    NamedTreeNode node = (NamedTreeNode) e.getPath().getLastPathComponent();

    loadPage(node.getName());
  }
}

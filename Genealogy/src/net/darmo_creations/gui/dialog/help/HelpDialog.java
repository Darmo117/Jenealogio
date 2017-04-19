package net.darmo_creations.gui.dialog.help;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.util.I18n;

public class HelpDialog extends AbstractDialog {
  private static final long serialVersionUID = -4647705001322495655L;

  private HelpDialogController controller;
  private JTextPane textPane;

  public HelpDialog(JFrame owner, GlobalConfig config) {
    super(owner, Mode.CLOSE_OPTION, false);

    setTitle(I18n.getLocalizedString("dialog.help.title"));

    this.controller = new HelpDialogController(this, config);

    JSplitPane p = new JSplitPane();

    JTree tree = new JTree(new DefaultTreeModel(getTree()));
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.addTreeSelectionListener(this.controller);
    JScrollPane scrollPane = new JScrollPane(tree);
    p.add(scrollPane, JSplitPane.LEFT);

    this.textPane = new JTextPane();
    this.textPane.setContentType("text/html");
    this.textPane.setEditable(false);
    this.textPane.setPreferredSize(new Dimension(600, 400));
    this.textPane.setDocument(getDocument(this.textPane));
    p.add(this.textPane, JSplitPane.RIGHT);

    add(p);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);
    // TEMP
    this.controller.init();
  }

  private Document getDocument(JTextPane textPane) {
    HTMLEditorKit kit = new HTMLEditorKit();
    textPane.setEditorKit(kit);

    StyleSheet styleSheet = kit.getStyleSheet();
    styleSheet.addRule("body {font-family: sans serif; margin: 4px;}");

    return kit.createDefaultDocument();
  }

  private TreeNode getTree() {
    NamedTreeNode root = new NamedTreeNode("root", "");

    root.add(new NamedTreeNode("overview", I18n.getLocalizedString("node.help.overview")));
    NamedTreeNode gettingStarted = new NamedTreeNode("getting_started", I18n.getLocalizedString("node.help.getting_started"));
    root.add(gettingStarted);
    gettingStarted.add(new NamedTreeNode("getting_started.workbench", I18n.getLocalizedString("node.help.getting_started.workbench")));
    NamedTreeNode cards = new NamedTreeNode("getting_started.cards", I18n.getLocalizedString("node.help.getting_started.cards"));
    gettingStarted.add(cards);
    cards.add(new NamedTreeNode("getting_started.cards.add", I18n.getLocalizedString("node.help.getting_started.cards_add")));
    cards.add(new NamedTreeNode("getting_started.cards.update", I18n.getLocalizedString("node.help.getting_started.cards_update")));
    cards.add(new NamedTreeNode("getting_started.cards.delete", I18n.getLocalizedString("node.help.getting_started.cards_delete")));
    NamedTreeNode links = new NamedTreeNode("getting_started.links", I18n.getLocalizedString("node.help.getting_started.links"));
    gettingStarted.add(links);
    links.add(new NamedTreeNode("getting_started.links.add", I18n.getLocalizedString("node.help.getting_started.links_add")));
    links.add(new NamedTreeNode("getting_started.links.update", I18n.getLocalizedString("node.help.getting_started.links_update")));
    links.add(new NamedTreeNode("getting_started.links.delete", I18n.getLocalizedString("node.help.getting_started.links_delete")));
    NamedTreeNode options = new NamedTreeNode("options", I18n.getLocalizedString("node.help.options"));
    root.add(options);
    options.add(new NamedTreeNode("options.languages", I18n.getLocalizedString("node.help.options.languages")));
    options.add(new NamedTreeNode("options.colors", I18n.getLocalizedString("node.help.options.colors")));
    root.add(new NamedTreeNode("legal", I18n.getLocalizedString("node.help.legal")));

    return root;
  }

  void loadHtmlPage(String page) {
    this.textPane.setText(page);
  }
}

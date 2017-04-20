package net.darmo_creations.gui.dialog.help;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.StringJoiner;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

public class HelpDialog extends AbstractDialog {
  private static final long serialVersionUID = -4647705001322495655L;

  private HelpDialogController controller;
  private JEditorPane editorPane;
  private JTree tree;
  private JToggleButton syncBtn;

  public HelpDialog(JFrame owner, GlobalConfig config) {
    super(owner, Mode.CLOSE_OPTION, false);

    setTitle(I18n.getLocalizedString("dialog.help.title"));

    this.controller = new HelpDialogController(this, config);

    final Dimension size = new Dimension(24, 24);

    JSplitPane splitPnl = new JSplitPane();

    JPanel leftPnl = new JPanel(new BorderLayout());
    JPanel leftButtonsPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    JButton collapseBtn = new JButton(Images.COLLAPSE);
    collapseBtn.setActionCommand("collapse");
    collapseBtn.addActionListener(this.controller);
    collapseBtn.setToolTipText(I18n.getLocalizedString("button.collapse_all.tooltip"));
    collapseBtn.setFocusable(false);
    collapseBtn.setPreferredSize(size);
    leftButtonsPnl.add(collapseBtn);
    this.syncBtn = new JToggleButton(Images.SYNC);
    this.syncBtn.setSelected(true);
    this.syncBtn.setActionCommand("sync");
    this.syncBtn.addActionListener(this.controller);
    this.syncBtn.setToolTipText(I18n.getLocalizedString("button.sync.tooltip"));
    this.syncBtn.setFocusable(false);
    this.syncBtn.setPreferredSize(size);
    leftButtonsPnl.add(this.syncBtn);
    leftPnl.add(leftButtonsPnl, BorderLayout.NORTH);

    this.tree = new JTree(new DefaultTreeModel(getTree()));
    this.tree.setRootVisible(false);
    this.tree.setShowsRootHandles(true);
    this.tree.addTreeSelectionListener(this.controller);
    this.tree.setCellRenderer(new DefaultTreeCellRenderer() {
      private static final long serialVersionUID = -489015910861060922L;

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
          boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (leaf) {
          setIcon(Images.PAGE);
        }
        else {
          setOpenIcon(Images.BOOK_OPEN);
          setClosedIcon(Images.BOOK);
        }

        return c;
      }
    });
    JScrollPane scrollPane = new JScrollPane(this.tree);
    leftPnl.add(scrollPane, BorderLayout.CENTER);
    splitPnl.add(leftPnl, JSplitPane.LEFT);

    JPanel rightPnl = new JPanel(new BorderLayout());
    JPanel rightButtonsPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    JButton homeBtn = new JButton(Images.HOME);
    homeBtn.setActionCommand("home");
    homeBtn.addActionListener(this.controller);
    homeBtn.setToolTipText(I18n.getLocalizedString("button.home.tooltip"));
    homeBtn.setFocusable(false);
    homeBtn.setPreferredSize(size);
    rightButtonsPnl.add(homeBtn);
    rightPnl.add(rightButtonsPnl, BorderLayout.NORTH);

    this.editorPane = new JEditorPane();
    this.editorPane.setContentType("text/html");
    this.editorPane.setEditable(false);
    this.editorPane.setPreferredSize(new Dimension(600, 400));
    this.editorPane.setDocument(getDocument(this.editorPane));
    this.editorPane.addHyperlinkListener(this.controller);
    rightPnl.add(this.editorPane, BorderLayout.CENTER);
    splitPnl.add(rightPnl, JSplitPane.RIGHT);

    add(splitPnl);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);

    this.controller.init(this.syncBtn.isSelected());
  }

  private Document getDocument(JEditorPane textPane) {
    HTMLEditorKit kit = new HTMLEditorKit();
    textPane.setEditorKit(kit);

    StyleSheet styleSheet = kit.getStyleSheet();
    styleSheet.addRule("body {font-family: sans serif; margin: 4px;}");
    styleSheet.addRule(".button, .menu {font-weight: bold;}");
    styleSheet.addRule(".menu {font-style: italic;}");

    return kit.createDefaultDocument();
  }

  private TreeNode getTree() {
    NamedTreeNode root = new NamedTreeNode("root", "");

    root.add(new NamedTreeNode("overview", I18n.getLocalizedString("node.help.overview.text")));
    NamedTreeNode gettingStarted = new NamedTreeNode("getting_started", I18n.getLocalizedString("node.help.getting_started.text"));
    root.add(gettingStarted);
    gettingStarted.add(new NamedTreeNode("getting_started.workbench", I18n.getLocalizedString("node.help.getting_started.workbench.text")));
    NamedTreeNode cards = new NamedTreeNode("getting_started.cards", I18n.getLocalizedString("node.help.getting_started.cards.text"));
    gettingStarted.add(cards);
    cards.add(new NamedTreeNode("getting_started.cards.add", I18n.getLocalizedString("node.help.getting_started.cards.add.text")));
    cards.add(new NamedTreeNode("getting_started.cards.update", I18n.getLocalizedString("node.help.getting_started.cards.update.text")));
    cards.add(new NamedTreeNode("getting_started.cards.delete", I18n.getLocalizedString("node.help.getting_started.cards.delete.text")));
    NamedTreeNode links = new NamedTreeNode("getting_started.links", I18n.getLocalizedString("node.help.getting_started.links.text"));
    gettingStarted.add(links);
    links.add(new NamedTreeNode("getting_started.links.add", I18n.getLocalizedString("node.help.getting_started.links.add.text")));
    links.add(new NamedTreeNode("getting_started.links.update", I18n.getLocalizedString("node.help.getting_started.links.update.text")));
    links.add(new NamedTreeNode("getting_started.links.delete", I18n.getLocalizedString("node.help.getting_started.links.delete.text")));
    NamedTreeNode options = new NamedTreeNode("options", I18n.getLocalizedString("node.help.options.text"));
    root.add(options);
    options.add(new NamedTreeNode("options.languages", I18n.getLocalizedString("node.help.options.languages.text")));
    options.add(new NamedTreeNode("options.colors", I18n.getLocalizedString("node.help.options.colors.text")));
    root.add(new NamedTreeNode("legal", I18n.getLocalizedString("node.help.legal.text")));

    return root;
  }

  void selectNode(String name) {
    NamedTreeNode rootNode = (NamedTreeNode) this.tree.getModel().getRoot();
    NamedTreeNode currentNode = rootNode;
    TreePath path = new TreePath(rootNode);
    String[] array = name.split("\\.");

    for (int i = 0; i < array.length; i++) {
      StringJoiner s = new StringJoiner(".");

      for (int j = 0; j <= i; j++) {
        s.add(array[j]);
      }

      for (int k = 0; k < currentNode.getChildCount(); k++) {
        NamedTreeNode child = (NamedTreeNode) currentNode.getChildAt(k);

        if (child.getName().equals(s.toString())) {
          path = path.pathByAddingChild(child);
          currentNode = child;
          break;
        }
      }
    }

    this.tree.setSelectionPath(path);
  }

  void collapseAllNodes() {
    TreeNode root = (TreeNode) this.tree.getModel().getRoot();
    collapseAllNodes(new TreePath(root));
    this.tree.expandPath(new TreePath(root));
  }

  private void collapseAllNodes(TreePath parent) {
    TreeNode node = (TreeNode) parent.getLastPathComponent();

    if (node.getChildCount() >= 0) {
      for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        collapseAllNodes(path);
      }
    }
    this.tree.collapsePath(parent);
  }

  void clearSelection() {
    this.tree.clearSelection();
  }

  void loadHtmlPage(String page) {
    this.editorPane.setText(page);
  }
}

package net.darmo_creations.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringJoiner;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

public class AboutDialog extends AbstractDialog {
  private static final long serialVersionUID = -1919314429757828369L;

  public AboutDialog(JFrame owner) {
    super(owner, Mode.CLOSE_OPTION, false);

    setTitle(I18n.getLocalizedString("dialog.about.title"));

    JPanel leftPnl = new JPanel();
    leftPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    leftPnl.setLayout(new BoxLayout(leftPnl, BoxLayout.Y_AXIS));
    leftPnl.add(new JLabel(Images.JENEALOGIO));
    leftPnl.add(new JLabel(Images.GNU_GPL));
    add(leftPnl, BorderLayout.WEST);

    JEditorPane textPnl = new JEditorPane();
    textPnl.setContentType("text/html");
    textPnl.setEditable(false);
    textPnl.setPreferredSize(new Dimension(600, 200));
    textPnl.setDocument(getDocument(textPnl));
    textPnl.addHyperlinkListener(e -> {
      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        try {
          Desktop.getDesktop().browse(new URI(e.getDescription()));
        }
        catch (IOException | URISyntaxException e1) {
          e1.printStackTrace();
        }
      }
    });
    textPnl.setText(getHtml());
    add(new JScrollPane(textPnl), BorderLayout.CENTER);

    setActionListener(new DefaultDialogController<>(this));

    pack();
    setLocationRelativeTo(owner);
  }

  private String getHtml() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/about.html")))) {
      String line;
      StringJoiner sj = new StringJoiner("\n");

      while ((line = br.readLine()) != null) {
        sj.add(line);
      }

      return sj.toString();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Document getDocument(JEditorPane textPane) {
    HTMLEditorKit kit = new HTMLEditorKit();
    textPane.setEditorKit(kit);

    StyleSheet styleSheet = kit.getStyleSheet();
    styleSheet.addRule("body {font-family: sans serif; margin: 4px; font-size: 10px}");
    styleSheet.addRule("h2 {margin-top: 0;}");

    return kit.createDefaultDocument();
  }
}

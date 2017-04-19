package net.darmo_creations.dao;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.darmo_creations.config.ColorConfigKey;
import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.config.Language;

/**
 * This class handles I/O operations for the {@code GlobalConfig} class.
 *
 * @author Damien Vergnet
 */
public class ConfigDao {
  private static ConfigDao instance;

  public static ConfigDao getInstance() {
    if (instance == null)
      instance = new ConfigDao();
    return instance;
  }

  /**
   * Loads the config stored in the "config.xml" file in the same directory as the jar.
   * 
   * @return the config or null if a fatal error occured
   */
  public GlobalConfig load() {
    try {
      GlobalConfig config = new GlobalConfig();

      File fXmlFile = new File(getJarDir() + "config.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);

      doc.getDocumentElement().normalize();

      Element root = (Element) doc.getElementsByTagName("Config").item(0);

      Element locale = (Element) root.getElementsByTagName("Locale").item(0);
      Language language = Language.fromCode(locale.getTextContent());
      if (language != null)
        config.setLanguage(language);

      Element colors = (Element) root.getElementsByTagName("Colors").item(0);
      NodeList list = colors.getElementsByTagName("Color");
      for (int i = 0; i < list.getLength(); i++) {
        Element color = (Element) list.item(i);
        Color c = new Color(Integer.parseInt(color.getTextContent()));
        config.setValue(ColorConfigKey.fromName(color.getAttribute("name")), c);
      }

      return config;
    }
    catch (NullPointerException | ClassCastException | ParserConfigurationException | SAXException | IOException __) {
      return new GlobalConfig();
    }
  }

  /**
   * Saves the given config to the "config.xml" file in the same directory as the jar.
   * 
   * @param config the config
   */
  public void save(GlobalConfig config) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      Element root = doc.createElement("Config");

      Element locale = doc.createElement("Locale");
      locale.appendChild(doc.createTextNode(config.getLanguage().getCode()));
      root.appendChild(locale);

      Element colors = doc.createElement("Colors");
      for (ColorConfigKey key : ColorConfigKey.values()) {
        Element color = doc.createElement("Color");
        color.setAttribute("name", key.getName());
        color.appendChild(doc.createTextNode("" + config.getValue(key).getRGB()));
        colors.appendChild(color);
      }
      root.appendChild(colors);

      doc.appendChild(root);

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new File(getJarDir() + "config.xml"));

      transformer.transform(new DOMSource(doc), result);
    }
    catch (ParserConfigurationException | TransformerException __) {}
  }

  /**
   * @return the path to the jar
   */
  private static String getJarDir() {
    try {
      String path = ConfigDao.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

      if (File.separatorChar == '\\') {
        path = path.replace('/', '\\');
        path = path.substring(1); // Removes the first /.
      }

      return path;
    }
    catch (URISyntaxException e) {
      return null;
    }
  }

  private ConfigDao() {}
}

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
package net.darmo_creations.dao;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

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

import net.darmo_creations.config.BooleanConfigKey;
import net.darmo_creations.config.ColorConfigKey;
import net.darmo_creations.config.ConfigKey;
import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.config.Language;
import net.darmo_creations.util.JarUtil;

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
    GlobalConfig config = new GlobalConfig();

    try {
      File fXmlFile = new File(JarUtil.getJarDir() + "config.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);

      doc.getDocumentElement().normalize();

      Element root = (Element) doc.getElementsByTagName("Config").item(0);
      if (root != null) {
        Element locale = (Element) root.getElementsByTagName("Locale").item(0);
        if (locale != null) {
          Language language = Language.fromCode(locale.getTextContent());
          if (language != null)
            config.setLanguage(language);
        }

        Element colors = (Element) root.getElementsByTagName("Colors").item(0);
        if (colors != null) {
          NodeList colorNodes = colors.getElementsByTagName("Color");
          for (int i = 0; i < colorNodes.getLength(); i++) {
            Element color = (Element) colorNodes.item(i);
            Color c = new Color(Integer.parseInt(color.getTextContent()));
            config.setValue(ConfigKey.fromName(color.getAttribute("name"), ColorConfigKey.class), c);
          }
        }

        Element booleans = (Element) root.getElementsByTagName("Booleans").item(0);
        if (booleans != null) {
          NodeList booleanNodes = booleans.getElementsByTagName("Boolean");
          for (int i = 0; i < booleanNodes.getLength(); i++) {
            Element bool = (Element) booleanNodes.item(i);
            boolean b = "true".equalsIgnoreCase(bool.getTextContent());
            config.setValue(ConfigKey.fromName(bool.getAttribute("name"), BooleanConfigKey.class), b);
          }
        }
      }
    }
    catch (NullPointerException | ClassCastException | ParserConfigurationException | SAXException | IOException __) {}

    return config;
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

      Element booleans = doc.createElement("Booleans");
      for (BooleanConfigKey key : BooleanConfigKey.values()) {
        Element bool = doc.createElement("Boolean");
        bool.setAttribute("name", key.getName());
        bool.appendChild(doc.createTextNode(config.getValue(key) ? "true" : "false"));
        booleans.appendChild(bool);
      }
      root.appendChild(booleans);

      doc.appendChild(root);

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new File(JarUtil.getJarDir() + "config.xml"));

      transformer.transform(new DOMSource(doc), result);
    }
    catch (ParserConfigurationException | TransformerException __) {}
  }

  private ConfigDao() {}
}

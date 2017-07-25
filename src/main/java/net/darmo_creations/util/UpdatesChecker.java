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
package net.darmo_creations.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.darmo_creations.events.EventsDispatcher;
import net.darmo_creations.events.UpdateEvent;

/**
 * This class checks if a newer version is available online.
 *
 * @author Damien Vergnet
 */
public final class UpdatesChecker {
  private static final Pattern TITLE_PATTERN = Pattern.compile("Jenealogio (\\d+\\.\\d+(?:\\.\\d+)?d?)");

  private Version version;
  private String link, changelog;
  private boolean updateAvailable;

  public UpdatesChecker() {
    reset();
  }

  /**
   * @return the version
   */
  public Version getVersion() {
    return this.version;
  }

  /**
   * @return the link to the update
   */
  public String getLink() {
    return this.link;
  }

  /**
   * @return the changelog
   */
  public String getChangelog() {
    return this.changelog;
  }

  /**
   * @return true if an update is available
   */
  public boolean isUpdateAvailable() {
    return this.updateAvailable;
  }

  /**
   * Checks if an update is available.
   */
  public void checkUpdate() {
    UpdateEvent.Checking event = new UpdateEvent.Checking();
    EventsDispatcher.EVENT_BUS.dispatchEvent(event);

    if (event.isCanceled()) {
      return;
    }

    Runnable r = () -> {
      boolean error = false;
      String errorMsg = null;

      try {
        URL oracle = new URL("https://github.com/Darmo117/Jenealogio/releases.atom");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()))) {
          StringJoiner joiner = new StringJoiner("\n");
          String inputLine;

          while ((inputLine = in.readLine()) != null)
            joiner.add(inputLine);

          Optional<Document> opt = loadXMLFromString(joiner.toString());
          if (opt.isPresent()) {
            try {
              Document doc = opt.get();
              XPathExpression expr = XPathFactory.newInstance().newXPath().compile("/feed/entry");
              NodeList entries = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
              SortedMap<Version, String[]> versions = new TreeMap<>();

              for (int i = 0; i < entries.getLength(); i++) {
                Element entry = (Element) entries.item(i);
                Node title = entry.getElementsByTagName("title").item(0);
                Matcher m = TITLE_PATTERN.matcher(title.getTextContent());

                if (m.matches()) {
                  try {
                    Node link = entry.getElementsByTagName("link").item(0);
                    Node content = entry.getElementsByTagName("content").item(0);
                    String[] data = new String[]{"http://github.com" + link.getAttributes().getNamedItem("href").getNodeValue(),
                      content.getTextContent()};
                    versions.put(Version.fromString(m.group(1)), data);
                  }
                  catch (ParseException __) {}
                }
              }

              if (!versions.isEmpty()) {
                Version lastVersion = versions.lastKey();

                if (lastVersion.after(Version.CURRENT_VERSION)) {
                  String[] data = versions.get(lastVersion);

                  this.version = lastVersion;
                  this.link = data[0];
                  this.changelog = data[1];
                  this.updateAvailable = true;
                  EventsDispatcher.EVENT_BUS.dispatchEvent(new UpdateEvent.NewUpdate(lastVersion, data[0], data[1]));
                }
              }
              else {
                reset();
              }
            }
            catch (XPathExpressionException ex) {
              error = true;
              errorMsg = ex.getLocalizedMessage();
            }
          }
        }
      }
      catch (IOException ex) {
        error = true;
        errorMsg = ex.getLocalizedMessage();
      }

      if (error) {
        reset();
        EventsDispatcher.EVENT_BUS.dispatchEvent(new UpdateEvent.CheckFailed(errorMsg));
      }
    };
    new Thread(r).start();
  }

  /**
   * Resets the checker.
   */
  private void reset() {
    this.version = null;
    this.link = null;
    this.changelog = null;
    this.updateAvailable = false;
  }

  /**
   * Parses an XML string.
   * 
   * @param xml the string
   * @return the XML document
   */
  private Optional<Document> loadXMLFromString(String xml) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(xml));

      return Optional.of(builder.parse(is));
    }
    catch (SAXException | IOException | ParserConfigurationException __) {
      return Optional.empty();
    }
  }
}

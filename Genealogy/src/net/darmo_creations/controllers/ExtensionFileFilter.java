package net.darmo_creations.controllers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * This file filter is used by JFileChooser objects to filter one file type only.
 * 
 * @author Damien Vergnet
 */
public class ExtensionFileFilter extends FileFilter {
  private List<String> extensions;
  private String description;

  /**
   * Creates a file filter.
   * 
   * @param fileType the description of the file type.
   * @param extensions the extensions without the '.'
   */
  public ExtensionFileFilter(String fileType, String... extensions) {
    this.extensions = Arrays.asList(extensions);
    String[] exts = this.extensions.stream().map(ext -> "*." + ext).toArray(String[]::new);
    this.description = fileType + " (" + String.join(", ", exts) + ")";
  }

  @Override
  public boolean accept(File f) {
    return f.isDirectory() || hasValidExtension(f);
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * Checks if the given file has a valid extension.
   * 
   * @param f the file
   * @return true if and only if the file has a valid extension
   */
  private boolean hasValidExtension(File f) {
    return this.extensions.stream().filter(ext -> f.getName().toLowerCase().endsWith("." + ext)).findAny().isPresent();
  }
}

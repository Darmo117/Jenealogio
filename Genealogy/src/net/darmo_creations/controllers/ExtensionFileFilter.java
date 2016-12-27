package net.darmo_creations.controllers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {
  private List<String> extensions;
  private String description;

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

  private boolean hasValidExtension(File f) {
    return this.extensions.stream().filter(ext -> f.getName().toLowerCase().endsWith("." + ext)).findAny().isPresent();
  }
}

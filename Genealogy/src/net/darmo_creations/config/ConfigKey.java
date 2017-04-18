package net.darmo_creations.config;

public interface ConfigKey<T> {
  String getName();

  Class<T> getValueClass();
}

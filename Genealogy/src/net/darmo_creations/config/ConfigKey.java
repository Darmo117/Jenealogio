package net.darmo_creations.config;

/**
 * A {@code ConfigKey} is used in the {@code GlobalConfig} object as options keys.
 * 
 * @author Damien Vergnet
 *
 * @param <T> the type of the associated values
 */
public interface ConfigKey<T> {
  /**
   * @return this key's name
   */
  String getName();
}

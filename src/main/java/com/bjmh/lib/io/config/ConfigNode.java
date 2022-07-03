package com.bjmh.lib.io.config;

/**
 * The {@code ConfigNode} is the basis of the {@code Configuration} data
 * structure.
 * All other type extends this interface.
 * 
 * @see ConfigOption
 * @see ConfigSection
 * @see Configuration
 */
public interface ConfigNode {
  /**
   * This method allows for navigating the {@code Condiguration} data structure
   * unidirectionally.
   * 
   * @return this {@code ConfigNodes's} parent
   * 
   * @see Configuration
   */
  public ConfigSection getParent();

  /**
   * This method allows for navigating the {@code Condiguration} data structure
   * unidirectionally.
   * 
   * @param parent this {@code ConfigNode's} parent
   * 
   * @see Configuration
   */
  public void setParent(ConfigSection parent);

  /**
   * this method is used to locate this {@code CondigNode} in the
   * {@code Condiguration} data structure.
   * 
   * @return this {@code ConfigNode's} name
   * 
   * @see Configuration
   */
  public String getName();

  /**
   * this method is used to locate this {@code CondigNode} in the
   * {@code Condiguration} data structure.
   * 
   * @param name this {@code ConfigNode's} name
   * 
   * @see Configuration
   */
  public void setName(String name);
}

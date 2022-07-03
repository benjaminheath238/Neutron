package com.bjmh.lib.io.config;

/**
 * The {@code ConfigOption} is an extension of the {@code ConfigNode} it allows
 * {@code ConfigSection} to have key-value pairs.
 * 
 * @see ConfigNode
 * @see ConfigSection
 * @see Configuration
 */
public interface ConfigOption extends ConfigNode {
  /**
   * This method allows for the use of simple key-value pairs in
   * {@code ConfigSection}s
   * 
   * @return this {@code ConfigOption's} value
   * 
   * @see ConfigSection
   */
  public String getValue();

  /**
   * This method allows for the use of simple key-value pairs in
   * {@code ConfigSections}
   * 
   * @param value this {@code ConfigOption's} value
   * 
   * @see ConfigSection
   */
  public void setValue(String value);
}

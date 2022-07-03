package com.bjmh.lib.io.config;

/**
 * The {@code ConfigPath} does not extend the {@code ConfigNode} interface
 * and instead it's used to navigate the {@code Configuration} data structre.
 * 
 * @see ConfigNode
 * @see Configuration
 */
public interface ConfigPath {
  /**
   * 
   * @return the next part in the path
   */
  public String next();

  /**
   * 
   * @return is there anymore parts to this path
   */
  public boolean hasNext();
}

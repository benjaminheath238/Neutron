package com.bjmh.lib.io.config;

import java.util.Collection;

/**
 * The {@code ConfigSection} is an extension of the {@code ConfigNode} it allows
 * {@code ConfigSections} and {@code Configurations} to have sections and subsections 
 * defined with headers.
 * 
 * @see ConfigNode
 * @see ConfigOption
 * @see Configuration
 */
public interface ConfigSection extends ConfigNode {
  /**
   * 
   * @param name the name of the child {@code ConfigNode}
   * @return the child {@code ConfigNode}
   */
  public ConfigNode getChild(String name);

  public void addChild(ConfigNode child);

  public Collection<ConfigNode> getChildren();
}

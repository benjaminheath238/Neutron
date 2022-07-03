package com.bjmh.lib.io.config;

import java.util.Collection;

public interface ConfigSection extends ConfigNode {
  public ConfigNode getChild(String name);
  
  public void addChild(ConfigNode child);

  public Collection<ConfigNode> getChildren();
}

package com.bjmh.lib.io.config;

import java.util.Collection;

public abstract class ConfigSection extends ConfigNode {
  public abstract ConfigNode getChild(String name);

  public abstract void addChild(ConfigNode child);

  public abstract Collection<ConfigNode> getChildren();

  public abstract void foreach(ConfigConsumer consumer);

  @Override
  public String toString() {
    return "{name="
        + this.getName()
        + ", type="
        + this.getType()
        + ", parent="
        + this.getParent().getName()
        + ", children="
        + this.getChildren()
        + "}";
  }
}

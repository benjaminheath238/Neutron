package com.bjmh.lib.io.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigSection extends ConfigNode {
  protected Map<String, ConfigNode> children = new HashMap<>();

  public ConfigSection() {}

  public ConfigSection(ConfigSection parent, String name, Type type) {
    this.type = type;
    this.parent = parent;
    this.name = name;
  }

  public ConfigNode getChild(String name) {
    return this.children.get(name);
  }

  public Collection<ConfigNode> getChildren() {
    return this.children.values();
  }


  public void addChild(ConfigNode child) {
    child.setParent(this);
    this.children.put(child.getName(), child);
  }

  public void foreach(ConfigConsumer consumer) {
    for (ConfigNode node : this.children.values()) {
      consumer.accept(node);
      if (node instanceof ConfigSection)
        ((ConfigSection) node).foreach(consumer);
    }
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(this.getName(), this.getType());
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ConfigSection && this.hashCode() == o.hashCode();
  }
  
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

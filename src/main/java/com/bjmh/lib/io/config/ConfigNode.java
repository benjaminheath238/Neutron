package com.bjmh.lib.io.config;

public class ConfigNode implements Comparable<ConfigNode> {
  protected ConfigSection parent;
  protected String name;
  protected Type type;

  public ConfigNode() {
  }

  public ConfigNode(ConfigSection parent, String name, Type type) {
    this.type = type;
    this.parent = parent;
    this.name = name;
  }

  public ConfigSection getParent() {
    return this.parent;
  }

  public void setParent(ConfigSection parent) {
    this.parent = parent;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(this.getName(), this.getType());
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ConfigNode && o.hashCode() == this.hashCode();
  }

  @Override
  public String toString() {
    return "{name=" + this.getName() + ", type=" + this.getType() + ", parent=" + this.getParent().getName() + "}";
  }

  @Override
  public int compareTo(ConfigNode node) {
    return this.getName().compareTo(node.getName());
  }

  public enum Type {
    SIMPLE_SECTION(true, true),
    ROOT_SECTION(true, true),
    SIMPLE_OPTION(false, false),
    COMPLEX_OPTION(true, false),
    MAP_OPTION(true, false),
    ARRAY_OPTION(true, false);

    private final boolean canIterate;
    private final boolean isSection;

    Type(boolean canIterate, boolean isSection) {
      this.canIterate = canIterate;
      this.isSection = isSection;
    }

    public boolean canIterate() {
      return canIterate;
    }

    public boolean isSection() {
      return isSection;
    }
  }
}

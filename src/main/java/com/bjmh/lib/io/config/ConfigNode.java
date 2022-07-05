package com.bjmh.lib.io.config;

public abstract class ConfigNode implements Comparable<ConfigNode> {
  public abstract ConfigSection getParent();

  public abstract void setParent(ConfigSection parent);

  public abstract String getName();

  public abstract void setName(String name);

  public abstract Type getType();

  public abstract void setType(Type type);

  @Override
  public int hashCode() {
    return java.util.Objects.hash(this.getName(), this.getType());
  }

  @Override
  public boolean equals(Object o) {
    return o != null && o instanceof ConfigNode && o.hashCode() == this.hashCode();
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
    SIMPLE_SECTION,
    ROOT_SECTION,
    SIMPLE_OPTION,
    COMPLEX_OPTION,
    MAP_OPTION,
    ARRAY_OPTION;
  }
}

package com.bjmh.lib.io.config;

public interface ConfigNode {
  public ConfigSection getParent();

  public void setParent(ConfigSection parent);

  public String getName();

  public void setName(String name);

  public Type getType();

  public void setType(Type type);

  public enum Type {
    SIMPLE_SECTION,
    ROOT_SECTION,
    SIMPLE_OPTION,
    COMPLEX_OPTION;
  }
}

package com.bjmh.lib.io.config;

public interface ConfigNode {
  public ConfigSection getParent();

  public void setParent(ConfigSection node);

  public String getName();

  public void setName(String name);
}

package com.bjmh.lib.io.config;

public interface ConfigOption extends ConfigNode {
  public String getValue();
  
  public void setValue(String value);
}

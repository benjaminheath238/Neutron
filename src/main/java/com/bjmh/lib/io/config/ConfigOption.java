package com.bjmh.lib.io.config;

public abstract class ConfigOption extends ConfigNode {
  public abstract String getValue();

  public abstract void setValue(String value);

  @Override
  public String toString() {
    return "{name="
        + this.getName()
        + ", type="
        + this.getType()
        + ", parent="
        + this.getParent().getName()
        + ", value="
        + this.getValue()
        + "}";
  }
}

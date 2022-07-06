package com.bjmh.lib.io.config;

public class ConfigOption extends ConfigNode {
  protected String value;

  public ConfigOption() {}

  public ConfigOption(ConfigSection parent, String name, Type type, String value) {
    this.type = type;
    this.parent = parent;
    this.name = name;
    this.value = value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(this.getName(), this.getType());
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ConfigOption && o.hashCode() == this.hashCode();
  }

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

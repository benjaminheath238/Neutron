package com.bjmh.lib.io.config;

public class ConfigPath {
  private String[] path = null;
  private int index = 0;

  public ConfigPath(String path) {
    this.path = path.split("\\.");
  }

  public String next() {
    return this.path[index++];
  }

  public boolean hasNext() {
    return this.index < this.path.length;
  }

  @Override
  public int hashCode() {
    return java.util.Arrays.hashCode(this.path);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Configuration && this.hashCode() == o.hashCode();
  }
}

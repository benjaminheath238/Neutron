package com.bjmh.lib.io.config;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.HashMap;

public class Configuration extends ConfigSection {
  public Configuration(String name) {
    this.parent = null;
    this.name = name;
    this.children = new HashMap<>();
    this.type = Type.ROOT_SECTION;
  }


  public ConfigNode getChild(ConfigPath path) {
    ConfigNode node = this.getChild(path.next());
    while (path.hasNext()) {
      if (node == null) {
        break;
      } else if (node instanceof ConfigSection) {
        node = ((ConfigSection) node).getChild(path.next());
      }
    }
    return node;
  }


  @Override
  public void setParent(ConfigSection parent) {
    // The Parent is always null
  }

  @Override
  public void setName(String name) {
    // The name is not changable
  }

  @Override
  public void setType(Type type) {
    // The type is always ROOT_SECTION
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(this.getName(), this.getType());
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Configuration && this.hashCode() == o.hashCode();
  }

  @Override
  public String toString() {
    return "{name=" + this.name + ", type=" + this.type + ", children=" + this.children + "}";
  }

  public void parse(String path, ParserMethod method) {
    File file = new File(path);
    try (FileReader reader = new FileReader(file); BufferedReader buffer = new BufferedReader(reader)) {

      while (buffer.ready()) {
        method.parse(buffer.readLine(), this);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

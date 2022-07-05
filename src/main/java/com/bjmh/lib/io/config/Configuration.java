package com.bjmh.lib.io.config;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class Configuration extends ConfigSection {
  private String name;
  private ConfigSection parent;
  private Map<String, ConfigNode> children;
  private Type type;

  public Configuration(String name) {
    this.parent = null;
    this.name = name;
    this.children = new HashMap<>();
    this.type = Type.ROOT_SECTION;
  }

  public ConfigSection getParent() {
    return this.parent;
  }

  public ConfigNode getChild(String name) {
    return this.children.get(name);
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

  public String getName() {
    return this.name;
  }

  public Collection<ConfigNode> getChildren() {
    return this.children.values();
  }

  public void setParent(ConfigSection parent) {
    // this.parent = parent;
  }

  public void addChild(ConfigNode child) {
    this.children.put(child.getName(), child);
  }

  public void setName(String name) {
    // this.name = name;
  }

  public void setType(Type type) {
    // this.type = type;
  }

  public Type getType() {
    return type;
  }

  public void foreach(ConfigConsumer consumer) {
    for (ConfigNode node : this.children.values()) {
      consumer.accept(node);
      if (node instanceof ConfigSection)
        ((ConfigSection) node).foreach(consumer);
    }
  }

  public String toString() {
    return "{name=" + this.name + ", type=" + this.type + ", children=" + this.children + "}";
  }

  public void parse(String path, ParserMethod method) throws IOException {
    File file = new File(path);
    FileReader reader = new FileReader(file);
    BufferedReader buffer = new BufferedReader(reader);

    while (buffer.ready()) {
      method.parse(buffer.readLine(), this);
    }

    reader.close();
    buffer.close();
  }

  public ConfigOption newConfigOption() {
    return new ConfigOption() {
      private ConfigSection parent;
      private String name;
      private String value;
      private Type type;

      public ConfigSection getParent() {
        return this.parent;
      }

      public String getName() {
        return this.name;
      }

      public void setParent(ConfigSection parent) {
        this.parent = parent;
      }

      public void setName(String name) {
        this.name = name;
      }

      public void setValue(String value) {
        this.value = value;
      }

      public String getValue() {
        return this.value;
      }

      public void setType(Type type) {
        this.type = type;
      }

      public Type getType() {
        return type;
      }
    };
  }

  public ConfigSection newConfigSection() {
    return new ConfigSection() {
      private String name;
      private ConfigSection parent;
      private Map<String, ConfigNode> children = new HashMap<>();
      private Type type;

      public ConfigSection getParent() {
        return this.parent;
      }

      public ConfigNode getChild(String name) {
        return this.children.get(name);
      }

      public String getName() {
        return this.name;
      }

      public Collection<ConfigNode> getChildren() {
        return this.children.values();
      }

      public void setParent(ConfigSection parent) {
        this.parent = parent;
      }

      public void addChild(ConfigNode child) {
        child.setParent(this);
        this.children.put(child.getName(), child);
      }

      public void setName(String name) {
        this.name = name;
      }

      public void setType(Type type) {
        this.type = type;
      }

      public Type getType() {
        return type;
      }

      public void foreach(ConfigConsumer consumer) {
        for (ConfigNode node : this.children.values()) {
          consumer.accept(node);
          if (node instanceof ConfigSection)
            ((ConfigSection) node).foreach(consumer);
        }
      }
    };
  }

  public ConfigPath newConfigPath(final String location) {
    return new ConfigPath() {
      private String[] path = location.split("\\.");
      private int index = 0;

      public String next() {
        return path[index++];
      }

      public boolean hasNext() {
        return index < path.length;
      }
    };
  }
}

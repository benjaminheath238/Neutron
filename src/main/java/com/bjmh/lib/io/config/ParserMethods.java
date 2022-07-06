package com.bjmh.lib.io.config;

public class ParserMethods {
  private ParserMethods() {
  }

  public static void main(String[] args) {
    Configuration cpm = new Configuration("root");
    cpm.parse("/home/user/EmilyJennson.ini", INI_PARSER_WITH_ARRAYS_AND_MAPS);
    cpm.foreach(System.out::println);
  }

  public static final ParserMethod INI_PARSER_SIMPLE = new ParserMethod() {
    public void parse(String line, Configuration config) {
      line = removeComments(line);

      if (line.isEmpty())
        return;

      config.addChild(parseSimpleOption(line, config));
    }
  };

  public static final ParserMethod INI_PARSER_WITH_SECTIONS = new ParserMethod() {
    private ConfigSection section = null;

    public void parse(String line, Configuration config) {
      line = removeComments(line);

      if (line.isEmpty())
        return;

      if (isHeader(line)) {
        section = parseHeader(line, config);
      } else {
        firstNonNull(section, config)
            .addChild(parseSimpleOption(line, firstNonNull(section, config)));
      }
    }
  };

  public static final ParserMethod INI_PARSER_WITH_COMPLEX_OPTIONS = new ParserMethod() {
    private ConfigSection section = null;

    public void parse(String line, Configuration config) {
      line = removeComments(line);

      if (line.isEmpty())
        return;

      if (isHeader(line)) {
        section = parseHeader(line, config);
      } else if (isComplexOption(line)) {
        firstNonNull(section, config)
            .addChild(parseComplexOption(line, firstNonNull(section, config)));
      } else {
        firstNonNull(section, config)
            .addChild(parseSimpleOption(line, firstNonNull(section, config)));
      }
    }
  };

  public static final ParserMethod INI_PARSER_WITH_SUB_SECTIONS = new ParserMethod() {
    private ConfigSection section = null;

    public void parse(String line, Configuration config) {
      line = removeComments(line);

      if (line.isEmpty())
        return;

      if (isSubHeader(line)) {
        section = parseSubHeader(line, config);
      } else if (isHeader(line)) {
        section = parseHeader(line, config);
      } else if (isComplexOption(line)) {
        firstNonNull(section, config).addChild(parseComplexOption(line, config));
      } else {
        firstNonNull(section, config)
            .addChild(parseSimpleOption(line, firstNonNull(section, config)));
      }
    }
  };

  public static final ParserMethod INI_PARSER_WITH_INHERITANCE = new ParserMethod() {
    private ConfigSection section = null;

    public void parse(String line, Configuration config) {
      line = removeComments(line);

      if (line.isEmpty())
        return;

      if (isSubHeader(line)) {
        section = inheritOptions(parseSubHeader(line, config));
      } else if (isHeader(line)) {
        section = parseHeader(line, config);
      } else if (isComplexOption(line)) {
        firstNonNull(section, config)
            .addChild(
                inheritOptions(
                    parseComplexOption(line, firstNonNull(section, config))));
      } else {
        firstNonNull(section, config)
            .addChild(parseSimpleOption(line, firstNonNull(section, config)));
      }
    }
  };

  public static final ParserMethod INI_PARSER_WITH_ARRAYS_AND_MAPS = new ParserMethod() {
    private ConfigSection section = null;

    public void parse(String line, Configuration config) {
      line = removeComments(line);

      if (line.isEmpty())
        return;

      if (isSubHeader(line)) {
        section = inheritOptions(parseSubHeader(line, config));
      } else if (isHeader(line)) {
        section = parseHeader(line, config);
      } else if (isComplexOption(line)) {
        firstNonNull(section, config)
            .addChild(
                inheritOptions(
                    parseComplexOption(line, firstNonNull(section, config))));
      } else if (isNullMapOrArray(line)) {
        // Catch the case of an empty array or map
      } else if (isMap(line)) {
        firstNonNull(section, config)
            .addChild(inheritOptions(parseMap(line, firstNonNull(section, config))));
      } else if (isArray(line)) {
        firstNonNull(section, config)
            .addChild(inheritOptions(parseArray(line, firstNonNull(section, config))));
      } else {
        firstNonNull(section, config)
            .addChild(parseSimpleOption(line, firstNonNull(section, config)));
      }
    }
  };

  public static ConfigOption parseSimpleOption(
      String line, ConfigSection parent) {
    ConfigOption option = new ConfigOption();

    option.setParent(parent);
    option.setName(line.split("=")[0].trim());
    option.setValue(line.split("=")[1].replace("\"", "").trim());
    option.setType(ConfigNode.Type.SIMPLE_OPTION);

    return option;
  }

  public static ConfigSection parseComplexOption(
      String line, ConfigSection parent) {
    ConfigSection section = new ConfigSection();
    line = line.replaceAll("[\\(\\)]", "").trim();

    section.setParent(parent);
    section.setName(line.split("=")[0]);
    section.setType(ConfigNode.Type.COMPLEX_OPTION);

    if (line.endsWith("="))
      return section;

    for (String option : line.split("=", 2)[1].split(",")) {
      section.addChild(parseSimpleOption(option, section));
    }

    return section;
  }

  public static ConfigSection parseHeader(String line, Configuration config) {
    ConfigSection section = new ConfigSection();

    section.setParent(config);
    section.setName(line.replaceAll("[\\[\\]]", ""));
    section.setType(ConfigNode.Type.SIMPLE_SECTION);

    config.addChild(section);

    return section;
  }

  public static ConfigSection parseSubHeader(String line, Configuration config) {
    String[] headers = line.replaceAll("[\\[\\]]", "").split("\\.");

    ConfigSection section = new ConfigSection();
    ConfigSection current = config;

    for (int i = 0; i < headers.length - 1; i++) {
      if (!isSectionPresent(headers[i], current)) {
        break;
      }

      current = (ConfigSection) current.getChild(headers[i]);
    }

    section.setName(headers[headers.length - 1]);
    section.setType(ConfigNode.Type.SIMPLE_SECTION);

    current.addChild(section);

    return section;
  }

  public static ConfigSection inheritOptions(ConfigSection section) {
    for (ConfigNode node : section.getParent().getChildren()) {
      if (node.getType().isSection())
        continue;

      if (node.getType().canIterate()) {
        ConfigSection copy = (ConfigSection) node;
        copy.foreach(new ConfigConsumer() {
          private ConfigSection section = null;

          @Override
          public void accept(ConfigNode node) {
            if (node.getType().canIterate()) {
              section = (ConfigSection) node;
            } else {
              section.addChild(
                  new ConfigOption(node.getParent(), node.getName(), node.getType(), ((ConfigOption) node).getValue()));
            }
          }

        });
      } else {
        section.addChild(
            new ConfigOption(node.getParent(), node.getName(), node.getType(), ((ConfigOption) node).getValue()));
      }
    }

    return section;
  }

  public static ConfigSection parseArray(String line, ConfigSection parent) {
    line = line.replaceAll("[\"\\{\\}]*", "");
    String[] contents = line.split("=", 2)[1].split(",");

    ConfigSection section = new ConfigSection();
    section.setParent(parent);
    section.setName(line.split("=")[0]);
    section.setType(ConfigNode.Type.ARRAY_OPTION);

    for (int i = 0; i < contents.length; i++) {
      ConfigOption option = new ConfigOption();

      option.setParent(section);
      option.setName(String.valueOf(i));
      option.setValue(contents[i].trim());
      option.setType(ConfigNode.Type.SIMPLE_OPTION);

      section.addChild(option);
    }

    return section;
  }

  public static ConfigSection parseMap(String line, ConfigSection parent) {
    line = line.replaceAll("[\"\\{\\}]*", "");
    String[] contents = line.split("=", 2)[1].split(",");

    ConfigSection section = new ConfigSection();
    section.setParent(parent);
    section.setName(line.split("=", 2)[0]);
    section.setType(ConfigNode.Type.MAP_OPTION);

    for (String content : contents) {
      ConfigOption option = new ConfigOption();
      option.setParent(section);
      option.setName(content.split("=")[0].trim());
      option.setValue(content.split("=")[1].trim());
      option.setType(ConfigNode.Type.SIMPLE_OPTION);

      section.addChild(option);
    }

    return section;
  }

  public static String removeComments(String line) {
    return line.replaceAll("[#;].*", "").trim();
  }

  public static boolean isHeader(String line) {
    return line.matches("\\[.*\\]");
  }

  public static boolean isSubHeader(String line) {
    return line.matches("\\[(.*\\.?)*\\]");
  }

  public static boolean isComplexOption(String line) {
    return line.matches(".*=\\(.*\\)");
  }

  public static boolean isArray(String line) {
    return line.matches(".*=\\{(.*,?)+\\}");
  }

  public static boolean isMap(String line) {
    return line.matches(".*=\\{(.*=.*,?)+\\}");
  }

  public static boolean isNullMapOrArray(String line) {
    return line.matches(".*=\\{\\}");
  }

  public static boolean isSectionPresent(String name, ConfigSection section) {
    return section.getChild(name) instanceof ConfigSection;
  }

  public static ConfigSection firstNonNull(ConfigSection... sections) throws NullPointerException {
    for (ConfigSection section : sections) {
      if (section != null)
        return section;
    }
    throw new NullPointerException();
  }
}

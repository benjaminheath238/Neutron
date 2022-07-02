package com.bjmh.lib.io.config;

public class ParserMethods {
  public static final ParserMethod INI_PARSER_SIMPLE =
      new ParserMethod() {
        public void parse(String line, Configuration config) {
          line = removeComments(line);

          if (line.isEmpty()) return;

          config.addChild(parseSimpleOption(line, config, config));
        }
      };

  public static final ParserMethod INI_PARSER_WITH_SECTIONS =
      new ParserMethod() {
        private ConfigSection section = null;

        public void parse(String line, Configuration config) {
          line = removeComments(line);

          if (line.isEmpty()) return;

          if (isHeader(line)) {
            section = parseHeader(line, config);
          } else {
            section.addChild(parseSimpleOption(line, config, section == null ? config : section));
          }
        }
      };

  public static final ParserMethod INI_PARSER_WITH_COMPLEX_OPTIONS =
      new ParserMethod() {
        private ConfigSection section = null;

        public void parse(String line, Configuration config) {
          line = removeComments(line);

          if (line.isEmpty()) return;

          if (isHeader(line)) {
            section = parseHeader(line, config);
          } else if (isComplexOption(line)) {
            line = line.replaceAll("[\"\\(\\)]", "").trim();
            section.addChild(parseComplexOption(line, config, section == null ? config : section));
          } else {
            section.addChild(parseSimpleOption(line, config, section == null ? config : section));
          }
        }
      };

  public static final ParserMethod INI_PARSER_WITH_SUB_SECTIONS =
      new ParserMethod() {
        private ConfigSection section = null;

        public void parse(String line, Configuration config) {
          line = removeComments(line);

          if (line.isEmpty()) return;

          if (isSubHeader(line)) {
            section = parseSubHeader(line, config);
          } else if (isHeader(line)) {
            section = parseHeader(line, config);
          } else if (isComplexOption(line)) {
            line = line.replaceAll("[\\(\\)]", "").trim();
            section.addChild(parseComplexOption(line, config, config));
          } else {
            section.addChild(parseSimpleOption(line, config, section == null ? config : section));
          }
        }
      };

  public static final ParserMethod INI_PARSER_WITH_INHERITANCE =
      new ParserMethod() {
        private ConfigSection section = null;

        public void parse(String line, Configuration config) {
          line = removeComments(line);

          if (line.isEmpty()) return;

          if (isSubHeader(line)) {
            section = inheritOptions(parseSubHeader(line, config), config);
          } else if (isHeader(line)) {
            section = parseHeader(line, config);
          } else if (isComplexOption(line)) {
            line = line.replaceAll("[\\(\\)]", "").trim();
            section.addChild(inheritOptions(parseComplexOption(line, config, section), config));
          } else {
            section.addChild(parseSimpleOption(line, config, section == null ? config : section));
          }
        }
      };

  private static ConfigOption parseSimpleOption(
      String line, Configuration config, ConfigSection parent) {
    ConfigOption option = config.newConfigOption();

    option.setParent(parent);
    option.setName(line.split("=")[0].trim());
    option.setValue(line.split("=")[1].replace("\"", "").trim());

    return option;
  }

  private static ConfigSection parseComplexOption(
      String line, Configuration config, ConfigSection parent) {
    ConfigSection section = config.newConfigSection();

    section.setParent(parent);
    section.setName(line.split("=")[0]);

    if (line.endsWith("=")) return section;

    for (String option : line.split("=", 2)[1].split(",")) {
      section.addChild(parseSimpleOption(option, config, section));
    }

    return section;
  }

  private static ConfigSection parseHeader(String line, Configuration config) {
    ConfigSection section = config.newConfigSection();

    section.setParent(config);
    section.setName(line.replaceAll("[\\[\\]]", ""));

    config.addChild(section);

    return section;
  }

  private static ConfigSection parseSubHeader(String line, Configuration config) {
    String[] headers = line.replaceAll("[\\[\\]]", "").split("\\.");

    ConfigSection section = config.newConfigSection();
    ConfigSection current = config;

    for (int i = 0; i < headers.length - 1; i++) {
      if (!isSectionPresent(headers[i], current)) {
        break;
      }

      current = (ConfigSection) current.getChild(headers[i]);
    }

    section.setName(headers[headers.length - 1]);

    current.addChild(section);

    return section;
  }

  private static ConfigSection inheritOptions(ConfigSection section, Configuration config) {

    for (ConfigNode node : section.getParent().getChildren()) {
      if (node instanceof ConfigOption) {
        ConfigOption option = config.newConfigOption();

        option.setParent(section);
        option.setName(node.getName());
        option.setValue(((ConfigOption) node).getValue());

        if (section.getChild(option.getName()) == null) section.addChild(option);
      }
    }

    return section;
  }

  private static String removeComments(String line) {
    return line.replaceAll("[#;].*", "").trim();
  }

  private static boolean isHeader(String line) {
    return line.matches("\\[.*\\]");
  }

  private static boolean isSubHeader(String line) {
    return line.matches("\\[(.*\\..*)*\\]");
  }

  private static boolean isComplexOption(String line) {
    return line.matches(".*=\\(.*\\)");
  }

  private static boolean isSectionPresent(String name, ConfigSection section) {
    return section.getChild(name) != null && section.getChild(name) instanceof ConfigSection;
  }
}

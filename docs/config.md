# Configuration

## Basic Class Overview

Neutron handles config files by creating a tree similar to that used for XML parsing in the standerd Java packages.

The base type in the created tree is the `abstract class ConfigNode` that contains the most simple functionality such as naming the node, setting the nodes type and setting the nodes parent. This class is extended by two other classes ConfigOption and ConfigSection.

The `abstract class ConfigOption extends ConfigNode` is the primary method for creating and using key-value pairs. The only new methods are `String getValue()` and `void setValue(String)`

The `abstract class ConfigSection extends ConfigNode` class is the primary method for ordering ConfigOptions. This class supports child ConfigNodes. It has the new methods `void addChild(ConfigNode child)`, `ConfigNode getChild(String name)` and `Collection<ConfigNode> getChildren()`.

To explain, an example is; if the ConfigOption is a file then ConfigSection is a folder, so ConfigSections can have ConfigOptions and other ConfigSections as children.

The `class Configuration extends ConfigSection` contains all the default implementations that can be accessed via the appropriate method. The Configuration class serves as the top level ConfigSection that all children can be accessed from. Children can be accessed either by using the name of the child but this only works for direct children the alternative method is using the `ConfigPath` interface and parsing it the binary name of the child (`section.subsection.name`).

### Node Types

The type of ConfigNode is set when the file is parsed. The Type can be found in the ConfigNode class. The types are as follows

| Name             | canIterate | isSection |
|------------------|------------|-----------|
| `SIMPLE_SECTION` | True       | True      |
| `ROOT_SECTION`   | True       | True      |
| `SIMPLE_OPTION`  | False      | False     |
| `COMPLEX_OPTION` | True       | False     |
| `MAP_OPTION`     | True       | False     |
| `ARRAY_OPTION`   | True       | False     |

These can be used to chooce how to operate on a ConfigNode. The `ROOT_SECTION` is the Configuration object itself and is not used by anything else.

---

## Iteration

The ConfigSection class supports iteration throught its child nodes via either calling the methods `void foreach(ConfigConsumer)` or by calling the `Collextion<ConfigNode> getChildren()` and iterating through the result.

The functional interface `ConfigConsumer extends Consumer` is used for iteration via the `void foreach(ConfigConsumer)` method, like its super interface the functional method is `void accept(CondigNode)`.

### Order Of Iteration

If the config tree looks like this

```
     CfgScn0.0
    /       \
CfgScn1.0 CfgScn1.1
         /         \
       CfgScn2.0 CfgScn2.1
```

Then the order of iteration will be this

```
CfgScn0 -> CfgScn1.1 -> CfgScn2.1 -> CfgScn2.0 -> CfgScn1.0
```

It should be noted that the above is implemenation specific and may vary and in some cases (arrays) iteration may not be as expected.

---

## Parsing

There is no default parser and instead the config file is parsed using the `ParserMethod` interface but there are several simple implementations of this iterface in the `ParserMethods` class and the methods used can be found in the same class. The supported parsers are

1. `INI_PARSER_SIMPLE`
2. `INI_PARSER_WITH_SECTIONS`
3. `INI_PARSER_WITH_COMPLEX_OPTIONS`
4. `INI_PARSER_WITH_SUB_SECTIONS`
5. `INI_PARSER_WITH_INHERITANCE`
6. `INI_PARSER_WITH_ARRAYS_AND_MAPS`

Each implementation extends the last so `INI_PARSER_WITH_SUB_SECTIONS` does everything that `INI_PARSER_SIMPLE` does and more.

All the parser methods can have comments using the `#` or `;` and the use of `"` around values is not needed. It is not needed for all options to be in a section but may cause unplanned inheritance.

The `INI_PARSER_SIMPLE` implementation only parses key-value pairs of the form `key="value"` and adds them to the root Configuration object.

The `INI_PARSER_WITH_SECTIONS` implementation can also parse sections defined using headers in the form `[name]`.

The `INI_PARSER_WITH_COMPLEX_OPTIONS` implementation can also parse key-value pairs that are of the form `name=(key0="value" ...)`.

The `INI_PARSER_WITH_SUB_SECTIONS` implementation can also parse sub-sections that are of the form `[name0.name1 ...]`. Sub-sections can be nested infinitly. All super-section this sub-section belongs to must already exist.

The `INI_PARSER_WITH_INHERITANCE` implementation can not parse any new types but will cause all the simple options and complex options of sections and to be inherited by there sub-sections and complex options.

The `INI_PARSER_WITH_ARRAYS_AND_MAPS` implementation also can parse arrays and maps where the former has the form `key={"value0", "value1" ...}` and values are indexed using numbers based on position and the latter has the form `key={key0="value0", key1="value1" ...}` and the values are linked to there key. Map and arrays are also inherited.
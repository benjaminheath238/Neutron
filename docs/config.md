# Configuration

## Basic Class Overview

Neutron handles config files by creating a tree similar to that used for XML parsing in the standard Java packages.

The base type in the created tree is the `ConfigNode` class that contains the most simple functionality such as naming the node, setting the nodes type and setting the nodes parent. This class is extended by two other classes ConfigOption and ConfigSection.

The `ConfigOption` class is the primary method for creating and using key-value pairs.

The `ConfigSection` class is the primary method for ordering ConfigOptions. This class supports child ConfigNodes.

To explain, an example is; if the ConfigOption is a file then ConfigSection is a folder, so ConfigSections can have ConfigOptions and other ConfigSections as children just like the folder can have sub-folders and files as children.

The `Configuration` class serves as the top level ConfigSection that all children can be accessed from. Children can be accessed either by using the name of the child but this only works for direct children the alternative method is using the `ConfigPath` class and parsing it the binary name of the child (`section.subsection.name`) this method allows for access of ConfigNodes at any level of the tree.

### Node Types

The type of a ConfigNode is set when the file is parsed. The Type enum can be found in the ConfigNode class. The types are as follows

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

The ConfigSection class supports iteration through its child nodes via either calling the method `void foreach(ConfigConsumer)` or by iterating through the result of calling the `Collection<ConfigNode> getChildren()` method but this method will only iterate through a ConfigSections direct children.

The functional interface `ConfigConsumer` is used for iteration via the `void foreach(ConfigConsumer)` method, like its super interface the functional method is `void accept(ConfigNode)`.

### Order Of Iteration

The order of iteration is non reliable using the `void foreach(ConfigConsumer)` method therefore if the order is important then this method should be overwritten.

---

## Parsing

There is no default parser and instead the config file is parsed using the `ParserMethod` interface, there are several simple implementations of this iterface in the `ParserMethods` class and the methods used can be found in the same class. The supported parsers are

1. `INI_PARSER_SIMPLE`
2. `INI_PARSER_WITH_SECTIONS`
3. `INI_PARSER_WITH_COMPLEX_OPTIONS`
4. `INI_PARSER_WITH_SUB_SECTIONS`
5. `INI_PARSER_WITH_INHERITANCE`
6. `INI_PARSER_WITH_ARRAYS_AND_MAPS`

Each implementation extends the last so `INI_PARSER_WITH_SUB_SECTIONS` does everything that `INI_PARSER_SIMPLE` does and more.

All the parser methods can have comments using either `#` or `;`. The use of `"` around values is not needed. It is not needed for all options to be in a section but may cause unplanned inheritance.

The `INI_PARSER_SIMPLE` implementation only parses key-value pairs of the form `key="value"` and adds them to the root Configuration object.

The `INI_PARSER_WITH_SECTIONS` implementation can also parse sections defined using headers in the form `[name]`. Key-value pairs will be added to the section or root if no sections exists.

The `INI_PARSER_WITH_COMPLEX_OPTIONS` implementation can also parse key-value pairs that are of the form `name=(key0="value" ...)`.

The `INI_PARSER_WITH_SUB_SECTIONS` implementation can also parse sub-sections that are of the form `[name0.name1 ...]`. Sub-sections can be nested infinitly. All super-sections this sub-section belongs to must already exist.

The `INI_PARSER_WITH_INHERITANCE` implementation can not parse any new types but will cause all the simple options and complex options of sections and to be inherited by there sub-sections and complex options.

The `INI_PARSER_WITH_ARRAYS_AND_MAPS` implementation also can parse arrays and maps where the former has the form `key={"value0", "value1" ...}` and values are indexed using numbers based on position and the latter has the form `key={key0="value0", key1="value1" ...}` and the values are linked to there key. Map and arrays are inherited.
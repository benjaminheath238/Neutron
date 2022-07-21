# Neutron

Neutron is a library written in java containing some functionality such as config file parsing and network operations (not currently implemented).

---

## Installation

### Requirements

* [Gradle](https://gradle.org/install/)
* [Java 17](https://adoptium.net/en-GB/)

### Process

1. Download the source code
2. Navigate into the directory containing the `gradlew` file
3. Enter in a terminal/shell either `.\gradlew.bat publishToMavenLocal` or `./gradlew publishToMavenLocal` for Windows or Linux systems respectively
4. Add Neutron as a dependency as shown below

### Adding Neutron to a Maven project

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.bjmh.lib</groupId>
        <artifactId>neutron</artifactId>
        <version>[SUB VERSION HERE]</version>
    </dependency>
    ...
</dependencies>
```

### Adding Neutron to a Gradle project

```groovy
dependencies {
    ...
    implementation 'com.bjmh.lib:neutron:[SUB VERSION HERE]'
    ...
}
```

---

## Documentation

* [Config Files](./docs/config.md)

---

## Licensing

This library is licensed under the MIT license the definition of which can be found in the [LICENSE](LICENSE) file.

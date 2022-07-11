# Neutron

Neutron is a library written in java containing some common functionality such as config file parsing and network operations (not currently implemented).

---

## Installation

Neutron uses [Apache Maven](https://maven.apache.org/) as the build tool and Java 17 both of which must be installed for the following to function.

1. Download the source code either from [GitHub](https://github.com/benjaminheath238/Neutron/archive/refs/heads/master.zip) directly or via `git clone https://github.com/benjaminheath238/Neutron.git`
    * If the code was downloaded from GitHub the zip file must be unzipped
2. Enter the downloaded folder containing the `pom.xml` 
3. In a termial/shell enter one of the following
    * `mvn package` to create a jar file in `target/neutron-X.x.jar`
    * `mvm install` to install Neutron to your local Maven repository
4. Neutron can now be appropriately added as a dependency of projects

### Adding Neutron to a Maven project

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.bjmh.lib</groupId>
        <artifactId>Neutron</artifactId>
        <version>[SUB VERSION HERE]</version>
    </dependency>
    ...
</dependencies>
```

### Adding Neutron to a Gradle project

```groovy
dependencies {
    ...
    neutron 'com.bjmh.lib:neutron:[SUB VERSION HERE]'
    ...
}
```

---

## Documentation

* [Config Files](./docs/config.md)

---

## Licensing

This library is licensed under the MIT license the definition of which can be found in the [LICENSE](LICENSE) file.

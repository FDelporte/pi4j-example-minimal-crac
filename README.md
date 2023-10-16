Minimal Pi4J Example Application Using CRaC
===========================================

This project is based on [github.com/Pi4J/pi4j-example-minimal](https://github.com/Pi4J/pi4j-example-minimal) to experiment with the use of [CRaC](https://docs.azul.com/core/crac/crac-introduction) combined with Pi4J. It builds a FAT JAR as explained on [Build as a FAT JAR with Maven](https://pi4j.com/documentation/building/fat-jar/).

## Requirements

As described on the blog post [Running a CRaC Java application on Raspberry Pi - UPDATE](https://webtechie.be/post/2023-10-16-crac-on-raspberry-pi-update/).

* Raspberry Pi OS, 64-bit, Bookworm edition, released on October 11, 2023.
* Azul Zulu Builds of OpenJDK, version 17 or 21 with CRaC:
  * `17.0.8.crac-zulu`
  * `21.crac-zulu`
  * or newer

## Run with CRaC on Raspberry Pi

### Get the Project

```bash
$ git clone https://github.com/FDelporte/pi4j-example-minimal-crac.git
$ cd pi4j-example-minimal-crac
```

### Build and Initial Run

#### First Terminal

This Pi4J application needs to be executed as sudo to have the needed privileges to interact with the GPIOs.

```bash
$ mvn package
$ sudo `which java` -XX:CRaCCheckpointTo=cr -jar target/pi4j-crac.jar
```

#### Second Terminal

```bash
$ sudo `which jcmd` target/pi4j-crac.jar JDK.checkpoint
```

### Start From Snapshot



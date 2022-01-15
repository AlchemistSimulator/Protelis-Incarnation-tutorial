# Alchemist tutorial with Protelis

This tutorial includes a series of exercises of increasing complexity that showcase the Alchemist simulator's Protelis incarnation.

## How to launch

### Prerequisites

The only prerequisite is a Java version compatible with Gradle.
This project is configured to automatically upgrade the version of Gradle it relies upon,
so usually the last entry of the [Gradle-Java compatibility matrix](https://docs.gradle.org/current/userguide/compatibility.html) is the one to consider.
At the time of writing, the minimum required version is Java 8.
You can verify the version by running `java -version` in a terminal.
In case you have no Java installed, we suggest to use the latest LTS version from [AdoptOpenJDK](https://adoptopenjdk.net/).

Note that, internally, the simulator requires Java 11 or above.
The project is configured to self-provision a compatible Java environment,
assuming that network connection is available.

### Using Gradle

The easiest way to launch the examples is by relying on the pre-configured [Gradle][Gradle] build script.
It will automatically download all the required libraries, set up the environment, and execute the simulator via command line for you.

Gradle has been preconfigured in this repository for executing available Alchemist simulations inside `src/main/yaml/`.
For each such file, one Gradle task is generated.
By executing `./gradlew tasks`, these tasks will be displayed in the `Run Alchemist tasks` category, similar to (this may change):

```
Run Alchemist tasks
-------------------
run00-minimal - Launches simulation 00-minimal
run01-nodes - Launches simulation 01-nodes
run02-manynodes - Launches simulation 02-manynodes
run03-grid - Launches simulation 03-grid
run04-perturb-grid - Launches simulation 04-perturb-grid
run05-content - Launches simulation 05-content
run06-program - Launches simulation 06-program
run07-gradient - Launches simulation 07-gradient
runAll - Launches all simulations

```

Alchemist uses JSON files for modelling effects with the classic Swing UI.
If an effect file with the same name of the YAML simulation file is found in the `effects` folder,
it will be loaded as default graphics file.
If none is found, Alchemist will be started with the default effects set
(you can always fiddle with the rendering from within the simulator).

As first step, use `git` to locally clone this repository.

In order to launch all simulations, open a terminal and move to the project root folder, then on UNIX:
```bash
./gradlew runAll
```
On Windows:
```
gradlew.bat runAll
```

Note that the first launch will be rather slow, since Gradle will download all the required files.
They will get cached in the user's home folder (as per Gradle normal behavior).

### Using the redistributable jar file

If you got the redistributable, runnable Alchemist jar file (download it [here][Alchemist releases], you can use the alchemist command line to execute the provided simulations.

To learn which commands the alchemist CLI offers, use:

```bash
java -jar alchemist-redist-VERSION.jar --help
```
(remember to substitute VERSION with the alchemist version you actually have downloaded)

Suppose that you have copied such jar in the project root folder, and you want to launch the file `foo.yml` located in `src/main/yaml` using the effects file `effects/bar.aes`. You can do so by running:
```bash
java -jar alchemist-redist-VERSION.jar -y src/main/yaml/foo.yml -g effects/bar.aes
```
If your YAML simulation file refers to an external protelis resource, that should be located in the classpath to get correctly loaded.
In this case, you may want to manually control the classpath to make sure everything is correctly considered.
Let's suppose that you store your Protelis modules structure in a `src/main/protelis` folder located in the root project folder. In this case you may want to issue:
```bash
java -cp alchemist-redist-VERSION.jar:src/main/protelis it.unibo.alchemist.Alchemist -y src/main/yaml/foo.yml -g effects/bar.aes
```
If you are running Alchemist on Windows and using the DOS command interpreter, be wary that that the colon must get substituted by a semicolon:
```bash
java -cp alchemist-redist-VERSION.jar;src/main/protelis it.unibo.alchemist.Alchemist -y src/main/yaml/foo.yml -g effects/bar.aes
```

## Graphical interface shortcuts

The graphical interface that pops up when Alchemist is executed providing a YAML file on the command line is a stripped-down version of the one that shows when the simulator is invoked by `java -jar` or by double clicking.

In order to use such interface, consider the following command list:

| Key binding | Active          | Effect                                                                |
| ------------ | -------------- | --------------------------------------------------------------------- |
| L            | always         | (En/Dis)ables the painting of links between nodes                     |
| M            | always         | (En/Dis)ables the painting of a marker on the closest node            |
| Mouse pan    | in normal mode | Moves around                                                          |
| Mouse wheel  | in normal mode | Zooms in/out                                                          |
| Double click | in normal mode | Opens a frame with the closest node information                       |
| Right click  | in normal mode | Enters screen rotation mode                                           |
| P            | always         | Plays/pauses the simulation                                           |
| R            | always         | Enables the real-time mode                                            |
| Left arrow   | always         | Speeds the simulation down (more calls to the graphics)               |
| Right arrow  | always         | Speeds the simulation up (less calls to the graphics)                 |
| S            | always         | Enters / exits the select mode (nodes can be selected with the mouse) |
| O            | in select mode | Selected nodes can be moved by drag and drop                          |
| E            | in select mode | Enters edit mode (to manually change node contents)                   |


## Eclipse users

This project includes the eclipse configuration files and can be imported directly in the IDE.
The usage of Gradle Buildship is recommended (it is shipped with Eclipse Mars and newer, and it is available in the Eclipse Marketplace anyway).
Use File -> Import -> Git -> Import project from Git and paste the address of this repository to start cloning.


[Alchemist releases]: https://github.com/AlchemistSimulator/Alchemist/releases
[Gradle]: http://gradle.org/

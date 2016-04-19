# Alchemist tutorial with Protelis

This tutorial includes a series of exercises of increasing complexity that showcase the Alchemist simulator's Protelis incarnation.


## How to launch

### Prerequisites

The only prerequisite is a working version of Java.

### Using Gradle

The easiest way to launch the examples is by relying on the pre-configured [Gradle][Gradle] build script. It will automatically download all the required libraries, set up the environment, and execute the simulator via command line for you.
As first step, select the example you want to run by editing the `gradle.properties` file. After `simulation = ` write the name of the file you want Alchemist to use, without the `.yml` extension.
Alchemist uses so called "Alchemist Graphics Stack files" (`.aes` extension) to nicely paint the nodes. If a `.aes` file is with the same name of the YAML simulation file is found in the `effects` folder, then it will be loaded as default graphics file. If none is found, Alchemist will be started with the default effects set (you can always fiddle with the rendering from within the simulator).
As first step, use `git` to locally clone this repository.
In order to launch, open a terminal and move to the project root folder, then on UNIX:
```bash
./gradlew runAlchemist
```
On Windows:
```
gradlew.bat runAlchemist
```

Note that the first launch will be rather slow, since Gradle will download all the required files. They will get cached in the user's home folder (as per Gradle normal behavior).

### Using the redistributable jar file

If you got the redistributable, runnable Alchemist jar file, you can use the alchemist command line to execute the provided simulations.

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


[Gradle]: http://gradle.org/
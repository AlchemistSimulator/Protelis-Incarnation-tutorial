plugins {
    java
    kotlin("jvm") version "1.3.41"
}

repositories { mavenCentral() }

sourceSets {
    test {
        resources {
            srcDir("src/main/protelis")
        }
    }
}

dependencies {
    implementation("it.unibo.alchemist:alchemist:${extra["alchemistVersion"].toString()}")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:${extra["junitVersion"].toString()}")
}

val jar by tasks.getting(Jar::class) {
    archiveName = "classpath.jar"
    manifest {
        attributes["Class-Path"] = files(configurations.runtimeClasspath)
                .map { it.toURI() }
                .joinToString(" ")
    }
}
val simulation = extra["simulation"].toString()
tasks.register<JavaExec>("runAlchemist") {
    dependsOn("compileJava")
    dependsOn("jar")
    // clean up the classpath because the launcher jar has it.
    classpath = files(jar.archivePath)
    classpath("src/main/protelis")
    main = "it.unibo.alchemist.Alchemist"
    args("-y", "src/main/yaml/$simulation.yml", "-g", "effects/$simulation.aes")
}

defaultTasks("runAlchemist")

tasks.test { useJUnitPlatform() }
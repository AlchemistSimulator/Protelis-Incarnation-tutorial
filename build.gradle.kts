plugins {
    java
    kotlin("jvm")
}

repositories { mavenCentral() }

sourceSets {
    main {
        resources {
            srcDir("src/main/protelis")
            srcDir("src/main/yaml")
        }
    }
}

fun onJava16AndAbove(body: () -> Unit) {
    if (JavaVersion.current() >= JavaVersion.VERSION_16) {
        body()
    }
}

dependencies {
    implementation("it.unibo.alchemist:alchemist:_")
    implementation("it.unibo.alchemist:alchemist-swingui:_")
    implementation("it.unibo.alchemist:alchemist-incarnation-protelis:_")
    testImplementation("it.unibo.alchemist:alchemist-euclidean-geometry:_")
    testImplementation("io.kotest:kotest-runner-junit5:_")
    testImplementation("io.kotest:kotest-assertions-core-jvm:_")
    onJava16AndAbove {
        runtimeOnly("com.google.inject:guice:_")
        runtimeOnly("org.eclipse.xtext:org.eclipse.xtext:_")
        runtimeOnly("org.eclipse.xtext:org.eclipse.xtext.xbase:_")
    }
}


val batch: String by project
val maxTime: String by project

val alchemistGroup = "Run Alchemist"
/*
 * This task is used to run all experiments in sequence
 */
val runAll by tasks.register<DefaultTask>("runAll") {
    group = alchemistGroup
    description = "Launches all simulations"
}

/*
 * Scan the folder with the simulation files, and create a task for each one of them.
 */
File(rootProject.rootDir.path + "/src/main/yaml").listFiles()
        .filter { it.extension == "yml" }
        .sortedBy { it.nameWithoutExtension }
        .forEach {
            val task by tasks.register<JavaExec>("run${it.nameWithoutExtension.capitalize()}") {
                group = alchemistGroup
                description = "Launches simulation ${it.nameWithoutExtension}"
                onJava16AndAbove {
                    jvmArgs("--illegal-access=permit")
                }
                main = "it.unibo.alchemist.Alchemist"
                classpath = sourceSets["main"].runtimeClasspath
                val exportsDir = File("${projectDir.path}/build/exports/${it.nameWithoutExtension}")
                doFirst {
                    if (!exportsDir.exists()) {
                        exportsDir.mkdirs()
                    }
                }
                args("-y", it.absolutePath, "-e", "$exportsDir/${it.nameWithoutExtension}-${System.currentTimeMillis()}")
                if (System.getenv("CI") == "true" || batch == "true") {
                    args("-hl", "-t", maxTime)
                } else {
                    args("-g", "effects/${it.nameWithoutExtension}.json")
                }
                outputs.dir(exportsDir)
            }
            // task.dependsOn(classpathJar) // Uncomment to switch to jar-based cp resolution
            runAll.dependsOn(task)
        }

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

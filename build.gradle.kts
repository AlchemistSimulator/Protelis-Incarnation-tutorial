plugins {
    alias(libs.plugins.kotlin.jvm)
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

// Modules, versions, and bundles are declared in gradle/libs.versions.toml
dependencies {
    implementation(libs.bundles.alchemist)
    testImplementation(libs.bundles.kotest)
    onJava16AndAbove {
        runtimeOnly(libs.guice)
        runtimeOnly(libs.bundles.xtext)
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

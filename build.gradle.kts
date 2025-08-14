plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.taskTree)
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

multiJvm {
    jvmVersionForCompilation.set(17)
}

// Modules, versions, and bundles are declared in gradle/libs.versions.toml
dependencies {
    implementation(libs.bundles.alchemist)
    testImplementation(libs.bundles.kotest)
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
    .orEmpty()
    .apply { check(isNotEmpty()) }
    .filter { it.extension == "yml" }
    .sortedBy { it.nameWithoutExtension }
    .forEach { simulationFile ->
        val simulationName = simulationFile.nameWithoutExtension
        val task by tasks.register<JavaExec>("run${simulationName.replaceFirstChar { it.uppercase() } }") {
            javaLauncher.set(
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(multiJvm.latestJava))
                }
            )
            group = alchemistGroup
            description = "Launches simulation $simulationName"
            mainClass.set("it.unibo.alchemist.Alchemist")
            classpath = sourceSets["main"].runtimeClasspath
            val exportsDir = File("${projectDir.path}/build/exports/${simulationName}")
            doFirst {
                if (!exportsDir.exists()) {
                    exportsDir.mkdirs()
                }
            }
            args("run", simulationFile)
            if (System.getenv("CI") == "true" || batch == "true") {
                args(
                    "--override",
                    """
                    {
                        terminate: { type: AfterTime, parameters: 2 }
                    }
                    """.trimIndent()
                )
            } else {
                args(
                    "--override",
                    "{ monitors: { type: SwingGUI, parameters: { graphics: \"effects/${simulationName}.json\" } } }"
                )
            }
            outputs.dir(exportsDir)
        }
        runAll.dependsOn(task)
    }

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed", "standardError")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

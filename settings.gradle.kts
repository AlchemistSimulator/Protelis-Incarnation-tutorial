import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

rootProject.name = "protelis-alchemist-tutorial"

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard:dependencies:+")
}

bootstrapRefreshVersionsAndDependencies()

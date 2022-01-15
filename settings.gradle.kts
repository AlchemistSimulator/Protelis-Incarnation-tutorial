rootProject.name = "protelis-alchemist-tutorial"

plugins {
    id("com.gradle.enterprise") version "3.8.1"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.0.3"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}

enableFeaturePreview("VERSION_CATALOGS")

gitHooks {
    commitMsg { conventionalCommits() }
    createHooks()
}

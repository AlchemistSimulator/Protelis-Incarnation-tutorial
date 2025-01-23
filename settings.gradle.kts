rootProject.name = "protelis-alchemist-tutorial"

plugins {
    id("com.gradle.enterprise") version "3.19.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.18"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}

gitHooks {
    commitMsg { conventionalCommits() }
    createHooks()
}

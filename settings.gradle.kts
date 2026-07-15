pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    configurations.all {
        resolutionStrategy {
            force("org.apache.commons:commons-lang3:3.18.0")
            force("org.bitbucket.b_c:jose4j:0.9.6")
            force("org.bouncycastle:bcpkix-jdk18on:1.85")
            force("org.bouncycastle:bcprov-jdk18on:1.85")
        }
    }
    dependencies {
        // Override plugin transitive dependencies to resolve Dependabot alerts
        classpath("org.apache.commons:commons-lang3:3.18.0")
        classpath("org.bitbucket.b_c:jose4j:0.9.6")
        classpath("org.bouncycastle:bcpkix-jdk18on:1.85")
        classpath("org.bouncycastle:bcprov-jdk18on:1.85")
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GameScore"
include(":app")

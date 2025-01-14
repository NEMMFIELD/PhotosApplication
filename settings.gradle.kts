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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Photos Application"
include(":app")
include(":core:photos-api")
include(":features:photos-random")
include(":core:state")
include(":features:photos-details")
include(":core:navigation")
include(":features:photos-authorization")
include(":core:workmanager")
include(":core:utils")
include(":features:photos-search")

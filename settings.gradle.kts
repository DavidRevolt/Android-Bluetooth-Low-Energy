pluginManagement {
    repositories {
        google()
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

rootProject.name = "App"
include(":app")
include(":core:data")
include(":core:designsystem")
include(":feature:home")
include(":core:ble")
include(":feature:control")
include(":core:model")

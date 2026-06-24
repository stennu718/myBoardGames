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

rootProject.name = "MyBoardGames"
include(":app")
include(":core:common")
include(":core:database")
include(":core:ui")
include(":feature:chess")
include(":feature:checkers")
include(":feature:sudoku")
include(":feature:blockudoku")
include(":feature:puzzles")
include(":feature:daily")
include(":feature:profile")
include(":feature:puzzlerush")
include(":feature:premium")
include(":feature:stats")
include(":feature:widget")
include(":feature:cloud")
include(":core:sound")
include(":core:review")
include(":core:ads")
include(":feature:onboarding")
include(":feature:multiplayer")
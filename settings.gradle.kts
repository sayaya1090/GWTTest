rootProject.name = "GWTTest"
pluginManagement {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sayaya1090/maven")
            credentials {
                username = project.findProperty("github_username") as String
                password = project.findProperty("github_password") as String
            }
        }
        gradlePluginPortal()
    }
}
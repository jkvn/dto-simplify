plugins {
    id("java")
}

val gitTag: String? = System.getenv("VERSION")
val resolvedVersion = if (gitTag != null && !gitTag.endsWith("SNAPSHOT")) gitTag else "develop"

allprojects {
    group = "at.jkvn.dtosimplify"
    version = resolvedVersion

    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withId("java-library") {
        apply(plugin = "maven-publish")

        afterEvaluate {
            extensions.configure<PublishingExtension>("publishing") {
                publications {
                    create<MavenPublication>("gpr") {
                        from(components["java"])

                        groupId = "at.jkvn.dtosimplify"
                        artifactId = project.name
                        version = resolvedVersion
                    }
                }

                repositories {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/jkvn/dto-simplify")
                        credentials {
                            username = project.findProperty("githubUser") as String? ?: System.getenv("GITHUB_ACTOR")
                            password = project.findProperty("githubToken") as String? ?: System.getenv("GITHUB_TOKEN")
                        }
                    }
                }
            }
        }
    }
}
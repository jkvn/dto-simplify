plugins {
    id("java-library")
}

val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-core:$quarkusPlatformVersion")
    implementation("io.quarkus:quarkus-arc:$quarkusPlatformVersion")

    implementation(project(":dto-simplify-core"))
}
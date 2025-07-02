plugins {
    id("java-library")
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(project(":dto-simplify-quarkus:runtime"))

    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))

    implementation("io.quarkus:quarkus-core-deployment")
    implementation("io.quarkus:quarkus-arc-deployment")
    implementation("io.quarkus:quarkus-smallrye-openapi-deployment")
}
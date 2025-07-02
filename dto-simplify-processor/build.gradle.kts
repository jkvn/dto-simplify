plugins {
    id("java-library")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":dto-simplify-core"))
    implementation(project(":dto-simplify-openapi"))
}

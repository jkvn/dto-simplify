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
    implementation("io.swagger.core.v3:swagger-core:2.2.31")
    implementation(project(":dto-simplify-core"))
}

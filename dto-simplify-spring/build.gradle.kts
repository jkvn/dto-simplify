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
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.5.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation("org.reflections:reflections:0.10.2")

    api(project(":dto-simplify-core"))
    api(project(":dto-simplify-openapi"))
}

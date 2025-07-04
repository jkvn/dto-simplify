plugins {
    id("java-library")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("io.swagger.core.v3:swagger-core:2.2.31")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

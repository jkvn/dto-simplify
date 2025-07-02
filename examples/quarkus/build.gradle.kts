plugins {
    java
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    implementation(project(":dto-simplify-spring"))
    annotationProcessor(project(":dto-simplify-processor"))
    
}
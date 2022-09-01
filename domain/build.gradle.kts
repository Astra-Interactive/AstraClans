plugins {
    java
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.astrainteractive"
version = "2.4.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.Implementation.jdbc)
    implementation(Dependencies.Implementation.exposedJavaTime)
    implementation(Dependencies.Implementation.exposedJDBC)
    implementation(Dependencies.Implementation.exposedCORD)
    implementation(Dependencies.Implementation.exposedDAO)

    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.1.0")

}
tasks.test {
    useJUnitPlatform()
}

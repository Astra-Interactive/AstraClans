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
    mavenLocal()
    mavenCentral()
    flatDir { dirs("libs") }
}

dependencies {
    // Kotlin
    implementation(Dependencies.Implementation.kotlinGradlePlugin)
    // Coroutines
    implementation(Dependencies.Implementation.kotlinxCoroutines)
    implementation(Dependencies.Implementation.kotlinxCoroutinesCore)
    // Serialization
    implementation(Dependencies.Implementation.kotlinxSerialization)
    implementation(Dependencies.Implementation.kotlinxSerializationJson)
    implementation(Dependencies.Implementation.kotlinxSerializationYaml)
}
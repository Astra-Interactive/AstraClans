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
java {
    withSourcesJar()
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}
repositories {
    mavenCentral()
    flatDir { dirs("libs") }
}

dependencies {
    implementation(Dependencies.Implementation.jdbc)
    implementation(Dependencies.Implementation.exposedJavaTime)
    implementation(Dependencies.Implementation.exposedJDBC)
    implementation(Dependencies.Implementation.exposedCORD)
    implementation(Dependencies.Implementation.exposedDAO)
    // AstraLibs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.1.0")

}

tasks {
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
tasks.test {
    useJUnitPlatform()
}

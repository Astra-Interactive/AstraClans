plugins {
    java
    `maven-publish`
    `java-library`
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
    id("com.github.johnrengelman.shadow") version Dependencies.Kotlin.shadow
}

group = Dependencies.group
version = Dependencies.version
java {
    withSourcesJar()
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}
repositories {
    mavenCentral()
    astraLibs(project)
}

dependencies {
    // Database
    implementation(Dependencies.Libraries.jdbc)
    implementation(Dependencies.Libraries.exposedJavaTime)
    implementation(Dependencies.Libraries.exposedJDBC)
    implementation(Dependencies.Libraries.exposedCORD)
    implementation(Dependencies.Libraries.exposedDAO)
    // Serialization
    implementation(Dependencies.Libraries.kotlinxSerialization)
    implementation(Dependencies.Libraries.kotlinxSerializationJson)
    implementation(Dependencies.Libraries.kotlinxSerializationYaml)
    // AstraLibs
    implementation(Dependencies.Libraries.astraLibsKtxCore)
    implementation(Dependencies.Libraries.astraLibsSpigotCore)
    // Test
    testImplementation(kotlin("test"))
    testImplementation(Dependencies.Libraries.orgTeting)

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


group = "com.astrainteractive"
version = "2.4.0"
val name = "AstraClans"
description = "Simple but powerful clans plugin for mincraft servers"

plugins {
    java
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}
java {
    withSourcesJar()
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}
repositories {
    mavenLocal()
    mavenCentral()
    maven(Dependencies.Repositories.extendedclip)
    maven(Dependencies.Repositories.spigotmc)
    maven(Dependencies.Repositories.papermc)
    maven(Dependencies.Repositories.scarsz)
    maven(Dependencies.Repositories.dmulloy2)
    maven(Dependencies.Repositories.essentialsx)
    maven(Dependencies.Repositories.maven2Apache)
    maven(Dependencies.Repositories.enginehub)
    maven(Dependencies.Repositories.maven2)
    maven(Dependencies.Repositories.dv8tion)
    maven(Dependencies.Repositories.playpro)
    maven(Dependencies.Repositories.clojars)
    maven(Dependencies.Repositories.jitpack)
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
    // AstraLibs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Test
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.20")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:2.26.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.1")
    testImplementation("io.kotest:kotest-assertions-core:5.3.1")
    testImplementation(kotlin("test"))
    // Spigot dependencies
    compileOnly(Dependencies.CompileOnly.essentialsX)
    compileOnly(Dependencies.CompileOnly.paperMC)
    compileOnly(Dependencies.CompileOnly.spigotApi)
    compileOnly(Dependencies.CompileOnly.spigot)
    compileOnly(Dependencies.CompileOnly.protocolLib)
    compileOnly(Dependencies.CompileOnly.placeholderapi)
    compileOnly(Dependencies.CompileOnly.worldguard)
    compileOnly(Dependencies.CompileOnly.discordsrv)
    compileOnly(Dependencies.CompileOnly.vaultAPI)
    compileOnly(Dependencies.CompileOnly.coreprotect)
    implementation(project(":domain"))
}

tasks {
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    withType<Jar> {
        archiveClassifier.set("min")
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    test {
        useJUnit()
        testLogging {
            events("passed", "skipped", "failed")
            this.showStandardStreams = true
        }
    }
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                    "name" to project.name,
                    "version" to project.version,
                    "description" to project.description
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}
tasks.shadowJar {
    dependencies {
        include(dependency(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", ".aar")))))
        include(dependency(Dependencies.Implementation.kotlinGradlePlugin))
        include(dependency(Dependencies.Implementation.kotlinxCoroutines))
        include(dependency(Dependencies.Implementation.kotlinxCoroutinesCore))
        include(dependency(Dependencies.Implementation.kotlinxSerialization))
        include(dependency(Dependencies.Implementation.kotlinxSerializationJson))
        include(dependency(Dependencies.Implementation.kotlinxSerializationYaml))
    }
    isReproducibleFileOrder = true
    mergeServiceFiles()
    dependsOn(configurations)
    archiveClassifier.set(null as String?)
    from(sourceSets.main.get().output)
    from(project.configurations.runtimeClasspath)
    minimize {
        exclude(dependency("org.jetbrains.exposed:exposed-jdbc:${Dependencies.Kotlin.exposed}"))
        exclude(dependency("org.jetbrains.exposed:exposed-dao:${Dependencies.Kotlin.exposed}"))
    }
    destinationDirectory.set(File("D:\\Minecraft Servers\\1_19\\paper\\plugins"))
//    destinationDirectory.set(File("/media/makeevrserg/Новый том/Servers/Server/plugins"))
}

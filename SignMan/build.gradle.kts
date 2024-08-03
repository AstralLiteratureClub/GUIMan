//file:noinspection VulnerableLibrariesLocal
plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.freefair.lombok") version "8.6"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("maven-publish")
}

group = "bet.astral"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

var serverVersion = "1.21"
dependencies {
    // LOMBOK
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // PAPER MC
    paperweight.paperDevBundle("$serverVersion-R0.1-SNAPSHOT")
}

tasks.jar {
    enabled = false
    dependsOn(tasks.shadowJar)
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion(serverVersion)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "signman"
            from(components["java"])
        }
    }
}



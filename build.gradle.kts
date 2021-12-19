plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "xyz.tehbrian"
version = "0.1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://repo.thbn.me/snapshots/") {
        name = "thbn-snapshots"
    }
    maven("https://repo.broccol.ai/snapshots/") {
        name = "broccolai-snapshots"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

    implementation("com.google.inject:guice:5.0.1")

    implementation("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")

    implementation("org.spongepowered:configurate-yaml:4.1.2")

    implementation("cloud.commandframework:cloud-paper:1.6.1")
    implementation("cloud.commandframework:cloud-minecraft-extras:1.6.1")

    implementation("dev.tehbrian:tehlib-paper:0.1.0-SNAPSHOT")
    implementation("broccolai.corn:corn-minecraft-paper:3.0.0-SNAPSHOT")
}

tasks {
    processResources {
        expand("version" to project.version)
    }

    shadowJar {
        archiveBaseName.set("Iteminator")
        archiveClassifier.set("")

        val libsPackage = "xyz.tehbrian.iteminator.libs"
        relocate("com.google.inject", "$libsPackage.guice")
        relocate("net.kyori.adventure.text.minimessage", "$libsPackage.minimessage")
        relocate("org.spongepowered.configurate.yaml", "$libsPackage.configurate.yaml")
        relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
        relocate("cloud.commandframework", "$libsPackage.cloud")
    }

    runServer {
        minecraftVersion("1.17.1")
    }
}

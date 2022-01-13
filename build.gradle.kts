plugins {
    id("java")
    id("net.kyori.indra.checkstyle") version "2.0.6"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "xyz.tehbrian"
version = "0.1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    implementation("com.google.inject:guice:5.0.1")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("cloud.commandframework:cloud-minecraft-extras:1.6.1")

    implementation("dev.tehbrian:tehlib-paper:0.1.0-SNAPSHOT")
    implementation("broccolai.corn:corn-minecraft-paper:3.0.0-SNAPSHOT")

    testImplementation("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

/* Disable checkstyle on tests */
project.gradle.startParameter.excludedTaskNames.add("checkstyleTest")

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
        relocate("org.spongepowered.configurate", "$libsPackage.configurate")
        relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
        relocate("broccolai.corn", "$libsPackage.corn")
        relocate("cloud.commandframework", "$libsPackage.cloud")
    }

    runServer {
        minecraftVersion("1.18.1")
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

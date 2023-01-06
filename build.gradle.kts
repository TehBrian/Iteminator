plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("xyz.jpenilla.run-paper") version "2.0.1"
  id("net.kyori.indra.checkstyle") version "3.0.1"
  id("com.github.ben-manes.versions") version "0.44.0"
}

group = "xyz.tehbrian"
version = "0.4.0"
description = "A modern item editing plugin."

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.broccol.ai/releases/")
  maven("https://repo.thbn.me/releases/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

  implementation("broccolai.corn:corn-minecraft-paper:3.2.0")
  implementation("cloud.commandframework:cloud-minecraft-extras:1.8.0")
  implementation("com.google.inject:guice:5.1.0")
  implementation("dev.tehbrian:tehlib-paper:0.4.2")
  implementation("org.spongepowered:configurate-yaml:4.1.2")

  testImplementation("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(platform("org.junit:junit-bom:5.9.1"))
}

tasks {
  assemble {
    dependsOn(shadowJar)
  }

  processResources {
    expand("version" to project.version, "description" to project.description)
  }

  base {
    archivesName.set("Iteminator")
  }

  shadowJar {
    archiveClassifier.set("")

    val libsPackage = "${project.group}.${project.name}.libs"
    relocate("broccolai.corn", "$libsPackage.corn")
    relocate("cloud.commandframework", "$libsPackage.cloud")
    relocate("com.google.inject", "$libsPackage.guice")
    relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
    relocate("org.spongepowered.configurate", "$libsPackage.configurate")
  }

  runServer {
    minecraftVersion("1.19.3")
  }

  test {
    useJUnitPlatform()
    testLogging {
      events("passed", "skipped", "failed")
    }
  }
}

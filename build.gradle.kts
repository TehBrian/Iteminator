plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("xyz.jpenilla.run-paper") version "2.0.0"
  id("net.kyori.indra.checkstyle") version "3.0.1"
}

group = "xyz.tehbrian"
version = "0.2.0"
description = "A modern item editing plugin."

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  mavenCentral()
  maven("https://papermc.io/repo/repository/maven-public/")
  maven("https://repo.broccol.ai/releases/")
  maven("https://repo.thbn.me/releases/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

  implementation("broccolai.corn:corn-minecraft-paper:3.2.0")
  implementation("cloud.commandframework:cloud-minecraft-extras:1.7.1")
  implementation("com.google.inject:guice:5.1.0")
  implementation("dev.tehbrian:tehlib-paper:0.3.1")
  implementation("org.spongepowered:configurate-yaml:4.1.2")

  testImplementation("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
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

  shadowJar {
    archiveBaseName.set("Iteminator")
    archiveClassifier.set("")

    val libsPackage = "${project.group}.${project.name}.libs"
    relocate("broccolai.corn", "$libsPackage.corn")
    relocate("cloud.commandframework", "$libsPackage.cloud")
    relocate("com.google.inject", "$libsPackage.guice")
    relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
    relocate("org.spongepowered.configurate", "$libsPackage.configurate")
  }

  runServer {
    minecraftVersion("1.19.2")
  }

  test {
    useJUnitPlatform()
    testLogging {
      events("passed", "skipped", "failed")
    }
  }
}

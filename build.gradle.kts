plugins {
  id("java")
  id("com.gradleup.shadow") version "8.3.0"
  id("xyz.jpenilla.run-paper") version "2.3.0"
  id("net.kyori.indra.checkstyle") version "3.1.3"
  id("com.github.ben-manes.versions") version "0.51.0"
}

group = "dev.tehbrian"
version = "0.4.0"
description = "A modern item editing plugin."

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.thbn.me/releases/")
  maven("https://repo.broccol.ai/snapshots/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

  implementation("love.broccolai.corn:corn-minecraft:4.0.0-SNAPSHOT")
  implementation("cloud.commandframework:cloud-minecraft-extras:1.8.4")
  implementation("cloud.commandframework:cloud-paper:1.8.4")
  implementation("com.google.inject:guice:7.0.0")
  implementation("dev.tehbrian:tehlib-paper:0.6.0")
  implementation("org.spongepowered:configurate-yaml:4.1.2")

  testImplementation("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(platform("org.junit:junit-bom:5.11.0"))
}

tasks {
  assemble {
    dependsOn(shadowJar)
  }

  processResources {
    filesMatching("plugin.yml") {
      expand(
        "version" to project.version,
        "description" to project.description
      )
    }
  }

  base {
    archivesName.set("Iteminator")
  }

  shadowJar {
    archiveClassifier.set("")

    val libsPackage = "${project.group}.${project.name}.libs"
    fun moveToLibs(vararg patterns: String) {
      for (pattern in patterns) {
        relocate(pattern, "$libsPackage.$pattern")
      }
    }

    moveToLibs(
      "broccolai.corn",
      "cloud.commandframework",
      "com.google",
      "dev.tehbrian.tehlib",
      "io.leangen",
      "jakarta.inject",
      "javax.annotation",
      "net.kyori.examination",
      "org.aopalliance",
      "org.checkerframework",
      "org.spongepowered",
      "org.yaml",
    )
  }

  runServer {
    minecraftVersion("1.21.1")
  }

  test {
    useJUnitPlatform()
    testLogging {
      events("passed", "skipped", "failed")
    }
  }
}

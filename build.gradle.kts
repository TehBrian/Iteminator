plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("xyz.jpenilla.run-paper") version "2.2.3"
  id("net.kyori.indra.checkstyle") version "3.1.3"
  id("com.github.ben-manes.versions") version "0.51.0"
}

group = "dev.tehbrian"
version = "0.4.0"
description = "A modern item editing plugin."

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.thbn.me/releases/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

  implementation("broccolai.corn:corn-minecraft-paper:3.2.0")
  implementation("cloud.commandframework:cloud-minecraft-extras:1.8.4")
  implementation("cloud.commandframework:cloud-paper:1.8.4")
  implementation("com.google.inject:guice:7.0.0")
  implementation("dev.tehbrian:tehlib-paper:0.5.0")
  implementation("org.spongepowered:configurate-yaml:4.1.2")

  testImplementation("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(platform("org.junit:junit-bom:5.10.2"))
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
      "dev.tehbrian.restrictionhelper",
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
    minecraftVersion("1.20.4")
  }

  test {
    useJUnitPlatform()
    testLogging {
      events("passed", "skipped", "failed")
    }
  }
}

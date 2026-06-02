plugins {
	id("java")
	id("com.gradleup.shadow") version "9.4.2"
	id("xyz.jpenilla.run-paper") version "3.0.2"
	id("net.kyori.indra.checkstyle") version "4.0.0"
	id("com.github.ben-manes.versions") version "0.54.0"
}

group = "dev.tehbrian"
version = "0.5.0-SNAPSHOT"
description = "A modern item editing plugin."

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven("https://repo.tehbrian.dev/releases/")
	maven("https://repo.broccol.ai/snapshots/")
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
	implementation("love.broccolai.corn:corn-minecraft:4.1.0-SNAPSHOT")
	implementation("org.incendo:cloud-paper:2.0.0-beta.15")
	implementation("org.incendo:cloud-minecraft-extras:2.0.0-beta.15")
	implementation("org.bstats:bstats-bukkit:3.2.1")
	implementation("com.google.inject:guice:7.0.0")
	implementation("dev.tehbrian:agna-paper:1.0.1")
	implementation("org.spongepowered:configurate-hocon:4.2.0")

	testImplementation("io.papermc.paper:paper-api:26.1.2.build.+")
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation(platform("org.junit:junit-bom:6.1.0"))
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
	assemble {
		dependsOn(shadowJar)
	}

	processResources {
		filesMatching("plugin.yml") {
			expand(
					mapOf(
							"version" to project.version,
							"description" to project.description
					)
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
				"love.broccolai",
				"com.google.common",
				"com.google.errorprone",
				"com.google.inject",
				"com.google.j2objc",
				"com.google.thirdparty",
				"dev.tehbrian.agna",
				"io.leangen",
				"jakarta.inject",
				"javax.annotation",
				"net.kyori.option",
				"org.aopalliance",
				"org.bstats",
				"org.checkerframework",
				"org.incendo",
				"org.spongepowered",
		)
	}

	runServer {
		minecraftVersion("26.1.2")
	}

	test {
		useJUnitPlatform()
		testLogging {
			events("passed", "skipped", "failed")
		}
	}
}

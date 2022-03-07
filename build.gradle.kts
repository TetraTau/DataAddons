import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "io.xxr.dataaddon"
version = "b1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.lmax:disruptor:3.4.4")

    testImplementation("com.google.guava:guava:31.1-jre")
    testImplementation("com.lmax:disruptor:3.4.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    getByName<Test>("test") {
        useJUnitPlatform()
    }

    runPaper {
        disablePluginJarDetection()
    }

    runServer {
        runDirectory(file("$rootDir/run"))
        minecraftVersion("1.18.2")

        pluginJars.from(project(":paper-plugin").tasks.jar)
    }
}

project(":paper-plugin") {
    apply(plugin = "java")
    apply(plugin = "net.minecrell.plugin-yml.bukkit")

    group = "io.xxr.dataaddon"
    description = "Plugin for running and using"

    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }
        javadoc {
            options.encoding = Charsets.UTF_8.name()
        }
        processResources {
            filteringCharset = Charsets.UTF_8.name()
        }
    }

    bukkit {
        load = BukkitPluginDescription.PluginLoadOrder.STARTUP
        name = "DataAddons"
        version = rootProject.version.toString()
        main = "io.xxr.dataaddon.DataAddonPlugin"
        apiVersion = "1.18"
        authors = listOf("Denery")

        commands {
            register("dataaddons") {
                description = ""
                usage = "\"/dataaddons version\" for version"
            }
        }
    }
}

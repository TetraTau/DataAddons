plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" apply false
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

subprojects {
    apply(plugin = "java")
    apply(plugin = "net.minecrell.plugin-yml.bukkit")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
}

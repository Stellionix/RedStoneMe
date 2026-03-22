plugins {
    java
    id("com.gradleup.shadow") version "9.3.0"
}

group = "my.crylonz"
version = "3.0.1"

tasks.register("printVersion") {
    doLast {
        println(project.version)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigot-repo"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    testImplementation("org.xerial:sqlite-jdbc:3.50.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.85.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release = 8
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
    options.release = 17
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    archiveBaseName = "RedStoneMe"
    archiveClassifier = "plain"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName = "redstone-me"
    archiveClassifier = ""
}

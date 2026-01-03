plugins {
    kotlin("jvm") version "2.2.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.choco-solver:choco-solver:4.10.14")
}

tasks.test {
    useJUnitPlatform()
}
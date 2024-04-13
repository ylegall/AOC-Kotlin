
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.ygl"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.23"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.8.9")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

// can run a file like: ./gradlew runDay -Pyear=2022 -Pday=4
task("runDay", JavaExec::class) {
    val year = project.properties["year"] ?: "0"
    val day = project.properties["day"] ?: "0"
    mainClass.set("aoc$year.Day${day}Kt")
    classpath = sourceSets["main"].runtimeClasspath
}

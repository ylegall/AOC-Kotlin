
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.ygl"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.20"
    java
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.8.5")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

task<JavaExec>("solution1") {
    main = "aoc2018.Solution1Kt"
    classpath = sourceSets["main"].runtimeClasspath
}

task<JavaExec>("Day9") {
    main = "aoc2015.Day9Kt"
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
}
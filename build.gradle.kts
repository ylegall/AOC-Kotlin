import org.gradle.api.internal.file.pattern.PatternMatcherFactory.compile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.ygl"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.10"
    java
}
repositories {
    mavenCentral()
}
dependencies {
    compile(kotlin("stdlib-jdk8"))
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
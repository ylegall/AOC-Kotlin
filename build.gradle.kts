
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.ygl"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.6.0"
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

//task<JavaExec>("runDay") {
//    val year = project.properties["year"] ?: "0"
//    val day = project.properties["day"] ?: "0"
//    main = "aoc$year.Day${day}Kt"
//    classpath = sourceSets["main"].runtimeClasspath
//}

task("runDay", JavaExec::class) {
    val year = project.properties["year"] ?: "0"
    val day = project.properties["day"] ?: "0"
    main = "aoc$year.Day${day}Kt"
    classpath = sourceSets["main"].runtimeClasspath
}

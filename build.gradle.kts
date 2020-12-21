
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

task<JavaExec>("runDay") {
    val year = project.properties["year"]!!
    val day = project.properties["day"]!!
    main = "aoc$year.Day${day}Kt"
    classpath = sourceSets["main"].runtimeClasspath
}
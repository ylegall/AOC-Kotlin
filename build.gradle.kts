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
    main = "Solution1Kt"
    classpath = sourceSets["main"].runtimeClasspath
}

task<JavaExec>("solution2") {
    main = "Solution2Kt"
    classpath = sourceSets["main"].runtimeClasspath
}
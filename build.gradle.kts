

group = "org.ygl"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.0.21"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.25.3")
}


// can run a file like: ./gradlew runDay -Pyear=2022 -Pday=4
task("runDay", JavaExec::class) {
    val year = project.properties["year"] ?: "0"
    val day = project.properties["day"] ?: "0"
    mainClass.set("aoc$year.Day${day}Kt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.test {
    useJUnitPlatform()
}
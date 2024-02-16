plugins {
    kotlin("plugin.serialization") version "1.5.31"
    kotlin("jvm") version "1.9.20"
    application
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/commons-io/commons-io
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.mindrot:jbcrypt:0.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
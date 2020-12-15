import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.compose.compose

group = "com.draketalley"

plugins {
    kotlin("jvm") version "1.4.20"
    id("com.squareup.sqldelight") version "1.4.3"
    id("org.jetbrains.compose") version "0.2.0-build132"
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    jcenter()
}

val ktor_version = "1.4.3"
dependencies {
    implementation("com.squareup.sqldelight:sqlite-driver:1.4.3")
    implementation(compose.desktop.currentOs)
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}

sqldelight {
    database("KramerDb") {
        packageName = "com.draketalley.kramer"
        schemaOutputDirectory = file("src/main/sqldelight/databases")
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

// compile bytecode to java 8 (default is java 6)
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}


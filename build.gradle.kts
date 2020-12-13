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

dependencies {
    implementation("com.squareup.sqldelight:sqlite-driver:1.4.3")
    implementation(compose.desktop.currentOs)
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

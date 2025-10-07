plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "1.9.0"
    id("org.openjfx.javafxplugin") version "0.0.14"
    application
}


repositories { mavenCentral() }


dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    // JavaFX versi 24
    implementation("org.openjfx:javafx-controls:24.0.1")
    implementation("org.openjfx:javafx-fxml:24.0.1")
}

javafx {
    version = "24.0.1"
    modules = listOf("javafx.controls","javafx.fxml")
}


application {
    mainClass.set("com.example.client.MainClientAppKt")
}
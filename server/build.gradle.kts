import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.spring") version "1.9.0"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.openjfx.javafxplugin") version "0.0.14"
    application
}


group = "com.example"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_21


repositories { mavenCentral() }


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))


// JavaFX
    implementation("org.openjfx:javafx-controls:20")
    implementation("org.openjfx:javafx-fxml:20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}


javafx {
    version = "20"
    modules = listOf("javafx.controls", "javafx.fxml")
}


application {
    mainClass.set("com.example.server.MainKt")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}
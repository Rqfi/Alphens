buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle-api:8.2.2")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "7.2.1" apply false
}

tasks.register("clean",Delete::class) {
    delete (rootProject.buildDir)
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    kotlin("kapt") version "1.9.21"
    id("com.google.dagger.hilt.android") version "2.49" apply false
}

buildscript {
    extra.apply {
        set("version_hilt", "2.49")
        set("version_room", "2.6.1")
        set("version_navigation", "2.7.5")
        set("version_lifecycle", "2.6.2")
    }
}
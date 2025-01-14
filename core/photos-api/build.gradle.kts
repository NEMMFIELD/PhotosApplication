plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerializaiton)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
kotlin {
    jvmToolchain(21) // Настройка JVM для Kotlin
}
dependencies {
implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
    api(libs.logging.interceptor)
    api(libs.moshi.kotlin)
    api(libs.moshi)
    api(libs.moshi.converter)
    implementation(libs.retrofit.adapter)
}

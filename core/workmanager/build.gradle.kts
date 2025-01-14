plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.workmanager"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.runtime.ktx)
    // Dagger Hilt and KSP dependencies
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)  // KSP для Hilt
    ksp(libs.androidx.hilt.compiler)  // Для работы с Hilt и KSP
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // WorkManager dependencies
    implementation(libs.work.manager)
    implementation(libs.hilt.work)
    implementation(project(":core:photos-api"))
}

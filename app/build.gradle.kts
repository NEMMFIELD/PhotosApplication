plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.photosapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.photosapplication"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "ACCESS_KEY", "\"bYtWFCqztTPkUMBAl6Uoqye5_x6TxxnF-95CSr7OpzM\"")
            buildConfigField("String", "SECRET_KEY", "\"DTCZfgSvZttb9H8kzE2zSqJRgm_pzCRfkgcNH9CZexw\"")
        }
        release {
            buildConfigField("String", "ACCESS_KEY", "\"bYtWFCqztTPkUMBAl6Uoqye5_x6TxxnF-95CSr7OpzM\"")
            buildConfigField("String", "SECRET_KEY", "\"DTCZfgSvZttb9H8kzE2zSqJRgm_pzCRfkgcNH9CZexw\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        viewBinding = true
        android.buildFeatures.buildConfig = true
    }

}

dependencies {
    implementation(libs.androidx.hilt.common)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.play.services.phenotype)
    implementation(project(":core:utils"))
    implementation(libs.androidx.core.ktx)
    implementation (libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.fragment.kotlin)
    implementation(libs.logging.interceptor)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    implementation(libs.androidx.browser)

    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler) // Используем KSP для Hilt
    implementation(libs.hilt.work)

    // WorkManager
    implementation(libs.work.manager)

    // Project dependencies
    implementation(project(":features:photos-random"))
    implementation(project(":features:photos-details"))
    implementation(project(":features:photos-authorization"))
    implementation(project(":features:photos-search"))
    implementation(project(":core:photos-api"))
    implementation(project(":core:navigation"))
    implementation(project(":core:workmanager"))
    implementation(project(":core:utils"))
}

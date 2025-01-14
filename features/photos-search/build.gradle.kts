plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.photos.search"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    implementation(libs.fragment.kotlin)
    implementation(libs.recyclerview)
    implementation(libs.coil)
    implementation(libs.coil.okhttp)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // Dagger Hilt and KSP dependencies
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)  // KSP для Hilt
    ksp(libs.androidx.hilt.compiler)  // Для работы с Hilt и KSP

    implementation(project(":core:photos-api"))
    implementation(project(":core:state"))
    implementation(project(":core:navigation"))
    implementation(project(":features:photos-details"))
    implementation(project(":core:utils"))
}

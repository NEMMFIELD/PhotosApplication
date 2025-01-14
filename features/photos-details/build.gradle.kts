plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.example.photos_details"
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
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.fragment.kotlin)
    implementation(libs.recyclerview)
    implementation(libs.coil)
    implementation(libs.coil.okhttp)
    implementation(libs.androidx.browser)

    //dagger
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(project(":core:photos-api"))
    implementation(project(":core:state"))
    implementation(project(":core:utils"))

    testImplementation(libs.junit)
    testImplementation (libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.robolectric)
    testImplementation(kotlin("test"))
    testImplementation (libs.turbine)
    testImplementation (libs.kotlinx.coroutines.test)
}

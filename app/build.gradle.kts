plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.example.plantix"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.plantix.android"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))

    //navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    //dots indicator
    implementation(libs.dots.indicator)

    //chart
    implementation(libs.chart)

    //shimmer
    implementation(libs.shimmer)

    //swipe refresh layout
    implementation(libs.swipe.refresh.layout)

    // CameraX
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.camera.extensions)

    //activity
    implementation(libs.androidx.activity)

}

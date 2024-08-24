plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.example.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_NEWS_URL", "\"https://newsapi.org/\"")
            buildConfigField("String", "API_KEY", "\"885205dcfec64390a1bcde52d85c29e0\"")
            buildConfigField("String", "BASE_URL", "\"https://plantix-api-ypm2dd7h7q-et.a.run.app/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_NEWS_URL", "\"https://newsapi.org/\"")
            buildConfigField("String", "API_KEY", "\"885205dcfec64390a1bcde52d85c29e0\"")
            buildConfigField("String", "BASE_URL", "\"https://plantix-api-ypm2dd7h7q-et.a.run.app/\"")
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
        buildConfig = true
    }
}

dependencies {
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //kotlin coroutine
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    api(libs.livedata)

    //viewmodel
    implementation(libs.viewmodel)

    //datastore
    implementation(libs.datastore)
}
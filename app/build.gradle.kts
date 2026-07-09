plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ashraftraders"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.ashraftraders"
        minSdk = 26
        targetSdk = 36
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
    buildFeatures{
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    // Gson
    implementation (libs.gson)
    // OkHttp
    implementation (libs.logging.interceptor)

    // Glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)
    implementation (libs.room.ktx)
    implementation (libs.lifecycle.livedata.ktx)
    implementation (libs.lifecycle.viewmodel.ktx)

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
}
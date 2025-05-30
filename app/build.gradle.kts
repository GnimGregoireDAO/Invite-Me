plugins {

    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.android)

    id("org.jetbrains.kotlin.kapt") // Si tu utilises kapt

    // id("com.google.devtools.ksp") // Décommente si tu utilises KSP au lieu de kapt

}

android {

    namespace = "com.inviteme"

    compileSdk = 35

    defaultConfig {

        applicationId = "com.inviteme"

        minSdk = 24

        targetSdk = 35

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

    buildFeatures {

        viewBinding = true

        dataBinding = true

    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11

        targetCompatibility = JavaVersion.VERSION_11

    }

    kotlinOptions {

        jvmTarget = "11"

    }

}

dependencies {

    // ROOM

    implementation(libs.room.runtime)

    implementation(libs.room.ktx)

    kapt(libs.room.compiler) // ou ksp(libs.room.compiler) si tu utilises KSP

    // ANDROIDX

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)

    implementation(libs.material)

    implementation(libs.androidx.activity)

    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.recyclerview)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // NAVIGATION

    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.androidx.navigation.ui.ktx)

    // COROUTINES

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // TESTS

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)

}


// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    // ===================
    // add this line serially
    alias(libs.plugins.kotlin.android)  // builtInKotlin=false এর কারণে দরকার
    alias(libs.plugins.ksp)             // ksp সবসময় hilt এর আগে
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.nsoft.notificationhistory"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.nsoft.notificationhistory"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {

        // udpate java version to 17
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // add this lines
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.core:core-ktx:1.19.0")
    implementation("com.google.android.material:material:1.14.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)



    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)         // 'kapt' না, 'ksp' ব্যবহার করো

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)


}
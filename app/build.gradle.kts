plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)

//    alias(libs.plugins.ksp)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")



}

android {
    namespace = "com.example.financetracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.financetracker"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.google.play.services.auth)

    // dependency for pie chart
    implementation(libs.mpandroidchart)
    // dependency for bottom sheet
    implementation(libs.material.v1100)
    // dependency for gauge meter for the income to expenditure ratio
    implementation(libs.speedviewlib)

    // important dependencies for Room


    implementation(libs.androidx.room.runtime)

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp(libs.androidx.room.compiler)

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor(libs.androidx.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // optional - RxJava2 support for Room
    implementation(libs.androidx.room.rxjava2)

    // optional - RxJava3 support for Room
    implementation(libs.androidx.room.rxjava3)

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    // dependency for viewmodel scope for coroutine
    implementation (libs.androidx.lifecycle.viewmodel.ktx) // or latest



    implementation(libs.imagepicker)  // Image Picker

    implementation(libs.glide)  // Glide for Image Loading
    annotationProcessor(libs.glide.compiler)// ✅ Use kapt for Glide Compiler in Kotlin projects

    implementation(platform(libs.firebase.bom)) // ✅ Use Firebase BOM for compatibility
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore) {
        exclude(
            group = "com.google.firebase",
            module = "firebase-common"
        ) // ✅ Prevents duplicate firebase-common
    }
    implementation(libs.firebase.storage)
}

// ✅ Force Firebase dependencies to use the correct version
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.google.firebase") {
            when (requested.name) {
                "firebase-common" -> useVersion("20.4.1") // ✅ Forces firebase-common to a stable version
                "firebase-firestore" -> useVersion("24.10.3") // ✅ Ensures Firestore is compatible
            }
        }
    }
}

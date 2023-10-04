plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.uha.hassenforder.team"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.uha.hassenforder.team"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-opt-in=org.mylibrary.OptInAnnotation"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Kotlin components
    implementation("androidx.core:core-ktx:1.10.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0-alpha01")
    implementation("androidx.activity:activity-compose:1.7.2")

    // UI
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Misc ??
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")
    implementation("com.google.android.material:compose-theme-adapter-3:1.1.1")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.navigation:navigation-common-ktx:2.7.0")

    // Room components
    implementation( "androidx.room:room-ktx:2.5.2")
    implementation("com.google.android.material:material:1.9.0")
    implementation(project(mapOf("path" to ":android")))
    ksp("androidx.room:room-compiler:2.5.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    ksp("com.google.dagger:hilt-compiler:2.47")

    //Image
    implementation("io.coil-kt:coil-compose:2.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.dumchykov.socialnetworkdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dumchykov.socialnetworkdemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"http://178.63.9.114:7777/api/\"")
    }

    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
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
}

room {
    schemaDirectory("${rootProject.projectDir}/schemas")
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    // Preferences DataStore (SharedPreferences like APIs)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.glide)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    //Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}
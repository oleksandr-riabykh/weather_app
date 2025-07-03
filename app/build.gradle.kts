plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)               // <--- Apply KSP plugin
    alias(libs.plugins.hilt.gradle)
}

android {
    namespace = "com.life.totally.great"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.life.is.great"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            //todo: api keys might be stored securely in config file, or loaded form firebase remote config
            buildConfigField("String", "WEATHER_API_HOST", "\"https://api.openweathermap.org/\"")
            buildConfigField("String", "WEATHER_API_KEY", "\"d0d237645c1c1ab82724fc647d2d6d9d\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "WEATHER_API_HOST", "\"https://api.openweathermap.org/\"")
            buildConfigField("String", "WEATHER_API_KEY", "\"d0d237645c1c1ab82724fc647d2d6d9d\"")
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    packaging {
        resources { pickFirsts += listOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md") }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.accompanist.permissions)

    implementation(libs.datastore.preferences)
    implementation(libs.work.runtime.ktx)
    implementation(libs.kotlinx.coroutines.play.services)

    // Chart
    implementation(libs.charty)

    // Coil for Jetpack Compose
    implementation(libs.coil.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.play.services.location)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)

    // Hilt Navigation Compose
    implementation(libs.hilt.navigation.compose)

    // Retrofit & OkHttp Logging
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson) // For Gson serialization/deserialization
    implementation(libs.gson) // Gson library itself
    implementation(libs.okhttp.logging.interceptor)

    // TESTS
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(kotlin("test"))

    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android.testing)   // For Hilt instrumented tests (androidTest)
    kspAndroidTest(libs.hilt.android.compiler)

//    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.bundles.android.test)
    debugImplementation(libs.bundles.debug.test)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
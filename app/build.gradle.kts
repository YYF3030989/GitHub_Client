import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

val githubPropsFile = rootProject.file("github.properties")
val githubProps = Properties()
if (githubPropsFile.exists()) {
    githubProps.load(githubPropsFile.inputStream())
}

val clientId = githubProps.getProperty("CLIENT_ID") ?: ""
val clientSecret = githubProps.getProperty("CLIENT_SECRET") ?: ""

android {
    namespace = "com.example.githubclient"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.githubclient"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TODO: Read GITHUB_API_TOKEN from local.properties. Remove after GithubLogin implementation
//        val localProperties = Properties()
//        localProperties.load(rootProject.file("local.properties").inputStream())
//
//        buildConfigField("String", "GITHUB_API_TOKEN",  "\"${localProperties.getProperty("GITHUB_API_TOKEN")}\"")
        buildConfigField("String", "GITHUB_CLIENT_ID", "\"$clientId\"")
        buildConfigField("String", "GITHUB_CLIENT_SECRET", "\"$clientSecret\"")
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
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation(libs.retrofit)
    // JSON converter
    implementation(libs.converter.gson)
    // Coroutine support (for suspend functions)
    implementation(libs.kotlinx.coroutines.android)
    // OkHttp logging (optional but useful for debugging)
    implementation(libs.logging.interceptor)

    implementation(libs.coil.compose)

    implementation(libs.androidx.paging.runtime)
    // alternatively - without Android dependencies for tests
    testImplementation(libs.androidx.paging.common)
    // optional - Jetpack Compose integration
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.java.jwt)
    implementation (libs.androidx.browser) // For Chrome Custom Tabs

    implementation(libs.androidx.datastore.preferences.v100)
    implementation(libs.androidx.datastore.core)


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // (Optional) For Hilt ViewModel support
    implementation(libs.androidx.hilt.navigation.compose)
}
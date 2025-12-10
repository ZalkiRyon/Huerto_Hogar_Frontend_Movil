plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.huerto_hogar"
    compileSdk = 36

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/licenses/**"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }
    }
    defaultConfig {
        applicationId = "com.example.huerto_hogar"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.camera:camera-camera2:1.5.0")
    implementation("androidx.camera:camera-lifecycle:1.5.0")
    implementation("androidx.camera:camera-view:1.5.0")
    // Para la vista previa en compose (o usa AndroidView)
    implementation("androidx.camera:camera-compose:1.0.0-alpha02")
    // Para manejar los permisos fácilmente
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    // Dependencia para cargar la imagen después de ser capturada
    implementation("io.coil-kt:coil-compose:2.1.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")

    implementation("androidx.navigation:navigation-compose:2.8.0")
    
    // Retrofit para consumir APIs
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Gson para serialización JSON
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Coroutines para operaciones asíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Pruebas Unitarias
    // MockK
    testImplementation("io.mockk:mockk:1.13.8")
    // Si también haces pruebas instrumentadas (androidTest), añade esta línea también:
    androidTestImplementation("io.mockk:mockk-android:1.13.8")
    // --- Instrumented Tests (androidTest - UI Tests) ---

    // Dependencias base de AndroidX Test (Versiones actualizadas y compatibles)
    // Usa las versiones del libs.versions.toml si existen, si no, usa cableado:
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Usa una version consistente
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Usa una version consistente

    // Dependencias de Compose UI Test (usando el BOM para compatibilidad)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4) // Usa la versión gestionada por el BOM/libs

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
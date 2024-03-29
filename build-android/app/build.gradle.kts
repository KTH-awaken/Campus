plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.google.firebase.codelab.friendlychat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.google.firebase.codelab.friendlychat"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //från Campus
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
    //från Campus
    buildFeatures {
        compose = true
    }
    //från Campus
    composeOptions {
//        kotlinCompilerExtensionVersion = "1.4.3"//från Campus
        kotlinCompilerExtensionVersion = "1.5.7"//från devAndoid hemidian
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources.excludes += "META-INF/LICENSE"
        resources.excludes += "META-INF/LICENSE-FIREBASE.txt"
        resources.excludes += "META-INF/NOTICE"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    lint {
        disable += "NotificationPermission"
    }
}

dependencies {
    // For send notificatins using HTTP REQUSET
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // for push notifications from Firebase messaging
    implementation ("com.google.firebase:firebase-messaging")

    //To show profile picture with rememberImagePainter that takes imageUrl
    implementation("io.coil-kt:coil-compose:1.4.0") // todo Check for the latest version

    // GPS
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    //Main activity
    implementation("androidx.activity:activity-ktx:1.8.2")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.4.0")

    implementation("com.google.android.material:material:1.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.media:media:1.7.0")
    implementation("androidx.core:core-ktx:1.12.0")

    // Google
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firebase UI
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("androidx.compose.material3:material3:1.1.2")

    //för att få den att fungera kommer från build starts gradel
//    implementation(project(mapOf("path" to ":build-android:app")))


    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
}


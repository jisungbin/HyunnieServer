plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("name.remal.check-dependency-updates") version "1.0.211"
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Application.compileSdk)
    defaultConfig {
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        versionCode = Application.versionCode
        versionName = Application.versionName
        multiDexEnabled = true
        setProperty("archivesBaseName", "v$versionName ($versionCode)")
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isUseProguard = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/library_release.kotlin_module")
    }

    compileOptions {
        sourceCompatibility = Application.sourceCompat
        targetCompatibility = Application.targetCompat
    }

    kotlinOptions {
        jvmTarget = Application.jvmTarget
    }
}

dependencies {
    fun def(vararg strings: String) {
        for (string in strings) implementation(string)
    }

    def(
        Dependencies.Network.CommonsIo,
        Dependencies.Network.CommonsNet,

        Dependencies.Essential.AppCompat,
        Dependencies.Essential.Anko,
        Dependencies.Essential.Kotlin,

        Dependencies.Ktx.Core,
        Dependencies.Ktx.Fragment,

        Dependencies.Di.Hilt,

        Dependencies.Ui.SpotLight,
        Dependencies.Ui.TransformationLayout,
        Dependencies.Ui.ShapeOfYou,
        Dependencies.Ui.YoYo,
        Dependencies.Ui.Lottie,
        Dependencies.Ui.Licenser,
        Dependencies.Ui.Material,
        Dependencies.Ui.Glide,
        Dependencies.Ui.ConstraintLayout,

        Dependencies.Utils.YoyoHelper,
        Dependencies.Utils.AndroidUtils,
        Dependencies.Utils.CrashReporter
    )

    kapt(Dependencies.Di.HiltCompiler)
    kapt(Dependencies.Utils.GlideCompiler)
}
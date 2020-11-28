plugins {
    id("com.android.application")
    id("name.remal.check-dependency-updates") version "1.1.4"
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
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

    kotlinOptions.jvmTarget = Application.jvmTarget
    sourceSets.getByName("main").java.srcDirs("src/main/kotlin")

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/library_release.kotlin_module")
    }

    compileOptions {
        sourceCompatibility = Application.sourceCompat
        targetCompatibility = Application.targetCompat
    }

}

dependencies {
    "implementation"(platform(Dependencies.Firebase.Bom))

    fun def(vararg strings: String) {
        for (string in strings) implementation(string)
    }

    def(
        Dependencies.Network.CommonsIo,
        Dependencies.Network.CommonsNet,

        Dependencies.Essential.AppCompat,
        Dependencies.Essential.Anko,
        Dependencies.Essential.Kotlin,

        Dependencies.Ktx.NavigationUi,
        Dependencies.Ktx.NavigationFragment,
        Dependencies.Ktx.Core,
        Dependencies.Ktx.Fragment,
        Dependencies.Ktx.Config,

        Dependencies.Ui.Flexbox,
        Dependencies.Ui.SmoothBottomBar,
        Dependencies.Ui.Lottie,
        Dependencies.Ui.Material,
        Dependencies.Ui.Glide,
        Dependencies.Ui.ConstraintLayout,

        Dependencies.Util.AndroidUtils
    )

    kapt(Dependencies.Util.GlideCompiler)
}
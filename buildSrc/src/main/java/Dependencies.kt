import org.gradle.api.JavaVersion

object Application {
    const val minSdk = 23
    const val targetSdk = 30
    const val compileSdk = 30
    const val jvmTarget = "1.8"
    const val versionCode = 1
    const val versionName = "1.0.0"

    val targetCompat = JavaVersion.VERSION_1_8
    val sourceCompat = JavaVersion.VERSION_1_8
}

object Versions {
    object Firebase {
        const val Bom = "26.1.0"
    }

    object Network {
        const val CommonsNet = "3.7.1"
        const val CommonsIo = "2.8.0"
    }

    object Essential {
        const val AppCompat = "1.2.0"
        const val Anko = "0.10.8"
        const val Kotlin = "1.4.20"
        const val Gradle = "4.1.1"
        const val Google = "4.3.3"
    }

    object Ktx {
        const val Core = "1.3.2"
        const val Fragment = "2.3.0"
    }

    object Jetpack {
        const val Navigation = "2.3.1"
    }

    object Ui {
        const val ToggleButtonLayout = "1.3.0"
        const val Flexbox = "2.0.1"
        const val SmoothBottomBar = "1.7.6"
        const val Lottie = "3.5.0"
        const val Material = "1.2.0-alpha06"
        const val Glide = "4.11.0"
        const val ConstraintLayout = "2.0.4"
    }

    object Util {
        const val AndroidUtils = "4.1.5"
    }
}

object Dependencies {
    object Firebase {
        const val Bom = "com.google.firebase:firebase-bom:${Versions.Firebase.Bom}"
    }


    object Network {
        const val CommonsNet = "commons-net:commons-net:${Versions.Network.CommonsNet}"
        const val CommonsIo = "commons-io:commons-io:${Versions.Network.CommonsIo}"
    }

    object Essential {
        const val AppCompat = "androidx.appcompat:appcompat:${Versions.Essential.AppCompat}"
        const val Anko = "org.jetbrains.anko:anko:${Versions.Essential.Anko}"
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Essential.Kotlin}"
    }

    object Ktx {
        const val Config = "com.google.firebase:firebase-config-ktx"
        const val NavigationFragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Jetpack.Navigation}"
        const val NavigationUi =
            "androidx.navigation:navigation-ui-ktx:${Versions.Jetpack.Navigation}"
        const val Core = "androidx.core:core-ktx:${Versions.Ktx.Core}"
        const val Fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.Ktx.Fragment}"
    }

    object Ui {
        const val ToggleButtonLayout =
            "com.github.savvyapps:ToggleButtonLayout:${Versions.Ui.ToggleButtonLayout}"
        const val Flexbox = "com.google.android:flexbox:${Versions.Ui.Flexbox}"
        const val SmoothBottomBar =
            "com.github.ibrahimsn98:SmoothBottomBar:${Versions.Ui.SmoothBottomBar}"
        const val Lottie = "com.airbnb.android:lottie:${Versions.Ui.Lottie}"
        const val Material = "com.google.android.material:material:${Versions.Ui.Material}"
        const val Glide = "com.github.bumptech.glide:glide:${Versions.Ui.Glide}"
        const val ConstraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.Ui.ConstraintLayout}"
    }

    object Util {
        const val GlideCompiler = "com.github.bumptech.glide:compiler:${Versions.Ui.Glide}"
        const val AndroidUtils = "com.github.sungbin5304:SBT:${Versions.Util.AndroidUtils}"
    }
}
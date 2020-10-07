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
    object Network {
        const val CommonsNet = "3.7.1"
        const val CommonsIo = "2.8.0"
    }

    object Essential {
        const val AppCompat = "1.2.0"
        const val Anko = "0.10.8"
        const val Kotlin = "1.4.10"
        const val Gradle = "4.0.2"
    }

    object Ktx {
        const val Core = "1.3.2"
        const val Fragment = "2.3.0"
    }

    object Di {
        const val Hilt = "2.28-alpha"
    }

    object Jetpack {
        const val DataStore = "1.0.0-alpha01"
        const val Navigation = "2.3.0"
    }

    object Ui {
        const val SpotLight = "2.0.2"
        const val TransformationLayout = "1.0.6"
        const val ShapeOfView = "1.4.7"
        const val YoYo = "2.4@aar"
        const val Lottie = "3.4.4"
        const val Licenser = "2.0.0"
        const val Material = "1.2.0-alpha06"
        const val Glide = "4.11.0"
        const val ConstraintLayout = "2.0.2"
    }

    object Util {
        const val YoYoHelper = "2.4@aar"
        const val AndroidUtils = "4.0.3"
        const val CarshReporter = "1.1.0"
    }
}

object Dependencies {
    object Network {
        const val CommonsNet = "commons-net:commons-net:${Versions.Network.CommonsNet}"
        const val CommonsIo = "commons-io:commons-io:${Versions.Network.CommonsIo}"
    }

    object Jetpack {
        const val DataStore =
            "androidx.datastore:datastore-preferences:${Versions.Jetpack.DataStore}"
    }

    object Essential {
        const val AppCompat = "androidx.appcompat:appcompat:${Versions.Essential.AppCompat}"
        const val Anko = "org.jetbrains.anko:anko:${Versions.Essential.Anko}"
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Essential.Kotlin}"
    }

    object Ktx {
        const val NavigationFragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Jetpack.Navigation}"
        const val NavigationUi =
            "androidx.navigation:navigation-ui-ktx:${Versions.Jetpack.Navigation}"
        const val Core = "androidx.core:core-ktx:${Versions.Ktx.Core}"
        const val Fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.Ktx.Fragment}"
    }

    object Di {
        const val Hilt = "com.google.dagger:hilt-android:${Versions.Di.Hilt}"
        const val HiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.Di.Hilt}"
    }

    object Ui {
        const val SpotLight = "com.github.takusemba:spotlight:${Versions.Ui.SpotLight}"
        const val TransformationLayout =
            "com.github.skydoves:transformationlayout:${Versions.Ui.TransformationLayout}"
        const val ShapeOfYou = "com.github.florent37:shapeofview:${Versions.Ui.ShapeOfView}"
        const val YoYo = "com.daimajia.androidanimations:library:${Versions.Ui.YoYo}"
        const val Lottie = "com.airbnb.android:lottie:${Versions.Ui.Lottie}"
        const val Licenser = "com.github.marcoscgdev:Licenser:${Versions.Ui.Licenser}"
        const val Material = "com.google.android.material:material:${Versions.Ui.Material}"
        const val Glide = "com.github.bumptech.glide:glide:${Versions.Ui.Glide}"
        const val ConstraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.Ui.ConstraintLayout}"
    }

    object Util {
        const val GlideCompiler = "com.github.bumptech.glide:compiler:${Versions.Ui.Glide}"
        const val YoyoHelper = "com.daimajia.easing:library:${Versions.Util.YoYoHelper}"
        const val AndroidUtils = "com.github.sungbin5304:SBT:${Versions.Util.AndroidUtils}"
        const val CrashReporter =
            "com.balsikandar.android:crashreporter:${Versions.Util.CarshReporter}"
    }
}
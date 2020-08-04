import org.gradle.api.JavaVersion

object Application {
    const val minSdk = 23
    const val targetSdk = 29
    const val compileSdk = 29
    const val versionCode = 10
    const val jvmTarget = "1.8"
    const val versionName = "1.0.0"

    val targetCompat = JavaVersion.VERSION_1_8
    val sourceCompat = JavaVersion.VERSION_1_8
}

object Versions {
    const val Anko = "0.10.8"
    const val Legacy = "1.0.0"
    const val Kotlin = "1.3.50"
    const val CoreKtx = "1.3.0"
    const val AppCompat = "1.0.2"
    const val FragmentKtx = "1.2.5"
    const val LifeCycleViewModel = "2.2.0"
    const val LifeCycleExtensions = "2.2.0"

    const val Hilt = "2.28-alpha"
    const val HiltAndroidX = "1.0.0-alpha01"

    const val Dagger = "2.28"

    const val Glide = "4.11.0"
    const val CardView = "1.0.0"
    const val ConstraintLayout = "1.1.3"

    const val AndroidUtils = "3.1.5"
    const val CrashReporter = "1.1.0"

    const val AnimatorLottie = "3.4.0"
    const val AnimatorTool = "2.1@aar"
    const val AnimatorYOYO = "2.3@aar"

    const val CommonIo = "2.7"
    const val CommonNet = "3.6"
}

object Dependencies {
    object Network {
        const val CommonIo = "commons-io:commons-io:${Versions.CommonIo}"
        const val CommonNet = "commons-net:commons-net:${Versions.CommonNet}"
    }

    object Essential {
        const val Anko = "org.jetbrains.anko:anko:${Versions.Anko}"
        const val CoreKtx = "androidx.core:core-ktx:${Versions.CoreKtx}"
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin}"
        const val AppCompat = "androidx.appcompat:appcompat:${Versions.AppCompat}"
        const val Legacy = "androidx.legacy:legacy-support-core-ui:${Versions.Legacy}"
        const val FragmentKtx = "androidx.fragment:fragment-ktx:${Versions.FragmentKtx}"
        const val LifeCycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.LifeCycleExtensions}"
        const val LifeCycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LifeCycleViewModel}"
    }

    object Di {
        const val Dagger = "com.google.dagger:dagger:${Versions.Dagger}"
        const val Hilt = "com.google.dagger:hilt-android:${Versions.Hilt}"
        const val HiltCommon = "androidx.hilt:hilt-common:${Versions.HiltAndroidX}"
        const val DaggerCompiler = "com.google.dagger:dagger-compiler:${Versions.Dagger}"
        const val HiltAndroidXCompiler = "androidx.hilt:hilt-compiler:${Versions.HiltAndroidX}"
        const val HiltGoogleCompiler = "com.google.dagger:hilt-android-compiler:${Versions.Hilt}"
        const val HiltLifeCycle = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.HiltAndroidX}"
    }

    object Ui {
        const val Glide = "com.github.bumptech.glide:glide:${Versions.Glide}"
        const val CardView = "androidx.cardview:cardview:${Versions.CardView}"
        const val GlideCompiler = "com.github.bumptech.glide:compiler:${Versions.Glide}"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.ConstraintLayout}"
    }

    object Utils {
        const val AndroidUtils = "com.github.sungbin5304:AndroidUtils:${Versions.AndroidUtils}"
        const val CrashReporter = "com.balsikandar.android:crashreporter:${Versions.CrashReporter}"
    }

    object Animator {
        const val Tool = "com.daimajia.easing:library:${Versions.AnimatorTool}"
        const val Lottie = "com.airbnb.android:lottie:${Versions.AnimatorLottie}"
        const val Yoyo = "com.daimajia.androidanimations:library:${Versions.AnimatorYOYO}"
    }
}
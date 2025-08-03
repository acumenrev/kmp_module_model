import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary) // Optional
}
kotlin {
    val xcf = XCFramework("KMPShared")


    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Configure iOS framework for SPM
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
    iosTargets.forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KMPShared"
            isStatic = true
            xcf.add(this)

            // Export dependencies to make them available in iOS
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)

            // For HTTP networking (optional) - using bundle
            api(libs.bundles.ktor.common)



        }
        commonTest.dependencies {
            // testing target
            implementation(libs.kotlin.test)
        }

        iosMain.dependencies {
            // put iOS dependencies here
            implementation(libs.ktor.client.darwin)
        }

    }
}

android {
    namespace = "org.example.project.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

tasks.register("createXCFramework") {
    group = "build"
    description = "Create XCFramework for iOS"

    dependsOn(
        "linkDebugFrameworkIosArm64",
        "linkDebugFrameworkIosX64",
        "linkDebugFrameworkIosSimulatorArm64"
    )

    doLast {
        val buildDir = project.layout.buildDirectory.dir("XCFrameworks").get().asFile
        buildDir.mkdirs()

        val xcframeworkPath = File(buildDir, "Shared.xcframework")
        if (xcframeworkPath.exists()) {
            xcframeworkPath.deleteRecursively()
        }

        exec {
            commandLine(
                "xcodebuild", "-create-xcframework",
                "-framework", "${project.layout.buildDirectory.get()}/bin/iosArm64/debugFramework/Shared.framework",
                "-framework", "${project.layout.buildDirectory.get()}/bin/iosX64/debugFramework/Shared.framework",
                "-framework", "${project.layout.buildDirectory.get()}/bin/iosSimulatorArm64/debugFramework/Shared.framework",
                "-output", xcframeworkPath.absolutePath
            )
        }

        println("✅ XCFramework created at: ${xcframeworkPath.absolutePath}")
    }
}

tasks.register("createReleaseXCFramework") {
    group = "build"
    description = "Create Release XCFramework for iOS"

    dependsOn(
        "linkReleaseFrameworkIosArm64",
        "linkReleaseFrameworkIosX64",
        "linkReleaseFrameworkIosSimulatorArm64"
    )

    doLast {
        val buildDir = project.layout.buildDirectory.dir("XCFrameworks").get().asFile
        buildDir.mkdirs()

        val xcframeworkPath = File(buildDir, "SharedModule.xcframework")
        if (xcframeworkPath.exists()) {
            xcframeworkPath.deleteRecursively()
        }

        exec {
            commandLine(
                "xcodebuild", "-create-xcframework",
                "-framework", "${project.layout.buildDirectory.get()}/bin/iosArm64/releaseFramework/SharedModule.framework",
                "-framework", "${project.layout.buildDirectory.get()}/bin/iosX64/releaseFramework/SharedModule.framework",
                "-framework", "${project.layout.buildDirectory.get()}/bin/iosSimulatorArm64/releaseFramework/SharedModule.framework",
                "-output", xcframeworkPath.absolutePath
            )
        }

        println("✅ Release XCFramework created at: ${xcframeworkPath.absolutePath}")
    }
}

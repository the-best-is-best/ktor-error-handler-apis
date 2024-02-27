import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)

    id("maven-publish")
    id("signing")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())


    val xcf = XCFramework()
    iosTargets.forEach {
        it.binaries.framework {
            baseName = "ErrorHandleApi"
            xcf.add(this)
        }
    }
    cocoapods {
        summary = "Handle Error Apis with Failure class"
        homepage = "https://github.com/the-best-is-best/KtorHandleErrorApiIos"
        source = "{:git=> 'https://github.com/the-best-is-best/KtorHandleErrorApiIos.git' }"
        version = "1.0.0"

        ios.deploymentTarget = "14.0"
        framework {
            baseName = "ErrorHandleApi"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


publishing {

    publications.withType<MavenPublication> {


        groupId = "io.github.the-best-is-best"
        artifactId = "ktor-error-handler-apis"
        version = "1.0.0-1-SNAPSHOT"
        //artifact(javadocJar)
        pom {
            name.set("Ktor Error Handler Apis")
            description.set("Handle Error Apis with Failure class")
            url.set("https://github.com/the-best-is-best/ktor-error-handler-apis")
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://opensource.org/licenses/Apache-2.0")
                }
            }
            issueManagement {
                system.set("Github")
                url.set("https://github.com/the-best-is-best/ktor-error-handler-apis/issues")
            }
            scm {
                connection.set("https://github.com/the-best-is-best/ktor-error-handler-apis.git")
                url.set("https://github.com/the-best-is-best/ktor-error-handler-apis")
            }
            developers {
                developer {
                    id.set("MichelleRaouf")
                    name.set("Michelle Raouf")
                    email.set("eng.michelle.raouf@gmail.com")
                }
            }
        }
    }


    repositories {
        maven {
            name = "OSSRH-snapshots"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials {

            }
        }
//            maven {
//                name = "OSSRH-snapshots"
//                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//                credentials {
//
//                }
//            }
//            maven {
//                name = "LocalMaven"
//                url = uri("$buildDir/maven")
//                   }
//                maven {
//                    name = "GitHubPackages"
//                    url = uri("https://maven.pkg.github.com/the-best-is-best/ComposeQuill")
//                    credentials {
//
//                    }
//                }
        //    }


    }

}


signing {
    useGpgCmd()
    sign(publishing.publications)
}

android {
    namespace = "io.tbib.ktorerrorhandleapis"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
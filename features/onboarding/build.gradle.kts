plugins {
    id("io.element.android-compose")
}

android {
    namespace = "io.element.android.x.features.onboarding"
}

dependencies {
    implementation(project(":libraries:core"))
    implementation(project(":libraries:elementresources"))
    implementation(project(":libraries:designsystem"))
    implementation(libs.mavericks.compose)
    implementation(libs.timber)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pagerindicator)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
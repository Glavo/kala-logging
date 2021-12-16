plugins {
    application
}

application {
    mainClass.set("org.glavo.kala.logging.benchmark.Main")
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

dependencies {
    implementation(rootProject)

    implementation("org.openjdk.jmh:jmh-core:1.32")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.32")
}
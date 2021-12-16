plugins {
    java
}

allprojects {
    apply {
        plugin("java")
    }

    group = "org.glavo.kala"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

}

val java9SourceDir = "src/main/java9"
val java9ClassesDir = file("$buildDir/classes/java9")

val compileJava9 = tasks.register<JavaExec>("compileJava9") {
    doFirst {
        java9ClassesDir.mkdirs()
    }

    dependsOn(tasks.compileJava)

    mainClass.set("com.sun.tools.javac.Main")
    args = listOf(
        "--release", "9",
        "-d", java9ClassesDir.path,
        "--source-path", files(sourceSets.main.get().allSource.srcDirs, java9SourceDir).asPath
    ) + fileTree(java9SourceDir).toList().map { it.path }
}


tasks.jar {
    dependsOn(compileJava9)

    manifest.attributes(
        "Multi-Release" to "true"
    )

    into("META-INF/versions/9") {
        from(java9ClassesDir)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
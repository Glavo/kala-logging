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

allprojects.filterNot { it == project(":benchmark") }.forEach { project ->
    val java9SourceDir = project.file("src/main/java9")
    val java9ClassesDir = project.file("${project.buildDir}/classes/java9")

    val compileJava9 = project.tasks.register<JavaExec>("compileJava9") {
        doFirst {
            java9ClassesDir.mkdirs()
        }

        dependsOn(project.tasks.compileJava)

        mainClass.set("com.sun.tools.javac.Main")
        args = listOf(
            "--release", "9",
            "-d", java9ClassesDir.path,
            "--module-path", project.configurations.runtimeClasspath.get().asPath,
            "--source-path", files(project.sourceSets.main.get().allSource.srcDirs, java9SourceDir).asPath
        ) + fileTree(java9SourceDir).toList().map { it.path }
    }


    project.tasks.jar {
        dependsOn(compileJava9)

        manifest.attributes(
            "Multi-Release" to "true"
        )

        into("META-INF/versions/9") {
            from(java9ClassesDir)
        }
    }

    project.tasks.compileJava {
        options.release.set(8)
    }

    project.tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }


    project.tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }
}

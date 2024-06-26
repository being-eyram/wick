plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.sunniercherries"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
    implementation("com.squareup.okio:okio:3.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.sunniercherries.MainKt")
}

tasks.jar {
    this.manifest {
        attributes(mapOf("Main-Class" to "com.sunniercherries.MainKt"))
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)

    from({
        configurations.runtimeClasspath.get()
            .filter {
                it.name.endsWith("jar")
            }
            .map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    id("io.ktor.plugin") version "2.1.3"
    id("io.ebean") version "13.10.0"
}

group = "lu.gbraun.geo_exporter"
version = "0.1"
application {
    mainClass.set("lu.gbraun.geo_exporter.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
buildscript {
    repositories {
        mavenCentral()
    }
}
repositories {
    mavenCentral()
}
ebean {
    debugLevel = 1
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
dependencies {
    implementation("io.ktor:ktor-server-forwarded-header-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.1.3")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("org.postgresql:postgresql:42.5.0")
    implementation("net.postgis:postgis-jdbc:2.5.1")
    implementation("net.postgis:postgis-geometry:2.5.1")

    implementation("de.grundid.opendatalab:geojson-jackson:1.14")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")

    implementation("io.ebean:persistence-api:3.0")
    implementation("io.ebean:ebean:13.10.0")
    implementation("io.ebean:ebean-postgis:13.10.0")
    kapt("io.ebean:querybean-generator:13.10.0")

    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
}
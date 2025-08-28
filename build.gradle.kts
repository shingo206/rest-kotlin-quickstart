plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("kapt") version "2.2.10"
    kotlin("plugin.allopen") version "2.2.10"
    kotlin("plugin.serialization") version "2.2.10"
    id("io.quarkus")
    id("org.jetbrains.kotlin.plugin.jpa") version "2.2.10"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val mapstructVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-mutiny")
    implementation("io.quarkus:quarkus-vertx")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-logging-json")
    // Kotlin
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Database
    implementation("io.quarkus:quarkus-reactive-pg-client")
    implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
    implementation("io.quarkus:quarkus-hibernate-validator")
    // OpenAPI
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-swagger-ui")
    // OpenTelemetry
    implementation("io.quarkus:quarkus-smallrye-metrics")
    implementation("io.quarkus:quarkus-opentelemetry")
    // MapStruct
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    // Test
    testImplementation(kotlin("test"))
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit5-mockito")
    testImplementation("io.quarkus:quarkus-test-vertx")
    testImplementation("io.quarkus:quarkus-test-hibernate-reactive-panache")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

group = "org.acme"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        javaParameters = true
    }
}
// MapStruct compile option
kapt {
    correctErrorTypes = true // Helps with generated code referencing unknown types
    keepJavacAnnotationProcessors = true // Ensures annotation processors are retained
    useBuildCache = true // Speeds up builds by caching kapt outputs

    arguments {
        arg("mapstruct.suppressGeneratorTimestamp", "true")
        arg("mapstruct.suppressGeneratorVersionInfoComment", "true")
        arg("mapstruct.verbose", "true")
        arg("mapstruct.defaultComponentModel", "cdi") // Optional: enforce CDI globally
        arg("mapstruct.defaultInjectionStrategy", "constructor") // Optional: use constructor injection
    }
}


tasks.test {
    jvmArgs("-javaagent:${classpath.find { it.name.contains("byte-buddy-agent") }?.absolutePath}")
}

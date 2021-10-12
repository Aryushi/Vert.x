import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("io.spring.dependency-management") version "1.0.1.RELEASE"
  id("com.google.cloud.tools.jib") version "2.7.1"
}

group = "com.danielprinz.udemy"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "3.9.8"
val junitJupiterVersion = "5.7.2"
val jacksonVersion = "2.11.4"

val mainVerticleName = "com.vertx.example.vertx_starter.MainVerticle"
val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"
val launcherClassName = "io.vertx.core.Launcher"

dependencyManagement {
  imports {
    mavenBom("org.apache.logging.log4j:log4j-bom:2.14.1")
  }
}
application {
  mainClass.set(launcherClassName)
}
dependencies {
  implementation("io.vertx:vertx-core:$vertxVersion")
  implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
  implementation("org.apache.logging.log4j:log4j-api")
  implementation("org.apache.logging.log4j:log4j-core")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl")
  implementation("org.slf4j:slf4j-api:1.7.31")
  testImplementation("io.vertx:vertx-junit5:$vertxVersion")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

jib {
  from {
    image = "amazoncorretto:11"
  }
  to {
    image = "example/jib/vertx-starter"
  }
  container {
    mainClass = "io.vertx.core.Launcher"
    args = listOf("run", mainVerticleName)
    ports = listOf("8888")
  }
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles {
    include("META-INF/services/io.vertx.core.spi.VerticleFactory")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

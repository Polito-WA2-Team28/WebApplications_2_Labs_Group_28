import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.google.cloud.tools.jib") version "3.1.4"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
}

group = "com.final_project"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.hibernate.validator:hibernate-validator")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")

	implementation("javax.json:javax.json-api:1.1.4")
	implementation("org.glassfish:javax.json:1.1.4")
	implementation("org.keycloak:keycloak-spring-boot-starter:21.1.1")
	implementation("org.keycloak:keycloak-admin-client:21.1.1")


	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("org.testcontainers:junit-jupiter:1.16.3")
	testImplementation("org.testcontainers:postgresql:1.17.6")

	testImplementation("com.github.dasniko:testcontainers-keycloak:2.5.0")


	// using new @Observed on class and enaabled @ObservedAspect
	implementation("org.springframework.boot:spring-boot-starter-aop")
	// enabled endpoint and expose metrics
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
	// handleing lifecycle of a span
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	// send span and trace data
	// endpoint is default to "http://locahost:9411/api/v2/spans" by actuator
	// we could setting by management.zipkin.tracing.endpoint
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
	// send logs by log Appender through URL
	implementation("com.github.loki4j:loki-logback-appender:1.4.0-rc2")

	// using new @Observed on class and enaabled @ObservedAspect
	implementation("org.springframework.boot:spring-boot-starter-aop")
	// enabled endpoint and expose metrics
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
	// handleing lifecycle of a span
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	// send span and trace data
	// endpoint is default to "http://locahost:9411/api/v2/spans" by actuator
	// we could set by management.zipkin.tracing.endpoint
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
	// send logs by log Appender through URL
	implementation("com.github.loki4j:loki-logback-appender:1.4.0-rc2")
	//logging
	implementation("org.slf4j:slf4j-api:2.0.7")
	implementation("ch.qos.logback:logback-classic")
}


dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:1.16.3")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib {
	container {
		jvmFlags = listOf("-Xms512m", "-Xdebug")
		args = listOf()
		ports = listOf("8081/tcp")
		workingDirectory = "/ticketing-service"
	}
}

jib.from.image = "amazoncorretto:17-alpine"
jib.to.image = "runcor3/ticketing-service:final-project"

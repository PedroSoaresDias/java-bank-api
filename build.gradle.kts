plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.bank"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.9")
	implementation("com.auth0:java-jwt:4.5.0")
	implementation("io.github.cdimascio:dotenv-java:2.2.3")
	implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql") // JDBC driver para Flyway
    implementation("org.postgresql:r2dbc-postgresql")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
    	exclude(group = "org.springframework.boot", module = "spring-boot-starter-web")
    	exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.springframework.security:spring-security-test") {
		exclude(group = "org.springframework.security", module = "spring-boot-starter-security")
	}
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    layered {}
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
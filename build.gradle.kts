plugins {
	java
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.1.0"
	id("idea")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework:spring-aspects")
	implementation("io.opentelemetry:opentelemetry-api:1.18.0")
	implementation("io.opentelemetry:opentelemetry-sdk:1.18.0")
//    implementation "io.opentelemetry:opentelemetry-sdk-common:1.18.0"
	implementation( "io.opentelemetry:opentelemetry-exporter-otlp:1.18.0")
//    implementation "io.opentelemetry:opentelemetry-exporter-otlp-trace:1.18.0"
	implementation( "io.grpc:grpc-okhttp:1.42.1")


//	implementation("io.micrometer:micrometer-tracing-bridge-brave")
//	implementation("io.micrometer:micrometer-registry-wavefront")
//	runtimeOnly("io.micrometer:micrometer-registry-wavefront")
//	runtimeOnly("io.micrometer:micrometer-tracing-reporter-wavefront")
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("org.projectlombok:lombok:1.18.20")




	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}


java {
	sourceCompatibility = JavaVersion.VERSION_17
	this.targetCompatibility = JavaVersion.VERSION_17

	if (JavaVersion.current() != this.targetCompatibility) {
		logger.warn("The build is using Java ${JavaVersion.current()} to build a Java ${this.targetCompatibility} compatible archive.")
		logger.warn("See the project README for instructions on changing the target Java version.")
	}
}
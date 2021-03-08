plugins {
    java
    application
}

group = "com.assignment"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClassName = "com.assignment.Application"
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")

    implementation("org.apache.logging.log4j:log4j-api:2.14.0")
    implementation("org.apache.logging.log4j:log4j-core:2.14.0")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.apache.commons:commons-lang3:3.0")

    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

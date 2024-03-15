import org.gradle.internal.UncheckedException
import org.wisepersist.gradle.plugins.gwt.GwtSuperDev
import kotlin.concurrent.thread

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.wisepersist.gwt") version "1.1.19"
    id("war")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.gwtproject:gwt-user:2.11.0")
    compileOnly("org.gwtproject:gwt-dev:2.11.0")
    implementation("org.jboss.elemento:elemento-core:1.4.0")
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.ktor:ktor-server-core:2.3.9")
    testImplementation("io.ktor:ktor-server-netty:2.3.9")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.18.1")
    testImplementation("org.slf4j:slf4j-simple:2.0.12")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()!!
tasks {
    gwt {
        gwt.modules = listOf("net.sayaya.blah.Index")
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
        sourceLevel = "auto"
    }
    compileGwt {
        extraJvmArgs = listOf("-javaagent:${lombok}=ECJ")
    }
    gwtDev {
        extraJvmArgs = listOf("-javaagent:${lombok}=ECJ")
        port = 8080
        codeServerPort = 8081
        war = file("src/main/resources/static")
    }
}

tasks {
    register<GwtSuperDev>("gwtDevTest") {
        modules = listOf("net.sayaya.blah.Test", "net.sayaya.blah.Index")
        launcherDir = file("build/resources/test/static")
        extraJvmArgs = listOf("-javaagent:${lombok}=ECJ")
        port = 8081
        src += files(File("src/test/java"))
    }
    val gwtTask = thread(start = false) { try {
        withType(GwtSuperDev::class.java).named("gwtDevTest").get().exec()
    } catch(ignore: UncheckedException) { } }
    test {
        useJUnitPlatform()
        doFirst { gwtTask.start() }
        finalizedBy("CloseGwtCodeServer")
    }
    register("CloseGwtCodeServer") {
        doLast {
            gwtTask.interrupt()
        }
    }
}
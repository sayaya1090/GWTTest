package net.sayaya

import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.UncheckedException
import org.wisepersist.gradle.plugins.gwt.GwtSuperDev
import kotlin.concurrent.thread

class GwtPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("org.wisepersist.gwt")
        val webserverTask = thread(start = false) { try {
            println("CCC")
            embeddedServer(Netty, 8080) { routing { staticResources("/", "static") } }.start(wait = true)
        } catch(ignore: UncheckedException) { } }
        project.tasks.register("openWebServer") {
            doFirst { webserverTask.start() }
            /*val url = URI.create("http://localhost:8080")
            val conn = url.toURL().openConnection()
            conn.connect()
            */
        }
        project.tasks.register("closeWebServer") {
            webserverTask.interrupt()
        }
        val gwtCodeServer = project.tasks.register("gwtTest", GwtSuperDev::class.java) {
            group = "gwt"
            dependsOn("processResources", "openWebServer")
            finalizedBy("closeWebServer")
        }
        val gwtCodeServerThread = thread(start = false) { try {
            gwtCodeServer.get().exec()
        } catch(ignore: UncheckedException) { } }

        project.tasks.register("closeGwtCodeServer") {
            gwtCodeServerThread.interrupt()
        }
        project.tasks.withType(Test::class.java).configureEach {
            useJUnitPlatform()
            dependsOn("processResources", "openWebServer")
            doFirst { gwtCodeServerThread.start() }
            finalizedBy("closeWebServer", "closeGwtCodeServer")
        }
    }
}
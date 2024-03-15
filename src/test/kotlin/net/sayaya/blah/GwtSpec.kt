package net.sayaya.blah

import io.kotest.assertions.nondeterministic.until
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import kotlin.time.Duration.Companion.seconds

abstract class GwtSpec (body: BehaviorSpec.() -> Unit = {}) : BehaviorSpec(body) {
    companion object {
        fun BehaviorSpec.Connect(name: String = "Connection", url: String, port:Int=8080,
                                 test: suspend BehaviorSpecGivenContainerScope.(ChromeDriver) -> Unit) {
            val document = ChromeDriver(ChromeOptions().addArguments("--headless"))
            lateinit var server: ApplicationEngine
            beforeSpec {
                until(60.seconds) {        // Wait until codeserver is ready
                    document.get("http://127.0.0.1:8081")
                    document.findElement(By.tagName("body")).text.startsWith("GWT Code Server")
                }
                server = embeddedServer(Netty, port) {
                    routing {
                        staticResources("/", "static")
                    }
                }.start(wait = false)
            }
            afterSpec {
                server.stop(0, 0)
            }
            Given(name) {
                document.get("http://127.0.0.1:$port/$url")
                until(120.seconds) {        // Wait until compile is done
                    document.findElement(By.tagName("body"))!=null &&
                    document.findElement(By.tagName("body")).text.startsWith("Compiling").not() &&
                    document.findElement(By.tagName("body")).text.isNotBlank()
                }
                test(document)
                document.quit()
            }
        }
    }
}
package net.sayaya.blah

import io.kotest.assertions.nondeterministic.until
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

abstract class GwtSpec (body: BehaviorSpec.() -> Unit = {}) : BehaviorSpec(body) {
    companion object {
        private val port = Random.nextInt(10000, 20000)
        init {
            embeddedServer(Netty, port) { routing { staticResources("/", "static") } }.start(wait = false)
        }

        private suspend fun waitUntil(until: kotlin.time.Duration, delay: kotlin.time.Duration = 0.5.seconds, predicate: () -> Boolean): Unit = until(until) {
            val result = try { predicate() } catch (exception: Exception) { false }
            if(result.not()) delay(delay.inWholeMilliseconds)
            result
        }

        fun BehaviorSpec.Connect(name: String = "Connection", url: String, test: suspend BehaviorSpecGivenContainerScope.(ChromeDriver) -> Unit) {
            Given(name) {
                val document = ChromeDriver(ChromeOptions().addArguments("--headless"))
                waitUntil(300.seconds) { document.get("http://127.0.0.1:8081/"); true }  // Wait until the GWT application is ready
                document.get("http://127.0.0.1:$port/$url")
                waitUntil(300.seconds) {                                                            // Wait until the GWT application is ready
                    var body: WebElement? = null
                    try { body = document.findElement(By.tagName("body")) } catch(ignore: NoSuchElementException) { }
                    body!=null && body.text.startsWith("Compiling").not() && body.text.isNotBlank()
                }
                test(document)
                document.quit()
            }
        }
    }
}
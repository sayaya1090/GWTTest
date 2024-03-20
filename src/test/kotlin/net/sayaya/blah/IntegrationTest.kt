package net.sayaya.blah

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import net.sayaya.gwt.test.GwtSpec
import org.openqa.selenium.By

class IntegrationTest : GwtSpec({
    Connect(url="Index.html") {document ->
        val body = document.findElement(By.tagName("body"))
        then("The body should contain 'Hello, World! This is Index!'") {
            body.text shouldStartWith "Hello, World! This is Index!"
        }
        val button = document.findElement(By.id("clickme"))
        then("The button should be displayed") {
            button.isDisplayed shouldBe true
        }
        document.quit()
    }
})
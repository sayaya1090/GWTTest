package net.sayaya.blah

import io.kotest.matchers.shouldBe
import net.sayaya.gwt.test.GwtSpec
import org.openqa.selenium.By

class ComponentTest :GwtSpec({
    Connect(url="Test.html") {document ->
        val button = document.findElement(By.id("clickme"))
        `when`("Click the button") {
            button.click()
            then("The button text should change") {
                button.text shouldBe "You clicked me!"
            }
        }
        document.quit()
    }
})
package net.sayaya.blah.client.component;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import lombok.experimental.Delegate;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;
import org.jboss.elemento.IsElement;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.button;

public class ComponentBlah implements IsElement<HTMLElement> {
    @Delegate private final HTMLContainerBuilder<HTMLDivElement> elem = div();
    public ComponentBlah() {
        var button = button().id("clickme").add("Click me!");
        button.on(EventType.click, evt->button.textContent("You clicked me!"));
        elem.add(button);
    }
}

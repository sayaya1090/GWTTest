package net.sayaya.blah.client;

import com.google.gwt.core.client.EntryPoint;
import net.sayaya.blah.client.component.ComponentBlah;

import static org.jboss.elemento.Elements.body;
import static org.jboss.elemento.Elements.button;

public class Test implements EntryPoint {
    @Override
    public void onModuleLoad() {
        body().add("Hello, World! This is Test!").add(new ComponentBlah());
    }
}

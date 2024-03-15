package net.sayaya.blah.client;

import com.google.gwt.core.client.EntryPoint;
import net.sayaya.blah.client.component.ComponentBlah;

import static org.jboss.elemento.Elements.body;

public class Index implements EntryPoint {
    @Override
    public void onModuleLoad() {
        body().add("Hello, World! This is Index!").add(new ComponentBlah());;
    }
}

package fi.valonia.pyorallatoihin.components;

import com.vaadin.ui.CssLayout;

public class Spacer extends CssLayout {
    private static final long serialVersionUID = 4849611007723596003L;

    public Spacer(String width, String height) {
        setWidth(width);
        setHeight(height);
        addStyleName("spacer");
    }
}

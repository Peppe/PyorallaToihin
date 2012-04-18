package fi.valonia.pyorallatoihin.views;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;

public class Header extends CssLayout {
    private static final long serialVersionUID = -3762111526374739947L;
    private final Button fin;
    private final Button swe;
    private final Button en;

    ClickListener clickListener = new ClickListener() {
        private static final long serialVersionUID = 1272415599058064909L;

        @Override
        public void buttonClick(ClickEvent event) {
            String language = null;
            if (event.getButton() == swe) {
                language = "SE";
            } else if (event.getButton() == en) {
                language = "EN";
            }
            ((PyorallaToihinRoot) getRoot()).setLanguage(language);
        }
    };

    public Header(PyorallaToihinRoot root) {
        addStyleName("header");
        Embedded valonia = new Embedded(null, new ThemeResource(
                "img/valonia_logo.png"));
        valonia.addStyleName("valonia-logo");
        Label changeLanguage = new Label(root.getMessages().getString(
                Messages.choose_language));
        changeLanguage.setWidth(null);
        changeLanguage.addStyleName("change-language");
        fin = new Button(null, clickListener);
        swe = new Button(null, clickListener);
        en = new Button(null, clickListener);
        fin.addStyleName("flag_fi");
        swe.addStyleName("flag_se");
        en.addStyleName("flag_en");
        CssLayout language = new CssLayout();
        language.addStyleName("language-layout");
        language.setSizeUndefined();

        addComponent(valonia);
        addComponent(language);
        language.addComponent(changeLanguage);
        language.addComponent(fin);
        language.addComponent(swe);
        language.addComponent(en);
        setWidth("1000px");
    }
}

package fi.valonia.pyorallatoihin.views;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinUI;

@SuppressWarnings("serial")
public class Header extends CssLayout {
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
            PyorallaToihinUI.get().setLanguage(language);
        }
    };

    public Header(boolean loggedIn) {
        final PyorallaToihinUI ui =  PyorallaToihinUI.get();
        addStyleName("header");
        Button valonia = new Button(null);
        valonia.setIcon(new ThemeResource("img/valonia_logo.png"));
        valonia.addStyleName("valonia-logo");
        valonia.addStyleName(ChameleonTheme.BUTTON_BORDERLESS);
        valonia.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 4904853297678384550L;

            @Override
            public void buttonClick(ClickEvent event) {
                Page.getCurrent().setUriFragment("", true);
            }
        });
        Label changeLanguage = new Label(ui.getMessages().getString(
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

        language.setSizeUndefined();

        if (loggedIn) {
            CssLayout langAndLogout = new CssLayout();
            Button logOut = new Button(ui.getMessages().getString(
                    Messages.log_out));
            logOut.addStyleName(ChameleonTheme.BUTTON_LINK);
            logOut.addStyleName("log-out");
            logOut.addClickListener(new ClickListener() {
                private static final long serialVersionUID = 4904853297678384550L;

                @Override
                public void buttonClick(ClickEvent event) {
                	Page.getCurrent().setUriFragment("", true);
                }
            });
            langAndLogout.addComponent(language);
            langAndLogout.addComponent(logOut);
            addComponent(langAndLogout);
            langAndLogout.addStyleName("header-right");
        } else {
            addComponent(language);
            language.addStyleName("header-right");
        }
        addComponent(valonia);
        language.addComponent(changeLanguage);
        language.addComponent(fin);
        language.addComponent(swe);
        language.addComponent(en);
        setWidth("1000px");
    }
}

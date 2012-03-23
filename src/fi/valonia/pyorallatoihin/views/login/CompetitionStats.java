package fi.valonia.pyorallatoihin.views.login;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class CompetitionStats extends CssLayout {

    public CompetitionStats() {
        addStyleName("competition-stats");
        Label label = new Label("[STATS]");
        label.addStyleName("competition-stats-label");
        addComponent(label);
        setWidth("600px");
        setHeight("150px");
    }
}

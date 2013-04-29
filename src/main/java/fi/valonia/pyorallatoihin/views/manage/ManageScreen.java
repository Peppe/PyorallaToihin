package fi.valonia.pyorallatoihin.views.manage;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ManageScreen extends VerticalLayout implements ClickListener {
    private static final long serialVersionUID = 4101570212559349303L;

    CompetitionStatisticsView stats = new CompetitionStatisticsView();
    CompanyEditView companies = null;
    CompetitionSettings settings = null;

    Button statsButton = new Button("Tilastot", this);
    Button companiesButton = new Button("Muokkaa yritysten tiedot", this);
    Button settingsButton = new Button("Asetukset", this);

    private final Panel panel;

    public ManageScreen() {
        setSizeFull();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(statsButton);
        buttons.addComponent(companiesButton);
        buttons.addComponent(settingsButton);
        companiesButton.setEnabled(false);
        settingsButton.setEnabled(false);
        panel = new Panel();
        panel.setSizeFull();
        panel.setContent(stats);
        addComponent(buttons);
        addComponent(panel);
        setMargin(true);
        setSpacing(true);
        setExpandRatio(panel, 1);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == statsButton) {
            if (stats == null) {
                stats = new CompetitionStatisticsView();
            }
            panel.setContent(stats);
        } else if (event.getButton() == companiesButton) {
            if (companies == null) {
                companies = new CompanyEditView();
            }
            panel.setContent(companies);
        } else if (event.getButton() == settingsButton) {
            if (settings == null) {
                settings = new CompetitionSettings();
            }
            panel.setContent(settings);
        }
        panel.requestRepaint();
    }
}

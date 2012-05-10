package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label.ContentMode;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;

public class CompanyStats extends VerticalLayout {
    private static final long serialVersionUID = 8524524551101097444L;

    private final Company company;

    private final CssLayout actualLayout;

    public CompanyStats(Company company) {
        this.company = company;
        setWidth("1120px");
        setHeight("150px");
        actualLayout = new CssLayout();
        actualLayout.addStyleName("company-stats");
        actualLayout.setWidth("1000px");
        actualLayout.setHeight("150px");
        addComponent(actualLayout);
        setComponentAlignment(actualLayout, Alignment.MIDDLE_CENTER);
        if (company.getTotalMarkers() != 0) {
            fillInCompanyStats();
        } else {
            showInstructions();
        }
    }

    private void fillInCompanyStats() {
        GridLayout layout = new GridLayout(5, 6);
        PyorallaToihinRoot root = (PyorallaToihinRoot) Root.getCurrentRoot();

        Label employeesLabel = new Label(root.getMessages().getString(
                Messages.company_stats_employees));
        Label participantsLabel = new Label(root.getMessages().getString(
                Messages.company_stats_participants));
        Label markersLabel = new Label(root.getMessages().getString(
                Messages.company_stats_markers));
        Label kmLabel = new Label(root.getMessages().getString(
                Messages.company_stats_km));
        Label averageMarkesLabel = new Label(root.getMessages().getString(
                Messages.company_stats_times_per_employee));
        // Label averageKmLabel = new Label("kilometrit / työntekijä");

        int partSize = company.getEmployees().size();
        int totalMarkers = company.getTotalMarkers();

        Label employees = new Label(String.valueOf(company.getSize()));
        Label participants = new Label(String.valueOf(partSize));
        Label markers = new Label(totalMarkers + " (" + company.getSize() * 5
                + ")");
        Label totalKm = new Label(String.valueOf(company.getTotalKm()));
        double avgKm = ((double) totalMarkers) / ((double) company.getSize());
        double roundedAvgKm = Math.round(avgKm * 100) / 100d;
        Label avgMarkers = new Label(String.valueOf(roundedAvgKm));

        layout.addComponent(employeesLabel, 0, 0);
        layout.addComponent(participantsLabel, 0, 1);
        layout.addComponent(markersLabel, 0, 2);
        layout.addComponent(kmLabel, 0, 3);
        layout.addComponent(averageMarkesLabel, 0, 4);
        // layout.addComponent(averageKmLabel, 0, 5);

        layout.addComponent(employees, 1, 0);
        layout.addComponent(participants, 1, 1);
        layout.addComponent(markers, 1, 2);
        layout.addComponent(totalKm, 1, 3);
        layout.addComponent(avgMarkers, 1, 4);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidth("100%");
        layout.setHeight(null);
        // layout.addComponent(averageKmLabel, 5, 1, 5);

        Label contactInfoLabel = new Label(root.getMessages().getString(
                Messages.contact_person_info));
        contactInfoLabel.addStyleName(ChameleonTheme.LABEL_H4);
        Label contactNameLabel = new Label(root.getMessages().getString(
                Messages.contact_person_name));
        Label contactEmailLabel = new Label(root.getMessages().getString(
                Messages.contact_person_email));
        Label contactPhoneLabel = new Label(root.getMessages().getString(
                Messages.contact_person_phone));

        Label contactName = new Label(company.getContactName());
        Label contactEmail = new Label(company.getContactEmail());
        Label contactPhone = new Label(company.getContactPhone());

        layout.addComponent(contactInfoLabel, 3, 0, 4, 0);
        layout.addComponent(contactNameLabel, 3, 1);
        layout.addComponent(contactEmailLabel, 3, 2);
        layout.addComponent(contactPhoneLabel, 3, 3);

        layout.addComponent(contactName, 4, 1);
        layout.addComponent(contactEmail, 4, 2);
        layout.addComponent(contactPhone, 4, 3);
        actualLayout.addComponent(layout);
    }

    private void showInstructions() {
        String token = ((PyorallaToihinRoot) Root.getCurrentRoot()).getToken();
        PyorallaToihinRoot root = (PyorallaToihinRoot) Root.getCurrentRoot();

        Label workplaceRegistered = new Label(root.getMessages().getString(
                Messages.company_registed_to_competition));
        Label address = new Label(Root.getCurrentRoot().getApplication()
                .getURL()
                + "#/" + token);
        address.addStyleName(ChameleonTheme.LABEL_H2);
        address.addStyleName(ChameleonTheme.LABEL_COLOR);
        Label shareToken = new Label(root.getMessages().getString(
                Messages.company_share_token_start)
                + " <span style=\"font-size:16px;color:#60B30E\">"
                + token
                + "</span> "
                + root.getMessages()
                        .getString(Messages.company_share_token_end)
                + ", "
                + Root.getCurrentRoot().getApplication().getURL(),
                ContentMode.XHTML);
        workplaceRegistered.setWidth(null);
        address.setWidth(null);
        shareToken.setWidth(null);

        VerticalLayout wrapper = new VerticalLayout();
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(workplaceRegistered);
        layout.addComponent(address);
        layout.addComponent(shareToken);
        layout.setComponentAlignment(workplaceRegistered, Alignment.TOP_CENTER);
        layout.setComponentAlignment(address, Alignment.TOP_CENTER);
        layout.setComponentAlignment(shareToken, Alignment.TOP_CENTER);
        layout.setSizeUndefined();
        wrapper.setSizeFull();
        wrapper.addComponent(layout);
        wrapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        actualLayout.addComponent(wrapper);
    }
}

package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label.ContentMode;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;

public class CompanyStats extends CssLayout {
    private static final long serialVersionUID = 8524524551101097444L;

    private final Company company;

    public CompanyStats(Company company) {
        this.company = company;
        addStyleName("company-stats");
        if (company.getTotalMarkers() != 0) {
            fillInCompanyStats();
        } else {
            showInstructions();
        }
    }

    private void fillInCompanyStats() {
        CssLayout layout = new CssLayout();
        CssLayout labels = new CssLayout();
        CssLayout values = new CssLayout();
        CssLayout contact = new CssLayout();
        layout.addStyleName("company-stats-layout");
        labels.addStyleName("company-stats-labels");
        values.addStyleName("company-stats-values");
        contact.addStyleName("company-stats-contact");

        layout.addComponent(labels);
        layout.addComponent(values);
        layout.addComponent(contact);

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

        labels.addComponent(employeesLabel);
        labels.addComponent(participantsLabel);
        labels.addComponent(markersLabel);
        labels.addComponent(kmLabel);
        labels.addComponent(averageMarkesLabel);
        // layout.addComponent(averageKmLabel, 0, 5);

        values.addComponent(employees);
        values.addComponent(participants);
        values.addComponent(markers);
        values.addComponent(totalKm);
        values.addComponent(avgMarkers);
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

        CssLayout contactInfo = new CssLayout();
        CssLayout contactLabels = new CssLayout();
        CssLayout contactValues = new CssLayout();

        contactInfo.addStyleName("company-stats-contact-info");
        contactLabels.addStyleName("company-stats-contact-labels");
        contactValues.addStyleName("company-stats-contact-values");

        contact.addComponent(contactInfoLabel);
        contact.addComponent(contactInfo);
        contactInfo.addComponent(contactLabels);
        contactInfo.addComponent(contactValues);

        contactLabels.addComponent(contactNameLabel);
        contactLabels.addComponent(contactEmailLabel);
        contactLabels.addComponent(contactPhoneLabel);

        contactValues.addComponent(contactName);
        contactValues.addComponent(contactEmail);
        contactValues.addComponent(contactPhone);
        addComponent(layout);
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
        addComponent(wrapper);
    }
}

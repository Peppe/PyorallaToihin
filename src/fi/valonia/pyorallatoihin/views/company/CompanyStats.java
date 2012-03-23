package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.valonia.pyorallatoihin.data.Company;

public class CompanyStats extends CssLayout {

    public CompanyStats() {
        addStyleName("company-stats");
        addStyleName("company-stats-line-height");
        Label label = new Label("[STATS]");
        label.addStyleName("company-stats-label");
        addComponent(label);
        setWidth("1000px");
        setHeight("150px");

    }

    public CompanyStats(Company company) {
        addStyleName("company-stats");

        GridLayout layout = new GridLayout(4, 6);
        Label employeesLabel = new Label("Työntekijöitä");
        Label participantsLabel = new Label("Osallistujia");
        Label markersLabel = new Label("Rasteja / max");
        Label kmLabel = new Label("Kilometrit");
        Label averageMarkesLabel = new Label("Kerrat / työntekijä");
        // Label averageKmLabel = new Label("kilometrit / työntekijä");

        int partSize = company.getEmployees().size();
        int totalMarkers = company.getTotalMarkers();

        Label employees = new Label(String.valueOf(company.getSize()));
        Label participants = new Label(String.valueOf(partSize));
        Label markers = new Label(totalMarkers + " / "
                + company.getEmployees().size() * 5);
        Label totalKm = new Label(String.valueOf(company.getTotalKm()));
        double aaa = Math.round(totalMarkers / (double) partSize * 100);
        double bbb = aaa / 100;
        Label avgMarkers = new Label(String.valueOf(bbb));
        Label label = new Label("[STATS]");
        label.addStyleName("company-stats-label");

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
        layout.setSizeFull();
        // layout.addComponent(averageKmLabel, 5, 1, 5);

        addComponent(layout);
        setWidth("1000px");
        setHeight("150px");
    }
}

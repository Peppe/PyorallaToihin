package fi.valonia.pyorallatoihin.views.manage;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Root;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

public class ManageScreen extends VerticalLayout {
    private static final long serialVersionUID = 4101570212559349303L;

    public ManageScreen() {
        Table table = createManageTable();
        addComponent(table);
        setSizeFull();
        setExpandRatio(table, 1);
        setMargin(true);
    }

    private Table createManageTable() {
        Table table = new Table();
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        table.setSizeFull();
        ICompanyService companyService = ((PyorallaToihinRoot) Root
                .getCurrentRoot()).getCompanyService();
        List<Company> companies = companyService.getAllCompanies();
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("Id", Integer.class, null);
        container.addContainerProperty("Tunnus", String.class, null);
        container.addContainerProperty("Nimi", String.class, null);
        container.addContainerProperty("Kausi", Integer.class, null);
        container.addContainerProperty("Ilmoitettu koko", Integer.class, null);
        container.addContainerProperty("Osallistujia", Integer.class, null);
        container.addContainerProperty("Ensimmäinen kerta", String.class, null);
        container.addContainerProperty("Kuullut", String.class, null);
        container.addContainerProperty("Katuosoite", String.class, null);
        container.addContainerProperty("Postinumero", String.class, null);
        container.addContainerProperty("Kaupunki", String.class, null);
        container.addContainerProperty("Yhteyshenkilön nimi", String.class,
                null);
        container.addContainerProperty("Yhteyshenkilön sähköposti",
                String.class, null);
        container.addContainerProperty("Yhteyshenkilön puhelinnumero",
                String.class, null);
        container.addContainerProperty("Yhteenlaskettu km", Double.class, null);
        container.addContainerProperty("Merkintöjä", Integer.class, null);

        for (Company company : companies) {
            Item item = container.addItem(company);
            item.getItemProperty("Id").setValue(company.getId());
            item.getItemProperty("Tunnus").setValue(company.getToken());
            item.getItemProperty("Nimi").setValue(company.getName());
            item.getItemProperty("Kausi").setValue(company.getSeasonId());
            item.getItemProperty("Ilmoitettu koko").setValue(company.getSize());
            item.getItemProperty("Osallistujia").setValue(
                    company.getEmployees().size());
            if (company.isFirstTime()) {
                item.getItemProperty("Ensimmäinen kerta").setValue("Kyllä");
            } else {
                item.getItemProperty("Ensimmäinen kerta").setValue("Ei");
            }
            item.getItemProperty("Kuullut").setValue(company.getHeardFrom());
            item.getItemProperty("Katuosoite").setValue(
                    company.getStreetAddress());
            item.getItemProperty("Postinumero").setValue(company.getZip());
            item.getItemProperty("Kaupunki").setValue(company.getCity());
            item.getItemProperty("Yhteyshenkilön nimi").setValue(
                    company.getContactName());
            item.getItemProperty("Yhteyshenkilön sähköposti").setValue(
                    company.getContactEmail());
            item.getItemProperty("Yhteyshenkilön puhelinnumero").setValue(
                    company.getContactPhone());
            item.getItemProperty("Yhteenlaskettu km").setValue(
                    company.getTotalKm());
            item.getItemProperty("Merkintöjä").setValue(
                    company.getTotalMarkers());
        }
        table.setContainerDataSource(container);
        return table;
    }
}

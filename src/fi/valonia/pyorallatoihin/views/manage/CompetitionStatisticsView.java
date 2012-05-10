package fi.valonia.pyorallatoihin.views.manage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Root;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

public class CompetitionStatisticsView extends VerticalLayout {
    private static final long serialVersionUID = 5290566709225821908L;
    public static final String sizeAll = "Kaikki";
    public static final String size1to4 = "1 - 4 ty�ntekij��";
    public static final String size5to20 = "5 - 20 ty�ntekij��";
    public static final String size21to100 = "21 - 100 ty�ntekij��";
    public static final String sizeOver100 = "Yli 100 ty�ntekij��";
    private IndexedContainer container;

    public CompetitionStatisticsView() {
        Layout filters = createFilterPanel();
        Table table = createManageTable();
        Layout buttons = createCsvPanel();
        addComponent(filters);
        addComponent(table);
        addComponent(buttons);
        setSizeFull();
        setExpandRatio(table, 1);
        setMargin(true);
        setSpacing(true);
        loadValues(sizeAll);
    }

    private Layout createFilterPanel() {
        HorizontalLayout layout = new HorizontalLayout();
        Label label = new Label("N�yt� yrikset kokoluokassa:");
        ComboBox combobox = new ComboBox();
        combobox.addItem(sizeAll);
        combobox.addItem(size1to4);
        combobox.addItem(size5to20);
        combobox.addItem(size21to100);
        combobox.addItem(sizeOver100);
        combobox.select(sizeAll);
        combobox.setNullSelectionAllowed(false);
        combobox.setImmediate(true);
        combobox.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = -3536246737228200039L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                loadValues((String) event.getProperty().getValue());
            }
        });
        layout.addComponent(label);
        layout.addComponent(combobox);
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        return layout;
    }

    private Layout createCsvPanel() {
        HorizontalLayout layout = new HorizontalLayout();
        Button button = new Button("Lataa tiedot", new ClickListener() {
            private static final long serialVersionUID = 8543929363951552224L;

            @Override
            public void buttonClick(ClickEvent event) {
                createCsvStream();
            }
        });
        layout.addComponent(button);
        layout.setSizeUndefined();
        layout.setSpacing(true);
        return layout;
    }

    private void loadValues(String size) {
        ICompanyService companyService = ((PyorallaToihinRoot) Root
                .getCurrentRoot()).getCompanyService();
        List<Company> companies = null;
        if (size.equals(size1to4)) {
            companies = companyService.getAllCompanies(1, 4);
        } else if (size.equals(size5to20)) {
            companies = companyService.getAllCompanies(5, 20);
        } else if (size.equals(size21to100)) {
            companies = companyService.getAllCompanies(21, 100);
        } else if (size.equals(sizeOver100)) {
            companies = companyService.getAllCompanies(100, -1);
        } else {
            companies = companyService.getAllCompanies();
        }
        container.removeAllItems();
        if (companies != null) {
            for (Company company : companies) {
                Item item = container.addItem(company);
                item.getItemProperty("Id").setValue(company.getId());
                item.getItemProperty("Tunnus").setValue(company.getToken());
                item.getItemProperty("Nimi").setValue(company.getName());
                item.getItemProperty("Kausi").setValue(company.getSeasonId());
                item.getItemProperty("Ilmoitettu koko").setValue(
                        company.getSize());
                item.getItemProperty("Osallistujia").setValue(
                        company.getEmployees().size());
                if (company.isFirstTime()) {
                    item.getItemProperty("Ensimm�inen kerta").setValue("Kyll�");
                } else {
                    item.getItemProperty("Ensimm�inen kerta").setValue("Ei");
                }
                item.getItemProperty("Kuullut")
                        .setValue(company.getHeardFrom());
                item.getItemProperty("Katuosoite").setValue(
                        company.getStreetAddress());
                item.getItemProperty("Postinumero").setValue(company.getZip());
                item.getItemProperty("Kaupunki").setValue(company.getCity());
                item.getItemProperty("Yhteyshenkil�n nimi").setValue(
                        company.getContactName());
                item.getItemProperty("Yhteyshenkil�n s�hk�posti").setValue(
                        company.getContactEmail());
                item.getItemProperty("Yhteyshenkil�n puhelinnumero").setValue(
                        company.getContactPhone());
                item.getItemProperty("Yhteenlaskettu km").setValue(
                        company.getTotalKm());
                item.getItemProperty("Merkint�j�").setValue(
                        company.getTotalMarkers());
            }
        }
    }

    private Table createManageTable() {
        Table table = new Table();
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        table.setSizeFull();
        container = new IndexedContainer();
        container.addContainerProperty("Id", Integer.class, null);
        container.addContainerProperty("Tunnus", String.class, null);
        container.addContainerProperty("Nimi", String.class, null);
        container.addContainerProperty("Kausi", Integer.class, null);
        container.addContainerProperty("Ilmoitettu koko", Integer.class, null);
        container.addContainerProperty("Osallistujia", Integer.class, null);
        container.addContainerProperty("Ensimm�inen kerta", String.class, null);
        container.addContainerProperty("Kuullut", String.class, null);
        container.addContainerProperty("Katuosoite", String.class, null);
        container.addContainerProperty("Postinumero", String.class, null);
        container.addContainerProperty("Kaupunki", String.class, null);
        container.addContainerProperty("Yhteyshenkil�n nimi", String.class,
                null);
        container.addContainerProperty("Yhteyshenkil�n s�hk�posti",
                String.class, null);
        container.addContainerProperty("Yhteyshenkil�n puhelinnumero",
                String.class, null);
        container.addContainerProperty("Yhteenlaskettu km", Double.class, null);
        container.addContainerProperty("Merkint�j�", Integer.class, null);
        table.setContainerDataSource(container);
        return table;
    }

    private void createCsvStream() {
        final StringBuilder string = new StringBuilder();

        Collection<?> containerProperties = container.getContainerPropertyIds();
        Iterator<?> propertyIterator = containerProperties.iterator();
        while (propertyIterator.hasNext()) {
            Object property = propertyIterator.next();
            string.append(property.toString());
            if (propertyIterator.hasNext()) {
                string.append(',');
            } else {
                string.append('\n');
            }
        }
        for (Object itemId : container.getItemIds()) {
            Item item = container.getItem(itemId);
            propertyIterator = containerProperties.iterator();
            while (propertyIterator.hasNext()) {
                Object property = propertyIterator.next();
                string.append(item.getItemProperty(property).getValue());
                if (propertyIterator.hasNext()) {
                    string.append(',');
                } else {
                    string.append('\n');
                }
            }
        }

        StreamResource.StreamSource streamSource = new StreamSource() {
            private static final long serialVersionUID = 7232269604288254725L;

            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(string.toString().getBytes());
            }
        };
        StreamResource resource = new StreamResource(streamSource,
                "tilastot.csv", Root.getCurrentRoot().getApplication());
        Root.getCurrentRoot().open(resource);
    }
}
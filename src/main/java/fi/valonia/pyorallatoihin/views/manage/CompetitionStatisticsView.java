package fi.valonia.pyorallatoihin.views.manage;

import java.math.BigDecimal;
import java.util.List;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import fi.valonia.pyorallatoihin.PyorallaToihinUI;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

@SuppressWarnings("serial")
public class CompetitionStatisticsView extends VerticalLayout {
    public static final String sizeAll = "Kaikki";
    public static final String size1to4 = "1 - 4 työntekijää";
    public static final String size5to20 = "5 - 20 työntekijää";
    public static final String size21to100 = "21 - 100 työntekijää";
    public static final String sizeOver100 = "Yli 100 työntekijää";
    private Table table;
    private IndexedContainer container;

    public CompetitionStatisticsView() {
        Layout filters = createFilterPanel();
        table = createManageTable();
        Layout buttons = createExportButton();
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
        Label label = new Label("Näytä yrikset kokoluokassa:");
        ComboBox combobox = new ComboBox();
        combobox.addItem(sizeAll);
        combobox.addItem(size1to4);
        combobox.addItem(size5to20);
        combobox.addItem(size21to100);
        combobox.addItem(sizeOver100);
        combobox.select(sizeAll);
        combobox.setNullSelectionAllowed(false);
        combobox.setImmediate(true);
        combobox.addValueChangeListener(new ValueChangeListener() {
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

	private Layout createExportButton() {
        HorizontalLayout layout = new HorizontalLayout();
        final Button excelExportButton = new Button("Vie tiedot exceliin");
        excelExportButton.addClickListener(new ClickListener() {
            private ExcelExport excelExport;

            public void buttonClick(final ClickEvent event) {
                excelExport = new ExcelExport(table);
                excelExport.export();
                excelExport.setDisplayTotals(false);
            }
        });
        layout.addComponent(excelExportButton);
        layout.setSizeUndefined();
        layout.setSpacing(true);
        return layout;
    }

    private Table createManageTable() {
        Table table = new Table();
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        table.setSizeFull();
        container = new IndexedContainer();
        container.addContainerProperty("Id", Integer.class, null);
        container.addContainerProperty("Kausi", Integer.class, null);
        container.addContainerProperty("Tunnus", String.class, null);
        container.addContainerProperty("Nimi", String.class, null);
        container.addContainerProperty("Ilmoitettu koko", Integer.class, null);
        container.addContainerProperty("Osallistujia", Integer.class, null);
        container.addContainerProperty("Yhteenlaskettu km", Double.class, null);
        container.addContainerProperty("Merkintöjä", Integer.class, null);
        container.addContainerProperty("Maks. merkintöjä", BigDecimal.class,
                null);
        container.addContainerProperty("Kerrat per työntekijä",
                BigDecimal.class, null);
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
        table.setContainerDataSource(container);
        return table;
    }

    @SuppressWarnings("unchecked")
	private void loadValues(String size) {
        ICompanyService companyService = PyorallaToihinUI.get().getCompanyService();
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
                item.getItemProperty("Kausi").setValue(company.getSeasonId());
                item.getItemProperty("Tunnus").setValue(company.getToken());
                item.getItemProperty("Nimi").setValue(company.getName());
                int companySize = company.getSize();
                item.getItemProperty("Ilmoitettu koko").setValue(companySize);
                int companyMarkers = company.getTotalMarkers();
                item.getItemProperty("Osallistujia").setValue(
                        company.getEmployees().size());
                item.getItemProperty("Merkintöjä").setValue(companyMarkers);
                BigDecimal maxMarkers = new BigDecimal(companySize);
                maxMarkers = maxMarkers.multiply(new BigDecimal("5"));
                maxMarkers.setScale(0);
                item.getItemProperty("Maks. merkintöjä").setValue(maxMarkers);

                BigDecimal markers = new BigDecimal(companyMarkers);
                markers = markers.divide(new BigDecimal(company.getSize()), 3,
                        BigDecimal.ROUND_HALF_UP);
                item.getItemProperty("Kerrat per työntekijä").setValue(markers);
                item.getItemProperty("Yhteenlaskettu km").setValue(
                        company.getTotalKm());
                if (company.isFirstTime()) {
                    item.getItemProperty("Ensimmäinen kerta").setValue("Kyllä");
                } else {
                    item.getItemProperty("Ensimmäinen kerta").setValue("Ei");
                }
                item.getItemProperty("Kuullut")
                        .setValue(company.getHeardFrom());
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
            }
        }
    }
}

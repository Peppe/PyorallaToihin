package fi.valonia.pyorallatoihin.views.login;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinUI;
import fi.valonia.pyorallatoihin.components.Spacer;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.interfaces.CompanyNameInUseException;

public class RegisterCompanyForm extends VerticalLayout {
    private static final long serialVersionUID = 3185151633819175680L;

    Company company = new Company();
    FieldGroup fieldGroup = new FieldGroup(new BeanItem<Company>(company));
    private Label errorMessage = null;
    private TextField companyName;
    private TextField contactPersonName;
    private TextField contactPersonEmail;

    private final PyorallaToihinUI root;

    private final ClickListener registerListener = new ClickListener() {
        private static final long serialVersionUID = 7715075013147891378L;

        @Override
        public void buttonClick(ClickEvent event) {
            try {

                if (companyName.getValue() == null
                        || companyName.getValue().equals("")) {
                    showError(root.getMessages().getString(
                            Messages.company_name_can_not_be_empty));
                    return;
                }
                if (companySize.getValue() == null
                        || companySize.getValue().equals("")) {
                    showError(root.getMessages().getString(
                            Messages.company_size_numeric));
                    return;
                }
                if (contactPersonName.getValue() == null
                        || contactPersonName.getValue().equals("")
                        || contactPersonEmail.getValue() == null
                        || contactPersonEmail.getValue().equals("")) {
                    showError(root.getMessages().getString(
                            Messages.contact_person_info_can_not_be_empty));
                    return;
                }
                fieldGroup.commit();
                if ("Muulla".equals(company.getHeardFrom())) {
                    company.setHeardFrom(heardFromOther.getValue());
                }
                PyorallaToihinUI ui = PyorallaToihinUI.get();
                ui.getCompanyService().createCompany(company);
                ui.showCompany(company);
            } catch (CommitException e) {
                e.printStackTrace();
            } catch (CompanyNameInUseException e) {
                showError(root.getMessages().getString(
                        Messages.company_name_already_exists));
            }
        }
    };

    private TextField companySize;

    private TextField heardFromOther;

    public RegisterCompanyForm(PyorallaToihinUI root) {
        this.root = root;
        setWidth("100%");
        setSpacing(true);
        CssLayout spacer = new CssLayout();
        spacer.setStyleName("register-company-spacer");

        // HorizontalLayout splitter = new HorizontalLayout();
        // splitter.setWidth("100%");
        // VerticalLayout leftColumn = createLeftColumn();
        // VerticalLayout rightColumn = createRightColumn();
        // splitter.addComponent(leftColumn);
        // splitter.addComponent(rightColumn);
        // splitter.setSpacing(true);
        GridLayout grid = createGrid();

        CssLayout spacer2 = new CssLayout();
        spacer2.setStyleName("register-company-spacer");

        Button register = new Button(root.getMessages().getString(
                Messages.register), registerListener);

        addComponent(spacer);
        addComponent(grid);
        addComponent(spacer2);
        addComponent(register);

    }

    private GridLayout createGrid() {
        final GridLayout layout = new GridLayout(2, 8);
        layout.setSpacing(true);
        layout.setWidth("100%");

        Label workInfo = new Label(root.getMessages().getString(
                Messages.company_info));
        workInfo.setStyleName(ChameleonTheme.LABEL_H4);
        companyName = new TextField(root.getMessages().getString(
                Messages.company_name));
        companyName.setWidth("100%");
        companyName.setRequired(true);

        companySize = new TextField(root.getMessages().getString(
                Messages.company_size));
        companySize.setWidth("100%");
        companySize.setRequired(true);

        TextField companyStreet = new TextField(root.getMessages().getString(
                Messages.company_address));
        companyStreet.setWidth("100%");
        companyStreet.setInputPrompt(root.getMessages().getString(
                Messages.company_street));
        TextField companyZip = new TextField();
        companyZip.setInputPrompt(root.getMessages().getString(
                Messages.company_zip));
        companyZip.setColumns(7);
        TextField companyCity = new TextField();
        companyCity.setWidth("100%");
        companyCity.setInputPrompt(root.getMessages().getString(
                Messages.company_city));
        HorizontalLayout zipAndCity = new HorizontalLayout();
        zipAndCity.addComponent(companyZip);
        zipAndCity.addComponent(companyCity);
        zipAndCity.setWidth("100%");
        zipAndCity.setExpandRatio(companyCity, 1);
        zipAndCity.setSpacing(true);

        Label contactPersonInfo = new Label(root.getMessages().getString(
                Messages.contact_person_info));
        contactPersonInfo.setWidth("100%");
        contactPersonInfo.setStyleName(ChameleonTheme.LABEL_H4);
        contactPersonName = new TextField(root.getMessages().getString(
                Messages.contact_person_name));
        contactPersonName.setRequired(true);
        contactPersonName.setWidth("100%");
        contactPersonEmail = new TextField(root.getMessages().getString(
                Messages.contact_person_email));
        contactPersonEmail.setRequired(true);
        contactPersonEmail.setWidth("100%");
        TextField contactPersonPhone = new TextField(root.getMessages()
                .getString(Messages.contact_person_phone));
        contactPersonPhone.setWidth("100%");

        // TODO translate
        final ComboBox heardFrom = new ComboBox(root.getMessages().getString(
                Messages.heard_from));
        heardFrom.addContainerProperty("caption", String.class, "");
        Item item;
        item = heardFrom.addItem("S�hk�postilla sis�isesti");
        item.getItemProperty("caption").setValue(
                root.getMessages()
                        .getString(Messages.heard_from_email_internal));
        item = heardFrom.addItem("S�hk�postilla ulkoisesti");
        item.getItemProperty("caption").setValue(
                root.getMessages()
                        .getString(Messages.heard_from_email_external));
        item = heardFrom.addItem("Kirjeen�");
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.heard_from_letter));
        item = heardFrom.addItem("Internetist�");
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.heard_from_internet));
        item = heardFrom.addItem("Lehdest�");
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.heard_from_paper));
        item = heardFrom.addItem("Radiosta");
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.heard_from_radio));
        item = heardFrom.addItem("Kirjastosta");
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.heard_from_library));
        item = heardFrom.addItem("Liikuntapaikalta");
        item.getItemProperty("caption")
                .setValue(
                        root.getMessages().getString(
                                Messages.heard_from_sports_center));
        item = heardFrom.addItem("Muulla");
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.heard_from_other));
        heardFrom.setItemCaptionPropertyId("caption");
        heardFrom.setImmediate(true);
        heardFromOther = new TextField();
        heardFromOther.setWidth("100%");
        heardFromOther.setEnabled(false);
        final CheckBox firstTime = new CheckBox(root.getMessages().getString(
                Messages.first_time));
        firstTime.setWidth("100%");

        heardFrom.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = -7726168308206839583L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                if ("Muulla".equals(heardFrom.getValue())) {
                    heardFromOther.setEnabled(true);
                } else {
                    heardFromOther.setEnabled(false);
                }
            }
        });
        heardFrom.setWidth("100%");

        Spacer spacer = new Spacer("100%", "20px");
        companySize.setValue("");
        layout.addComponent(workInfo, 0, 0);
        layout.addComponent(companyName, 0, 1);
        layout.addComponent(companySize, 0, 2);
        layout.addComponent(companyStreet, 0, 3);
        layout.addComponent(zipAndCity, 0, 4);
        layout.addComponent(contactPersonInfo, 1, 0);
        layout.addComponent(contactPersonName, 1, 1);
        layout.addComponent(contactPersonEmail, 1, 2);
        layout.addComponent(contactPersonPhone, 1, 3);
        layout.addComponent(heardFrom, 1, 4);
        layout.addComponent(heardFromOther, 1, 5);
        layout.addComponent(spacer, 0, 6, 1, 6);
        layout.addComponent(firstTime, 0, 7, 1, 7);

        layout.setComponentAlignment(zipAndCity, Alignment.BOTTOM_LEFT);

        fieldGroup.bind(companyName, "name");
        fieldGroup.bind(companyStreet, "streetAddress");
        fieldGroup.bind(companyZip, "zip");
        fieldGroup.bind(companyCity, "city");

        fieldGroup.bind(contactPersonName, "contactName");
        fieldGroup.bind(contactPersonEmail, "contactEmail");
        fieldGroup.bind(contactPersonPhone, "contactPhone");
        fieldGroup.bind(companySize, "size");
        fieldGroup.bind(heardFrom, "heardFrom");
        fieldGroup.bind(firstTime, "firstTime");

        return layout;
    }

    private void showError(String message) {
        if (errorMessage == null) {
            errorMessage = new Label();
            addComponent(errorMessage);
        }
        errorMessage.setValue(message);
    }
}

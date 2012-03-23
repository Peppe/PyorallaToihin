package fi.valonia.pyorallatoihin.views.login;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.Company.CompanySize;
import fi.valonia.pyorallatoihin.interfaces.CompanyNameInUseException;

public class RegisterCompanyForm extends VerticalLayout {

    Company company = new Company();
    FieldGroup fieldGroup = new FieldGroup(new BeanItem<Company>(company));
    private Label errorMessage = null;
    private TextField companyName;
    private TextField contactPersonName;
    private TextField contactPersonEmail;

    private final PyorallaToihinRoot root;

    private ClickListener registerListener = new ClickListener() {
        public void buttonClick(ClickEvent event) {
            try {

                if (companyName.getValue() == null
                        || companyName.getValue().equals("")) {
                    showError(root.getMessages().getString(
                            Messages.company_name_can_not_be_empty));
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
                PyorallaToihinRoot root = ((PyorallaToihinRoot) getRoot());
                root.getCompanyService().createCompany(company);
                root.showCompany(company);
            } catch (CommitException e) {
                e.printStackTrace();
            } catch (CompanyNameInUseException e) {
                showError(root.getMessages().getString(
                        Messages.company_name_already_exists));
            }
        }
    };

    public RegisterCompanyForm(PyorallaToihinRoot root) {
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

        OptionGroup companySize = new OptionGroup(root.getMessages().getString(
                Messages.company_size));
        companySize.addStyleName("company-size");
        companySize.addContainerProperty("caption", String.class, "");
        companySize.setItemCaptionPropertyId("caption");
        Item item;
        item = companySize.addItem(CompanySize.S1_4);
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.size_1_to_4));
        item = companySize.addItem(CompanySize.S5_20);
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.size_5_to_20));
        item = companySize.addItem(CompanySize.S21_100);
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.size_21_to_100));
        item = companySize.addItem(CompanySize.S_OVER_100);
        item.getItemProperty("caption").setValue(
                root.getMessages().getString(Messages.size_over_100));

        CssLayout spacer2 = new CssLayout();
        spacer2.setStyleName("register-company-spacer");

        Button register = new Button(root.getMessages().getString(
                Messages.register), registerListener);

        fieldGroup.bind(companySize, "size");

        addComponent(spacer);
        addComponent(grid);
        addComponent(companySize);
        addComponent(spacer2);
        addComponent(register);

    }

    private GridLayout createGrid() {
        GridLayout layout = new GridLayout(2, 4);
        layout.setSpacing(true);
        layout.setWidth("100%");

        Label workInfo = new Label(root.getMessages().getString(
                Messages.company_info));
        workInfo.setStyleName(ChameleonTheme.LABEL_H4);
        companyName = new TextField(root.getMessages().getString(
                Messages.company_name));
        companyName.setWidth("100%");
        companyName.setRequired(true);

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

        fieldGroup.bind(contactPersonName, "contactName");
        fieldGroup.bind(contactPersonEmail, "contactEmail");
        fieldGroup.bind(contactPersonPhone, "contactPhone");

        layout.addComponent(workInfo, 0, 0);
        layout.addComponent(companyName, 0, 1);
        layout.addComponent(companyStreet, 0, 2);
        layout.addComponent(zipAndCity, 0, 3);
        layout.addComponent(contactPersonInfo);
        layout.addComponent(contactPersonName);
        layout.addComponent(contactPersonEmail);
        layout.addComponent(contactPersonPhone);

        layout.setComponentAlignment(zipAndCity, Alignment.BOTTOM_LEFT);
        fieldGroup.bind(companyName, "name");
        fieldGroup.bind(companyStreet, "streetAddress");
        fieldGroup.bind(companyZip, "zip");
        fieldGroup.bind(companyCity, "city");

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

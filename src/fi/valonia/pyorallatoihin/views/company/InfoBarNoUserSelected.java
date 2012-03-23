package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.data.Item;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Sport;

public class InfoBarNoUserSelected extends CssLayout {

    private CompanyScreen companyScreen;
    private Button addUserButton;
    private final PyorallaToihinRoot root;
    private CssLayout margins;

    public InfoBarNoUserSelected(PyorallaToihinRoot root,
            CompanyScreen companyScreen) {
        this.root = root;
        this.companyScreen = companyScreen;
        setHeight("110px");
        setWidth("1000px");
        addStyleName("info-bar");

        ClickListener addUserClickListener = new ClickListener() {
            public void buttonClick(ClickEvent event) {
                showNewUserForm();
            }
        };
        margins = new CssLayout();
        margins.setHeight("100%");

        margins.addStyleName("add-user-button-margins");
        addUserButton = new Button(root.getMessages().getString(
                Messages.add_your_name), addUserClickListener);
        addUserButton.setSizeFull();
        addUserButton.addStyleName("add-user-button");
        addUserButton.setIcon(new ThemeResource("img/plus.png"));
        Label orLabel = new Label(root.getMessages().getString(Messages.or));
        Label clickNameLabel = new Label(root.getMessages().getString(
                Messages.click_your_name));
        Label clickDaysLabel = new Label(root.getMessages().getString(
                Messages.mark_in_table));

        margins.setWidth("250px");
        orLabel.setWidth("50px");
        clickNameLabel.setWidth("200px");
        clickDaysLabel.setWidth("200px");

        orLabel.addStyleName(ChameleonTheme.LABEL_H4);
        orLabel.addStyleName("label1");
        clickNameLabel.addStyleName("label2");
        clickDaysLabel.addStyleName("label3");

        margins.addComponent(addUserButton);
        addComponent(margins);
        addComponent(orLabel);
        addComponent(clickNameLabel);
        addComponent(clickDaysLabel);
    }

    private void showNewUserForm() {
        VerticalLayout newUserLayout = new VerticalLayout();
        newUserLayout.addStyleName("add-user");
        newUserLayout.setSpacing(true);
        newUserLayout.setWidth("200px");
        newUserLayout.setHeight(null);
        final TextField name = new TextField();
        name.setInputPrompt(root.getMessages().getString(Messages.name));
        name.setWidth("100%");
        final ComboBox sport = new ComboBox();
        sport.setInputPrompt(root.getMessages().getString(Messages.type));
        sport.addContainerProperty("caption", String.class, "");
        sport.setWidth("100%");
        for (Sport sportEnum : Sport.values()) {
            Item item = sport.addItem(sportEnum);
            item.getItemProperty("caption").setValue(
                    root.getMessages().getString(sportEnum.toString()));
        }
        sport.setItemCaptionPropertyId("caption");
        HorizontalLayout kmAndAdd = new HorizontalLayout();
        kmAndAdd.setWidth("100%");
        final TextField km = new TextField();
        km.setInputPrompt(root.getMessages().getString(Messages.km));
        km.setColumns(4);
        Button add = new Button(root.getMessages().getString(Messages.add),
                new ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        Employee employee = new Employee();
                        employee.setName(name.getValue());
                        employee.setSport((Sport) sport.getValue());
                        try {
                            // TODO only >0 values.
                            employee.setDistance(Integer.parseInt(km.getValue()));
                        } catch (NumberFormatException e) {
                            return;
                            // TODO message on failiure
                        }
                        companyScreen.addEmployee(employee);
                    }
                });
        add.setWidth("100%");
        kmAndAdd.addComponent(km);
        kmAndAdd.addComponent(add);
        kmAndAdd.setExpandRatio(add, 1);
        kmAndAdd.setSpacing(true);

        newUserLayout.addComponent(name);
        newUserLayout.addComponent(sport);
        newUserLayout.addComponent(kmAndAdd);
        newUserLayout.addStyleName("new-user-layout");

        // margins.replaceComponent(addUserButton, newUserLayout);
        replaceComponent(margins, newUserLayout);
    }
}

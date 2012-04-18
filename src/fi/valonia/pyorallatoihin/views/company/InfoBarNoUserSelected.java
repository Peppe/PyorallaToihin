package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.data.Item;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
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
    private static final long serialVersionUID = -7855678756157150282L;

    private final CompanyScreen companyScreen;
    private final Button addUserButton;
    private final PyorallaToihinRoot root;
    private final CssLayout margins;

    public InfoBarNoUserSelected(PyorallaToihinRoot root,
            CompanyScreen companyScreen) {
        this.root = root;
        this.companyScreen = companyScreen;
        setHeight("130px");
        setWidth("1000px");
        addStyleName("info-bar");

        ClickListener addUserClickListener = new ClickListener() {
            private static final long serialVersionUID = 4299103971361663492L;

            @Override
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
        Label clickDaysLabel = new Label(root.getMessages().getString(
                Messages.mark_in_table));
        Label clickNameLabel = new Label(root.getMessages().getString(
                Messages.click_your_name));

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
        if (companyScreen.getCompany().getEmployees().size() > 0) {
            addComponent(orLabel);
            addComponent(clickDaysLabel);
            addComponent(clickNameLabel);
        }
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
                    private static final long serialVersionUID = -4940737576260162546L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        Employee employee = new Employee();
                        employee.setName(name.getValue());
                        employee.setSport((Sport) sport.getValue());
                        try {
                            String kmString = km.getValue();
                            kmString = kmString.replace(',', '.');
                            double kmDouble = Double.parseDouble(kmString);
                            if (kmDouble >= 0) {
                                employee.setDistance(kmDouble);
                                companyScreen.addEmployee(employee);
                            }
                            // TODO only >0 values message.
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return;
                            // TODO message on failure
                        }
                    }
                });
        add.setWidth("100%");

        name.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
        sport.addStyleName(ChameleonTheme.SELECT_BIG);
        km.addStyleName(ChameleonTheme.TEXTFIELD_BIG);

        kmAndAdd.addComponent(km);
        kmAndAdd.addComponent(add);
        kmAndAdd.setExpandRatio(add, 1);
        kmAndAdd.setSpacing(true);
        kmAndAdd.setComponentAlignment(add, Alignment.MIDDLE_LEFT);

        newUserLayout.addComponent(name);
        newUserLayout.addComponent(sport);
        newUserLayout.addComponent(kmAndAdd);
        newUserLayout.addStyleName("new-user-layout");

        // margins.replaceComponent(addUserButton, newUserLayout);
        replaceComponent(margins, newUserLayout);
    }
}

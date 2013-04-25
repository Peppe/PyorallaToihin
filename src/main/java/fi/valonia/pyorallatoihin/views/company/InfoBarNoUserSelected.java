package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinUI;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.views.company.UserForm.FormSubmit;

@SuppressWarnings("serial")
public class InfoBarNoUserSelected extends CssLayout {

    private final CompanyScreen companyScreen;
    private final Button addUserButton;
    private final CssLayout margins;

    public InfoBarNoUserSelected(PyorallaToihinUI root,
            CompanyScreen companyScreen) {
        this.companyScreen = companyScreen;
        setHeight("130px");
        setWidth("1200px");
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
        FormSubmit formSubmit = new FormSubmit() {
            @Override
            public void submit(Employee employee) {
                companyScreen.addEmployee(employee);
            }
        };
        UserForm newUserLayout = new UserForm(new Employee(), formSubmit);
        replaceComponent(margins, newUserLayout);
    }
}

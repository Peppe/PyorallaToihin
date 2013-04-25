package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinUI;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Sport;

public class UserForm extends VerticalLayout {
    private static final long serialVersionUID = 310608597275797924L;

    interface FormSubmit {
        public void submit(Employee employee);
    }

    private final Employee employee;
    private final TextField name;
    private final ComboBox sport;
    private final TextField km;
    private final FormSubmit formSubmit;

    public UserForm(Employee empl, final FormSubmit formSubmit) {
        this.employee = empl;
        this.formSubmit = formSubmit;
        PyorallaToihinUI ui = PyorallaToihinUI.get();
        addStyleName("add-user");
        setSpacing(true);
        setWidth("250px");
        setHeight("100%");
        name = new TextField();
        name.setInputPrompt(ui.getMessages().getString(Messages.name));
        name.setWidth("100%");
        sport = new ComboBox();
        sport.setInputPrompt(ui.getMessages().getString(Messages.type));
        sport.addContainerProperty("caption", String.class, "");
        sport.setWidth("100%");
        for (Sport sportEnum : Sport.values()) {
            Item item = sport.addItem(sportEnum);
            item.getItemProperty("caption").setValue(
                    ui.getMessages().getString(sportEnum.toString()));
        }
        sport.setItemCaptionPropertyId("caption");
        // HorizontalLayout kmAndAdd = new HorizontalLayout();
        // kmAndAdd.setWidth("100%");
        km = new TextField();
        km.setInputPrompt(ui.getMessages().getString(Messages.km_roundtrip));
        km.setWidth("100%");

        Button add = new Button(ui.getMessages().getString(Messages.save),
                new ClickListener() {
                    private static final long serialVersionUID = -4940737576260162546L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        submit();
                    }
                });
        add.setWidth("100%");

        sport.addStyleName("combobox-slim");
        // name.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
        // sport.addStyleName(ChameleonTheme.SELECT_BIG);
        // km.addStyleName(ChameleonTheme.TEXTFIELD_BIG);

        // kmAndAdd.addComponent(km);
        // kmAndAdd.addComponent(add);
        // kmAndAdd.setExpandRatio(add, 1);
        // kmAndAdd.setSpacing(true);
        // kmAndAdd.setComponentAlignment(add, Alignment.MIDDLE_LEFT);

        addComponent(name);
        addComponent(sport);
        addComponent(km);
        addComponent(add);

        if (employee.getName() != null && !employee.getName().equals("")) {
            name.setValue(employee.getName());
        }
        if (employee.getSport() != null) {
            sport.setValue(employee.getSport());
        }
        if (employee.getDistance() != 0d) {
            km.setValue(employee.getDistance() + "");
        }
        addStyleName("new-user-layout");
    }

    public void submit() {
        employee.setName(name.getValue());
        employee.setSport((Sport) sport.getValue());
        try {
            String kmString = km.getValue();
            kmString = kmString.replace(',', '.');
            double kmDouble = Double.parseDouble(kmString);
            if (kmDouble >= 0) {
                employee.setDistance(kmDouble);
                formSubmit.submit(employee);
            }
            // TODO only >0 values message.
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
            // TODO message on failure
        }
    }
}

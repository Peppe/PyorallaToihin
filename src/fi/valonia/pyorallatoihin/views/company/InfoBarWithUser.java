package fi.valonia.pyorallatoihin.views.company;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Employee;

public class InfoBarWithUser extends CssLayout {

    private CompanyScreen companyScreen;
    private Button markTodayButton;
    private final PyorallaToihinRoot root;

    public InfoBarWithUser(PyorallaToihinRoot root,
            CompanyScreen companyScreen, Employee employee) {
        this.root = root;
        this.companyScreen = companyScreen;
        setWidth("1000px");

        ClickListener markClickListener = new ClickListener() {
            public void buttonClick(ClickEvent event) {
                boolean todayMarked = InfoBarWithUser.this.companyScreen
                        .markTodayOnSelectedEmployee();
                todaySelected(todayMarked);
            }
        };
        markTodayButton = new Button(root.getMessages().getString(
                Messages.press_here_to_mark_today), markClickListener);
        markTodayButton.addStyleName("today-button");
        markTodayButton.setWidth("400px");
        markTodayButton.setHeight("110px");
        CssLayout info = new CssLayout();
        info.addStyleName("info-bar-right");
        Label nameLabel = new Label(root.getMessages().getString(Messages.hi)
                + " " + employee.getName());
        nameLabel.addStyleName(ChameleonTheme.LABEL_H2);
        nameLabel.addStyleName("name-label");
        Label descriptionLabel = new Label(root.getMessages().getString(
                Messages.remember_to_bookmark));
        info.addComponent(nameLabel);
        info.addComponent(descriptionLabel);
        info.setWidth("550px");
        addComponent(markTodayButton);
        addComponent(info);
    }

    public void todaySelected(boolean selected) {
        if (selected) {
            markTodayButton.setCaption(root.getMessages().getString(
                    Messages.you_came_today_without_a_motor));
            markTodayButton.addStyleName(ChameleonTheme.BUTTON_DOWN);
        } else {
            markTodayButton.setCaption(root.getMessages().getString(
                    Messages.press_here_to_mark_today));
            markTodayButton.removeStyleName(ChameleonTheme.BUTTON_DOWN);
        }
    }
}

package fi.valonia.pyorallatoihin.views.login;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label.ContentMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.views.Header;

public class LoginScreen extends VerticalLayout {
    private RegisterCompanyForm form;
    private final PyorallaToihinRoot root;

    public LoginScreen(PyorallaToihinRoot root) {
        this.root = root;
        setMargin(true);

        Header header = new Header(root);
        CssLayout spacer = new CssLayout();
        spacer.setHeight("200px");
        VerticalLayout layout = createLayout();
        form = new RegisterCompanyForm(root);
        layout.setWidth("600px");
        form.setWidth("600px");
        form.setVisible(false);
        addComponent(header);
        addComponent(spacer);
        addComponent(layout);
        addComponent(form);
        setComponentAlignment(header, Alignment.TOP_CENTER);
        setComponentAlignment(layout, Alignment.TOP_CENTER);
        setComponentAlignment(form, Alignment.TOP_CENTER);

    }

    private VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Label header = new Label(root.getMessages().getString(
                Messages.log_in_with_token));
        final TextField loginToken = new TextField();
        loginToken.setWidth("100%");
        loginToken.addStyleName("login-token");
        loginToken.focus();
        ClickListener submitButtonPressedListerner = new ClickListener() {
            private Label companyNotFoundMessage;

            public void buttonClick(final ClickEvent event) {
                String token = loginToken.getValue().toUpperCase();
                Company company = ((PyorallaToihinRoot) getRoot())
                        .getCompanyService().findCompany(token);
                if (company != null) {
                    ((PyorallaToihinRoot) getRoot()).showCompany(company);
                } else if (companyNotFoundMessage == null) {
                    companyNotFoundMessage = new Label(root.getMessages()
                            .getString(Messages.company_not_found));
                    layout.addComponent(companyNotFoundMessage, 2);
                }
            }
        };
        Button submit = new Button(root.getMessages()
                .getString(Messages.log_in), submitButtonPressedListerner);
        submit.setClickShortcut(KeyCode.ENTER, null);
        submit.addStyleName("login-submit");
        Label ruler = new Label("<hr />", ContentMode.XHTML);
        ruler.setHeight("16px");
        final Button showRegisterForm = new Button(root.getMessages()
                .getString(Messages.register_workplace_into_competition),
                new ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        form.setVisible(!form.isVisible());
                        if (form.isVisible()) {
                            event.getButton().setIcon(
                                    new ThemeResource("img/arrow_down.png"));
                        } else {
                            event.getButton().setIcon(
                                    new ThemeResource("img/arrow_right.png"));
                        }
                    }
                });
        showRegisterForm.setIcon(new ThemeResource("img/arrow_right.png"));
        showRegisterForm.setStyleName(ChameleonTheme.BUTTON_LINK);
        showRegisterForm.addStyleName("show-company-register-button");
        HorizontalLayout codeAndSubmit = new HorizontalLayout();
        codeAndSubmit.addComponent(loginToken);
        codeAndSubmit.addComponent(submit);
        codeAndSubmit.setHeight(null);
        codeAndSubmit.setWidth("100%");
        codeAndSubmit.setExpandRatio(loginToken, 1);

        CompetitionStats stats = new CompetitionStats();

        layout.addComponent(header);
        layout.addComponent(codeAndSubmit);
        layout.addComponent(stats);
        layout.addComponent(ruler);
        layout.addComponent(showRegisterForm);
        return layout;
    }
}

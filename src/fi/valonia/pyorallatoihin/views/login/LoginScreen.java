package fi.valonia.pyorallatoihin.views.login;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label.ContentMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.components.Spacer;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.views.Header;

public class LoginScreen extends VerticalLayout {
    private static final long serialVersionUID = 3096903961339163587L;

    private final RegisterCompanyForm form;
    private final PyorallaToihinRoot root;

    public LoginScreen(PyorallaToihinRoot root) {
        this.root = root;
        setMargin(true);
        addStyleName("main");
        Header header = new Header(false);
        Spacer spacer = new Spacer(null, "60px");
        HorizontalLayout instructions = createInfoLayout();
        Spacer spacer2 = new Spacer(null, "60px");
        VerticalLayout layout = createLayout();
        form = new RegisterCompanyForm(root);
        layout.setWidth("600px");
        form.setWidth("600px");
        form.setVisible(false);
        addComponent(header);
        addComponent(spacer);
        addComponent(instructions);
        addComponent(spacer2);
        addComponent(layout);
        addComponent(form);

        setComponentAlignment(header, Alignment.TOP_CENTER);
        setComponentAlignment(instructions, Alignment.TOP_CENTER);
        setComponentAlignment(layout, Alignment.TOP_CENTER);
        setComponentAlignment(form, Alignment.TOP_CENTER);

    }

    private VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Label header = new Label(root.getMessages().getString(
                Messages.log_in_with_token));
        final TextField loginToken = new TextField();
        loginToken.setWidth("100%");
        loginToken.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
        loginToken.focus();
        ClickListener submitButtonPressedListerner = new ClickListener() {
            private static final long serialVersionUID = 3944577360961223310L;

            private Label companyNotFoundMessage;

            @Override
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
                    private static final long serialVersionUID = 103040090524830577L;

                    @Override
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

        CompetitionStats stats = new CompetitionStats(root.getCompanyService()
                .getAllCompanyInfos());

        layout.addComponent(header);
        layout.addComponent(codeAndSubmit);
        layout.addComponent(stats);
        layout.addComponent(ruler);
        layout.addComponent(showRegisterForm);
        return layout;
    }

    private HorizontalLayout createInfoLayout() {
        HorizontalLayout instructions = new HorizontalLayout();
        instructions.setWidth("1000px");
        GridLayout contactInfo = createContactInfoLayout();

        Label instructionsLabel = new Label(root.getMessages().getString(
                Messages.competition_info), ContentMode.XHTML);
        instructionsLabel.addStyleName("login-instructions");
        Spacer spacer = new Spacer("60px", null);
        instructions.addComponent(instructionsLabel);
        instructions.addComponent(spacer);
        instructions.addComponent(contactInfo);
        instructions.setComponentAlignment(contactInfo, Alignment.TOP_RIGHT);
        instructions.setExpandRatio(instructionsLabel, 1);
        return instructions;
    }

    private GridLayout createContactInfoLayout() {
        GridLayout contactInfo = new GridLayout(3, 5);
        contactInfo.setSpacing(true);
        contactInfo.setSizeUndefined();
        Label contactInfoLabel = new Label(root.getMessages().getString(
                Messages.contact_person_valonia));
        Label contactNameLabel = new Label(root.getMessages().getString(
                Messages.contact_person_name));
        Label contactPhoneLabel = new Label(root.getMessages().getString(
                Messages.contact_person_phone));
        Label contactEmailLabel = new Label(root.getMessages().getString(
                Messages.contact_person_email));
        Label contactFaxLabel = new Label(root.getMessages().getString(
                Messages.contact_person_fax));
        contactInfoLabel.addStyleName(ChameleonTheme.LABEL_H4);
        contactNameLabel.addStyleName(ChameleonTheme.LABEL_H4);
        contactPhoneLabel.addStyleName(ChameleonTheme.LABEL_H4);
        contactEmailLabel.addStyleName(ChameleonTheme.LABEL_H4);
        contactFaxLabel.addStyleName(ChameleonTheme.LABEL_H4);

        Label contactName = new Label("Paula Väisänen");
        Label contactPhone = new Label("044 907 5986");
        Label contactEmail = new Label("paula.vaisanen@valonia.fi");
        Label contactFax = new Label("02 262 3450");

        contactInfoLabel.setSizeUndefined();
        contactNameLabel.setSizeUndefined();
        contactPhoneLabel.setSizeUndefined();
        contactEmailLabel.setSizeUndefined();
        contactFaxLabel.setSizeUndefined();
        contactName.setSizeUndefined();
        contactPhone.setSizeUndefined();
        contactEmail.setSizeUndefined();
        contactFax.setSizeUndefined();

        Spacer spacer = new Spacer("20px", null);

        contactInfo.addComponent(contactInfoLabel, 0, 0, 2, 0);
        contactInfo.addComponent(contactNameLabel, 0, 1);
        contactInfo.addComponent(contactPhoneLabel, 0, 2);
        contactInfo.addComponent(contactEmailLabel, 0, 3);
        contactInfo.addComponent(contactFaxLabel, 0, 4);

        contactInfo.addComponent(spacer, 1, 1, 1, 4);

        contactInfo.addComponent(contactName, 2, 1);
        contactInfo.addComponent(contactPhone, 2, 2);
        contactInfo.addComponent(contactEmail, 2, 3);
        contactInfo.addComponent(contactFax, 2, 4);

        contactInfo.setComponentAlignment(contactName, Alignment.MIDDLE_RIGHT);
        contactInfo.setComponentAlignment(contactPhone, Alignment.MIDDLE_RIGHT);
        contactInfo.setComponentAlignment(contactEmail, Alignment.MIDDLE_RIGHT);
        contactInfo.setComponentAlignment(contactFax, Alignment.MIDDLE_RIGHT);
        return contactInfo;
    }
}

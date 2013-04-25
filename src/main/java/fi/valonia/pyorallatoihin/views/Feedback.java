package fi.valonia.pyorallatoihin.views;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Link;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinUI;

@SuppressWarnings("serial")
public class Feedback extends CssLayout {

    public Feedback() {
        setStyleName("feedback");
        Link vaadin = new Link(null, new ExternalResource("http://vaadin.com"));
        vaadin.setIcon(new ThemeResource("img/vaadin.png"));
        Messages messages = PyorallaToihinUI.get().getMessages();
        Link jens = new Link(messages.getString(Messages.implementation)
                + " Jens Jansson", new ExternalResource(
                "http://jensjansson.com"));
        Link feedback = new Link(messages.getString(Messages.feedback),
                new ExternalResource("http://pyorallatoihin.uservoice.com/"));
        addComponent(vaadin);
        addComponent(jens);
        addComponent(feedback);
    }
}

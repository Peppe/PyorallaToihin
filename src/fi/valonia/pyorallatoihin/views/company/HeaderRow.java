package fi.valonia.pyorallatoihin.views.company;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label.ContentMode;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;

public class HeaderRow extends CssLayout {
    private static final long serialVersionUID = -585710224663561070L;

    public HeaderRow(PyorallaToihinRoot root, Date startDate, Locale locale) {
        setStyleName("header-row");
        Label name = new Label(root.getMessages().getString(Messages.name));
        Label sport = new Label(root.getMessages().getString(Messages.type));
        Label km = new Label(root.getMessages().getString(Messages.km));
        name.addStyleName("name");
        sport.addStyleName("sport");
        km.addStyleName("km-label");
        SimpleDateFormat weekDay = new SimpleDateFormat("EE", locale);
        SimpleDateFormat date = new SimpleDateFormat("dd.MM", locale);
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        Label day1 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day2 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day3 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day4 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day5 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day6 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day7 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        c.add(Calendar.DATE, 1);
        Label day8 = new Label(weekDay.format(c.getTime()) + "<br />"
                + date.format(c.getTime()), ContentMode.XHTML);
        Label kmTotal = new Label(root.getMessages().getString(
                Messages.km_total));
        kmTotal.setStyleName("km-total");

        name.setWidth(null);
        sport.setWidth(null);
        km.setWidth(null);
        day1.setWidth(null);
        day2.setWidth(null);
        day3.setWidth(null);
        day4.setWidth(null);
        day5.setWidth(null);
        day6.setWidth(null);
        day7.setWidth(null);
        day8.setWidth(null);
        kmTotal.setWidth("90px");

        day1.setStyleName("center-label day");
        day2.setStyleName("center-label day");
        day3.setStyleName("center-label day");
        day4.setStyleName("center-label day");
        day5.setStyleName("center-label day");
        day6.setStyleName("center-label day");
        day7.setStyleName("center-label day");
        day8.setStyleName("center-label day");

        name.addStyleName(ChameleonTheme.LABEL_H4);
        sport.addStyleName(ChameleonTheme.LABEL_H4);
        km.addStyleName(ChameleonTheme.LABEL_H4);
        day1.addStyleName(ChameleonTheme.LABEL_H4);
        day2.addStyleName(ChameleonTheme.LABEL_H4);
        day3.addStyleName(ChameleonTheme.LABEL_H4);
        day4.addStyleName(ChameleonTheme.LABEL_H4);
        day5.addStyleName(ChameleonTheme.LABEL_H4);
        day6.addStyleName(ChameleonTheme.LABEL_H4);
        day7.addStyleName(ChameleonTheme.LABEL_H4);
        day8.addStyleName(ChameleonTheme.LABEL_H4);
        kmTotal.addStyleName(ChameleonTheme.LABEL_H4);

        addComponent(name);
        addComponent(sport);
        addComponent(km);
        addComponent(day1);
        addComponent(day2);
        addComponent(day3);
        addComponent(day4);
        addComponent(day5);
        addComponent(day6);
        addComponent(day7);
        addComponent(day8);
        addComponent(kmTotal);
    }
}

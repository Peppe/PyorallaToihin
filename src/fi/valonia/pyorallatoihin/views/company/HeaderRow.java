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
        name.setSizeUndefined();
        Label sport = new Label(root.getMessages().getString(Messages.type));
        Label km = new Label(root.getMessages().getString(Messages.km));
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

        name.setWidth("150px");
        sport.setWidth("100px");
        km.setWidth("40px");
        day1.setWidth("75px");
        day2.setWidth("75px");
        day3.setWidth("75px");
        day4.setWidth("75px");
        day5.setWidth("75px");
        day6.setWidth("75px");
        day7.setWidth("75px");
        day8.setWidth("75px");
        kmTotal.setWidth("90px");

        day1.setStyleName("center-label");
        day2.setStyleName("center-label");
        day3.setStyleName("center-label");
        day4.setStyleName("center-label");
        day5.setStyleName("center-label");
        day6.setStyleName("center-label");
        day7.setStyleName("center-label");
        day8.setStyleName("center-label");

        km.setStyleName("km-label");
        kmTotal.setStyleName("km-label");

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

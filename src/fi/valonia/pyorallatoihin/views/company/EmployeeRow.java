package fi.valonia.pyorallatoihin.views.company;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label.ContentMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.components.Spacer;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Season;
import fi.valonia.pyorallatoihin.interfaces.ISystemService;
import fi.valonia.pyorallatoihin.views.company.UserForm.FormSubmit;

public class EmployeeRow extends CssLayout {
    private static final long serialVersionUID = 408469953129640391L;

    private final Employee employee;
    private final CompanyScreen companyScreen;
    private final Button[] buttons = new Button[8];
    private CssLayout controls;

    ClickListener clickListener = new ClickListener() {
        private static final long serialVersionUID = -3633715053483874698L;

        @Override
        public void buttonClick(ClickEvent event) {
            int day = (Integer) event.getButton().getData();
            toggleDay(day);
        }
    };
    private Label kmTotal;

    public EmployeeRow(Employee employee, CompanyScreen companyScreen) {
        this.employee = employee;
        this.companyScreen = companyScreen;
        setStyleName("employee-row");

        addListener(new LayoutClickListener() {
            private static final long serialVersionUID = -3589060577170678837L;

            @Override
            public void layoutClick(LayoutClickEvent event) {
                if (event.getClickedComponent() instanceof Button) {
                    return;
                }
                EmployeeRow.this.companyScreen
                        .selectEmployeeRow(EmployeeRow.this);
            }
        });
        paintRow();
    }

    private void paintRow() {
        CssLayout row = new CssLayout();
        row.addStyleName("employee-row-row");
        row.setWidth("1000px");
        Label name = new Label(employee.getName());
        name.setSizeUndefined();
        name.addStyleName("name");
        Label sport = new Label(((PyorallaToihinRoot) Root.getCurrentRoot())
                .getMessages().getString(employee.getSport().toString()));
        Label km = new Label(String.valueOf(employee.getDistance()));
        km.setStyleName("km-label");

        name.setWidth("150px");
        sport.setWidth("100px");
        km.setWidth("40px");

        row.addComponent(name);
        row.addComponent(sport);
        row.addComponent(km);

        for (int i = 0; i < 8; i++) {
            Button button = new Button(null, clickListener);
            button.setData(i);
            button.addStyleName((employee.getDays()[i]) ? "yes" : "no");
            button.setWidth("75px");
            row.addComponent(button);
            buttons[i] = button;
        }
        kmTotal = new Label();
        kmTotal.setWidth("90px");
        kmTotal.setStyleName("km-label");
        updateTotal(false);
        row.addComponent(kmTotal);
        addComponent(row);
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean toggleToday() {
        int today = getTodayNumber();
        if (today >= 0 && today <= 7) {
            toggleDay(today);
            return employee.getDays()[today];
        }
        return false;
    }

    /**
     * toggles the state of the day
     * 
     * @param day
     *            0 to 7 (0 is first day)
     */
    private boolean toggleDay(int day) {
        PyorallaToihinRoot root = (PyorallaToihinRoot) Root.getCurrentRoot();
        employee.getDays()[day] = !employee.getDays()[day];
        root.getCompanyService().updateEmployeeDays(employee);
        // removeAllComponents();
        // paintRow();
        int daysMarked = 0;
        for (boolean b : employee.getDays()) {
            if (b) {
                daysMarked++;
            }
        }
        if (daysMarked > 5 && employee.getDays()[day]) {
            root.showNotification(
                    root.getMessages().getString(
                            Messages.max_five_days_to_stats),
                    Notification.TYPE_TRAY_NOTIFICATION);
        }
        if (getTodayNumber() == day) {
            companyScreen.todayToggled();
        }
        if (employee.getDays()[day]) {
            buttons[day].setStyleName("yes");
        } else {
            buttons[day].setStyleName("no");
        }
        updateTotal(true);
        return employee.getDays()[day];
    }

    public boolean isTodayMarked() {
        int today = getTodayNumber();
        if (today >= 0 && today <= 7) {
            return employee.getDays()[today];
        } else {
            return false;
        }
    }

    private int getTodayNumber() {
        Date currentDate = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(currentDate);
        ISystemService systemService = ((PyorallaToihinRoot) Root
                .getCurrentRoot()).getSystemService();
        Season currentSeason = systemService.getCurrentSeason();
        Date startDate = currentSeason.getStartDate();
        int day = (int) (currentDate.getTime() - startDate.getTime())
                / (1000 * 60 * 60 * 24);
        return day;
    }

    public void updateTotal(boolean notify) {
        double total = 0;
        boolean[] days = employee.getDays();
        for (int i = 0; i < 8; i++) {
            if (days[i] == true) {
                total++;
            }
        }
        if (total > 5) {
            total = 5;
        }
        kmTotal.setValue(String.valueOf(new BigDecimal(total)
                .multiply(new BigDecimal(employee.getDistance()))
                .setScale(1, RoundingMode.HALF_UP).toString()));
        if (notify) {
            companyScreen.updateStats();
        }
    }

    public void select() {
        addStyleName("selected");
        if (controls == null) {
            controls = new CssLayout();
            controls.setWidth("60px");
            controls.addStyleName("employee-controls");
            controls.addComponent(createDeleteButton());
            controls.addComponent(createModifyButton());
            addComponent(controls);
        }
    }

    private Button createDeleteButton() {

        // TODO icon
        Button button = new Button();
        button.setIcon(new ThemeResource("img/Delete.png"));
        button.setStyleName(ChameleonTheme.BUTTON_BORDERLESS);
        // TODO translate
        button.setDescription("Poista");
        button.setWidth("24px");
        button.setHeight("24px");
        button.addListener(new ClickListener() {
            private static final long serialVersionUID = -5457045290015477705L;

            @Override
            public void buttonClick(ClickEvent event) {
                VerticalLayout layout = new VerticalLayout();
                final Window window = new Window(
                        "Poista " + employee.getName(), layout);
                // TODO localize
                Label label = new Label(
                        "Oletko varma että halueat poistaa käyttäjän <b>"
                                + employee.getName() + "</b>",
                        ContentMode.XHTML);
                HorizontalLayout buttons = new HorizontalLayout();
                Button accept = new Button("Hyväksy", new ClickListener() {
                    private static final long serialVersionUID = -5255880234005606761L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        PyorallaToihinRoot root = (PyorallaToihinRoot) Root
                                .getCurrentRoot();
                        root.getCompanyService().deleteEmployee(employee);
                        companyScreen.employeeDeleted(EmployeeRow.this);
                        root.removeWindow(window);
                    }
                });
                Button decline = new Button("Peruuta", new ClickListener() {
                    private static final long serialVersionUID = 4460770398255567444L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        Root.getCurrentRoot().removeWindow(window);

                    }
                });
                buttons.addComponent(accept);
                buttons.addComponent(decline);
                buttons.setSpacing(true);
                layout.addComponent(label);
                layout.addComponent(new Spacer(null, "40px"));
                layout.addComponent(buttons);
                layout.setMargin(true);
                window.setModal(true);
                window.setWidth("400px");
                Root.getCurrentRoot().addWindow(window);
            }
        });
        return button;
    }

    private Button createModifyButton() {

        // TODO icon
        Button button = new Button();
        button.setIcon(new ThemeResource("img/Modify.png"));
        button.setStyleName(ChameleonTheme.BUTTON_BORDERLESS);
        // TODO translate
        button.setDescription("Muokkaa");
        button.setWidth("24px");
        button.setHeight("24px");
        button.addListener(new ClickListener() {
            private static final long serialVersionUID = -5457045290015477705L;

            @Override
            public void buttonClick(ClickEvent event) {
                final Window window = new Window("Muokkaa "
                        + employee.getName());

                FormSubmit formSubmit = new FormSubmit() {
                    @Override
                    public void submit(Employee employee) {
                        companyScreen.updateEmployeeDetails(employee);
                        window.close();
                    }
                };
                final UserForm newUserLayout = new UserForm(employee,
                        formSubmit);
                newUserLayout.setMargin(true);
                window.setContent(newUserLayout);
                window.addShortcutListener(new ShortcutListener(null,
                        KeyCode.ESCAPE, null) {
                    private static final long serialVersionUID = -7666402117367991543L;

                    @Override
                    public void handleAction(Object sender, Object target) {
                        window.close();
                    }
                });
                window.addShortcutListener(new ShortcutListener(null,
                        KeyCode.ENTER, null) {
                    private static final long serialVersionUID = -4974329151498727901L;

                    @Override
                    public void handleAction(Object sender, Object target) {
                        newUserLayout.submit();
                    }
                });
                window.setModal(true);
                Root.getCurrentRoot().addWindow(window);
                window.setWidth(null);
                window.setHeight("150px");
                window.focus();
            }
        });
        return button;
    }

    public void unselect() {
        removeStyleName("selected");
        if (controls != null) {
            removeComponent(controls);
            controls = null;
        }
    }
}

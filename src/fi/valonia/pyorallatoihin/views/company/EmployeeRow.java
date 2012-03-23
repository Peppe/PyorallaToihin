package fi.valonia.pyorallatoihin.views.company;

import java.util.Calendar;
import java.util.Date;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Season;
import fi.valonia.pyorallatoihin.interfaces.ISystemService;

public class EmployeeRow extends CssLayout {

    private final PyorallaToihinRoot root;
    private final Company company;
    private final Employee employee;
    private final CompanyScreen companyScreen;
    private final Button[] buttons = new Button[8];

    ClickListener clickListener = new ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            int day = (Integer) event.getButton().getData();
            toggleDay(day);
        }
    };
    private Label kmTotal;

    public EmployeeRow(PyorallaToihinRoot root, Company company,
            Employee employee, CompanyScreen companyScreen) {
        this.root = root;
        this.company = company;
        this.employee = employee;
        this.companyScreen = companyScreen;
        setStyleName("employee-row");

        addListener(new LayoutClickListener() {

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
        Label name = new Label(employee.getName());
        name.setSizeUndefined();
        Label sport = new Label(root.getMessages().getString(
                employee.getSport().toString()));
        Label km = new Label(String.valueOf(employee.getDistance()));
        km.setStyleName("km-label");

        name.setWidth("150px");
        sport.setWidth("100px");
        km.setWidth("40px");

        addComponent(name);
        addComponent(sport);
        addComponent(km);

        for (int i = 0; i < 8; i++) {
            Button button = new Button(null, clickListener);
            button.setData(i);
            button.addStyleName((employee.getDays()[i]) ? "yes" : "no");
            button.setWidth("75px");
            addComponent(button);
            buttons[i] = button;
        }
        kmTotal = new Label();
        kmTotal.setWidth("90px");
        kmTotal.setStyleName("km-label");
        updateTotal();
        addComponent(kmTotal);
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean toggleToday() {
        int today = getTodayNumber();
        if (today >= 0 && today <= 7) {
            toggleDay(today);
        }
        return employee.getDays()[today];
    }

    /**
     * toggles the state of the day
     * 
     * @param day
     *            0 to 7 (0 is first day)
     */
    private boolean toggleDay(int day) {

        employee.getDays()[day] = !employee.getDays()[day];
        root.getCompanyService().updateEmployee(company, employee);
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
        updateTotal();
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
        ISystemService systemService = root.getSystemService();
        Season currentSeason = systemService.getCurrentSeason();
        Date startDate = currentSeason.getStartDate();
        int day = (int) (currentDate.getTime() - startDate.getTime())
                / (1000 * 60 * 60 * 24);
        return day;
    }

    public void updateTotal() {
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
        kmTotal.setValue(String.valueOf(total * employee.getDistance()));
        companyScreen.updateStats();
    }
}

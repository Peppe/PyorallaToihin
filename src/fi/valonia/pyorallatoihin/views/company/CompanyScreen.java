package fi.valonia.pyorallatoihin.views.company;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Root;

import fi.valonia.pyorallatoihin.PyorallaToihinRoot;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Season;
import fi.valonia.pyorallatoihin.interfaces.EmployeeExistsInCompanyException;
import fi.valonia.pyorallatoihin.views.Header;

public class CompanyScreen extends CssLayout {
    private static final long serialVersionUID = 7374548785322979632L;

    private final PyorallaToihinRoot root;
    private Company company;
    private CssLayout table;
    private EmployeeRow selected;
    private final List<EmployeeRow> employees = new ArrayList<EmployeeRow>();
    private Component infoBar;
    private CssLayout layout;
    private CompanyStats stats;

    public CompanyScreen(PyorallaToihinRoot root, Company company) {
        this.root = root;
        this.company = company;

        setMargin(true);
        Header header = new Header(root);
        CssLayout layout = createLayout();
        addComponent(header);
        addComponent(layout);
        // setComponentAlignment(header, Alignment.TOP_CENTER);
        // setComponentAlignment(layout, Alignment.TOP_CENTER);
        populateEmployeeTable();
    }

    public CompanyScreen(PyorallaToihinRoot root, Company company,
            int employeeId) {
        this(root, company);
        selectEmployeeId(employeeId);
    }

    private CssLayout createLayout() {
        layout = new CssLayout();
        layout.setWidth("1000px");
        layout.addStyleName("company-layout");
        Label companyName = new Label(company.getName() + " - "
                + Root.getCurrentRoot().getApplication().getURL() + "#/"
                + company.getToken());
        companyName.addStyleName("company-name");
        stats = new CompanyStats(company);
        infoBar = new InfoBarNoUserSelected(root, this);

        CssLayout spacer = new CssLayout();
        spacer.setHeight("20px");

        table = new CssLayout();
        table.setStyleName("employee-table");
        table.setWidth("1000px");

        layout.addComponent(companyName);
        layout.addComponent(stats);
        layout.addComponent(infoBar);
        layout.addComponent(spacer);
        layout.addComponent(table);
        return layout;
    }

    public void populateEmployeeTable() {
        employees.clear();
        table.removeAllComponents();
        Season currentSeason = root.getSystemService().getCurrentSeason();
        table.addComponent(new HeaderRow(root, currentSeason.getStartDate(),
                root.getLocale()));
        for (Employee employee : company.getEmployees()) {
            EmployeeRow row = new EmployeeRow(root, company, employee, this);
            table.addComponent(row);
            employees.add(row);
        }
        table.setVisible(company.getEmployees().size() > 0);
    }

    public void addEmployee(Employee employee) {
        try {
            root.getCompanyService().addEmployee(company, employee);
            company = root.getCompanyService().findCompany(company.getToken());
            populateEmployeeTable();
            selectEmployee(employee);
        } catch (EmployeeExistsInCompanyException e) {
            e.printStackTrace();
        }
    }

    public void selectEmployee(Employee employee) {
        for (EmployeeRow row : employees) {
            if (row.getEmployee().equals(employee)) {
                selectEmployeeRow(row);
                break;
            }
        }
    }

    private void selectEmployeeId(int employeeId) {
        for (EmployeeRow row : employees) {
            if (row.getEmployee().getId() == employeeId) {
                selectEmployeeRow(row);
                break;
            }
        }
    }

    public void selectEmployeeRow(EmployeeRow row) {
        if (selected != null) {
            selected.removeStyleName("selected");
        }
        if (row != null && row != selected) {
            row.addStyleName("selected");
            InfoBarWithUser infoBarWithUser = new InfoBarWithUser(root, this,
                    row.getEmployee());
            infoBarWithUser.todaySelected(row.isTodayMarked());
            layout.replaceComponent(infoBar, infoBarWithUser);
            infoBar = infoBarWithUser;
            selected = row;
            root.setUserId(row.getEmployee().getId());
        } else {
            InfoBarNoUserSelected infoBarNoUser = new InfoBarNoUserSelected(
                    root, this);
            layout.replaceComponent(infoBar, infoBarNoUser);
            infoBar = infoBarNoUser;
            selected = null;
            root.setUserId(-1);
        }
    }

    public boolean markTodayOnSelectedEmployee() {
        if (selected != null) {
            return selected.toggleToday();
        }
        return false;
    }

    public void todayToggled() {
        if (infoBar instanceof InfoBarWithUser) {
            ((InfoBarWithUser) infoBar).todaySelected(selected.isTodayMarked());
        }
    }

    public void updateStats() {
        company = root.getCompanyService().findCompany(company.getToken());
        CompanyStats stats = null;
        stats = new CompanyStats(company);
        layout.replaceComponent(this.stats, stats);
        this.stats = stats;
    }

    public Company getCompany() {
        return company;
    }
}
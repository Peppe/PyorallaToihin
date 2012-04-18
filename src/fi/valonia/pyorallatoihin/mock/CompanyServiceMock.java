package fi.valonia.pyorallatoihin.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.CompanyInfo;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Sport;
import fi.valonia.pyorallatoihin.interfaces.CompanyNameInUseException;
import fi.valonia.pyorallatoihin.interfaces.EmployeeExistsInCompanyException;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

public class CompanyServiceMock implements ICompanyService, Serializable {
    private static final long serialVersionUID = 6463127444016673402L;

    @Override
    public Company findCompany(String token) {
        if (token == null) {
            return null;
        }
        if (token.toUpperCase().equals("AAAAA")) {
            return createFakeCompany();
        }
        return null;
    }

    @Override
    public void createCompany(Company company) throws CompanyNameInUseException {
        if (company.getName().equals("abc")) {
            throw new CompanyNameInUseException();
        }
        company.setToken("AAAAA");
    }

    private Company createFakeCompany() {
        Company company = new Company();
        company.setName("example company");
        company.setToken("AAAAA");
        company.setSeasonId(1);
        company.setSize(12);
        company.setStreetAddress("street 1");
        company.setZip("12345");
        company.setCity("Turku");

        company.setContactName("Maija");
        company.setContactEmail("maija@maija.maija");
        company.setContactPhone("+1234324234234");

        Random generator = new Random();
        int employees = generator.nextInt(10) + 5;
        for (int i = 0; i < employees; i++) {
            Employee employee = new Employee();
            employee.setId(i);
            employee.setName("Erin Tihkunen");
            employee.setDistance(generator.nextInt(10) + 1);
            employee.setSport(Sport.BICYCLE);
            employee.getDays()[0] = generator.nextBoolean();
            employee.getDays()[1] = generator.nextBoolean();
            employee.getDays()[2] = generator.nextBoolean();
            company.getEmployees().add(employee);
        }
        return company;
    }

    @Override
    public void addEmployee(Company company, Employee employee)
            throws EmployeeExistsInCompanyException {
        for (Employee compEmployee : company.getEmployees()) {
            if (employee.getName().equals(compEmployee.getName())) {
                throw new EmployeeExistsInCompanyException();
            }
        }
        company.getEmployees().add(employee);
        employee.setId(company.getEmployees().size());
    }

    @Override
    public void updateEmployee(Company company, Employee employee) {
        // No need to do anything in the mock as everything is referenced.
    }

    @Override
    public List<CompanyInfo> getAllCompanyInfos() {
        List<CompanyInfo> info = new ArrayList<CompanyInfo>();
        CompanyInfo company = new CompanyInfo();
        company.setName("Yritys");
        company.setId(1);
        company.setSize(20);
        company.setRegistered(8);
        company.setTotalMarkers(15);
        return info;
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<Company>();
        companies.add(createFakeCompany());
        return companies;
    }
}

package fi.valonia.pyorallatoihin.mock;

import java.io.Serializable;
import java.util.Random;

import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.Company.CompanySize;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Sport;
import fi.valonia.pyorallatoihin.interfaces.CompanyNameInUseException;
import fi.valonia.pyorallatoihin.interfaces.EmployeeExistsInCompanyException;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

public class CompanyServiceMock implements ICompanyService, Serializable {

    public Company findCompany(String token) {
        if (token == null) {
            return null;
        }
        if (token.toUpperCase().equals("AAAAA")) {
            return createFakeCompany();
        }
        return null;
    }

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
        company.setSize(CompanySize.S5_20);
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

    public void updateEmployee(Company company, Employee employee) {
        // No need to do anything in the mock as everything is referenced.
    }
}

package fi.valonia.pyorallatoihin.interfaces;

import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.Employee;

public interface ICompanyService {

    /**
     * Takes in a generated five letter token which was created along with an
     * company and returns that company. Case is always switched to upper case
     * no matter what the input token is in, so 'AAaaA' is equal to 'AAAAA'.
     * 
     * @param token
     * @return Company object if found of null if not found.
     */
    public Company findCompany(String token);

    /**
     * Takes in a company object, checks if the company name is already in use
     * or not. If it is in use, the method twos an CompanyNameInUseException.
     * Otherwise it generates a new unique five letter token, puts it into
     * company.setToken(token) and stores the company. It also mails to the
     * company contact person's email address with an url directly to the
     * company page.
     * 
     * @param company
     * @return The same company object with the token set.
     * @throws CompanyNameInUseException
     */
    public void createCompany(Company company) throws CompanyNameInUseException;

    /**
     * Adds an employee to the company. Takes in an Company and Employee object,
     * stores the Employee to the company and generates an id to the employee.
     * 
     * @param company
     * @throws EmployeeExistsInCompanyException
     */
    public void addEmployee(Company company, Employee employee)
            throws EmployeeExistsInCompanyException;

    public void updateEmployee(Company company, Employee employee);
}

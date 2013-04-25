package fi.valonia.pyorallatoihin.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import fi.valonia.pyorallatoihin.PyorallaToihinUI;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.CompanyInfo;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Sport;
import fi.valonia.pyorallatoihin.interfaces.CompanyNameInUseException;
import fi.valonia.pyorallatoihin.interfaces.EmployeeExistsInCompanyException;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

public class CompanyService implements ICompanyService {

    // private final JdbcConnectionPool cp;

    private static final long serialVersionUID = 9186438590017092618L;

    public CompanyService() {
        // cp = JdbcConnectionPool.create("jdbc:h2:~/pyorallatoihin", "sa", "");
    }

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    private int getSeasonId() {
        return ((PyorallaToihinUI) PyorallaToihinUI.get()).getSystemService()
                .getCurrentSeason().getId();
    }

    @Override
    public Company findCompany(String token) {
        if (token == null || token.equals("")) {
            return null;
        }
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = getConnection();
            String SQL = "SELECT * FROM Company WHERE TOKEN=? AND SEASON_ID=?";
            PreparedStatement companyStatement = conn.prepareStatement(SQL);
            companyStatement.setString(1, token);
            companyStatement.setInt(2, getSeasonId());
            ResultSet set = companyStatement.executeQuery();
            Company company = null;
            if (set != null && set.next()) {
                company = convertResultSetToCopmany(set);
            }
            if (company != null) {
                SQL = "SELECT * FROM EMPLOYEE WHERE COMPANY_ID=?";
                PreparedStatement employeeStatement = conn
                        .prepareStatement(SQL);
                employeeStatement.setInt(1, company.getId());
                set = employeeStatement.executeQuery();
                List<Employee> employees = convertResultSetToEmployees(set);
                company.setEmployees(employees);
            }
            return company;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
        return null;
    }

    private Company convertResultSetToCopmany(ResultSet set)
            throws SQLException {
        if (set != null) {
            Company company = new Company();
            company.setId(set.getInt("ID"));
            company.setSeasonId(set.getInt("SEASON_ID"));
            company.setToken(set.getString("TOKEN"));
            company.setName(set.getString("NAME"));
            company.setSize(set.getInt("SIZE"));
            company.setStreetAddress(set.getString("STREET"));
            company.setZip(set.getString("ZIP"));
            company.setCity(set.getString("CITY"));

            company.setContactName(set.getString("CONTACT_NAME"));
            company.setContactEmail(set.getString("CONTACT_EMAIL"));
            company.setContactPhone(set.getString("CONTACT_PHONE"));
            company.setFirstTime(set.getBoolean("FIRST_TIME"));
            company.setHeardFrom(set.getString("HEARD_FROM"));
            return company;
        }
        return null;
    }

    private List<Employee> convertResultSetToEmployees(ResultSet set)
            throws SQLException {
        if (set != null) {
            List<Employee> employees = new ArrayList<Employee>();
            while (set.next()) {
                Employee employee = new Employee();
                employee.setId(set.getInt("ID"));
                employee.setName(set.getString("NAME"));
                Sport sport = Sport.stringToSport(set.getString("SPORT"));
                employee.setSport(sport);
                employee.setDistance(set.getDouble("DISTANCE"));
                for (int i = 1; i <= 8; i++) {
                    employee.getDays()[i - 1] = set.getBoolean("DAY" + i);
                }
                employees.add(employee);
            }
            return employees;
        }
        return null;
    }

    @Override
    public void createCompany(Company company) throws CompanyNameInUseException {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            int seasonId = getSeasonId();
            conn = getConnection();
            String token = generateString();
            company.setToken(token);
            String SQL = "INSERT INTO Company (SEASON_ID, TOKEN, NAME, SIZE, STREET, ZIP, CITY, CONTACT_NAME, CONTACT_EMAIL, CONTACT_PHONE, FIRST_TIME, HEARD_FROM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, seasonId);
            stmt.setString(2, company.getToken());
            stmt.setString(3, company.getName());
            stmt.setInt(4, company.getSize());
            stmt.setString(5, company.getStreetAddress());
            stmt.setString(6, company.getZip());
            stmt.setString(7, company.getCity());
            stmt.setString(8, company.getContactName());
            stmt.setString(9, company.getContactEmail());
            stmt.setString(10, company.getContactPhone());
            stmt.setBoolean(11, company.isFirstTime());
            stmt.setString(12, company.getHeardFrom());
            stmt.execute();
            SQL = "SELECT * FROM COMPANY WHERE TOKEN=? AND SEASON_ID=?";
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, token);
            stmt.setInt(2, seasonId);
            ResultSet set = stmt.executeQuery();
            set.next();
            company.setId(set.getInt("ID"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
    }

    public static String generateString() {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] text = new char[5];
        for (int i = 0; i < 5; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

    @Override
    public void addEmployee(Company company, Employee employee)
            throws EmployeeExistsInCompanyException {
        // TODO Auto-generated method stub
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = getConnection();
            String SQL = "INSERT INTO Employee  (NAME , COMPANY_ID, SPORT, DISTANCE) values (?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, employee.getName());
            stmt.setInt(2, company.getId());
            stmt.setString(3, employee.getSport().getStringRepresentation());
            stmt.setDouble(4, employee.getDistance());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
    }

    @Override
    public void updateEmployeeDetails(Employee employee) {
        Connection conn = null;
        try {
            conn = getConnection();
            String SQL = "UPDATE Employee SET NAME=?, SPORT=?, DISTANCE=? WHERE ID=?";
            PreparedStatement companyStatement = conn.prepareStatement(SQL);
            companyStatement.setString(1, employee.getName());
            companyStatement.setString(2, employee.getSport()
                    .getStringRepresentation());
            companyStatement.setDouble(3, employee.getDistance());
            companyStatement.setInt(4, employee.getId());
            companyStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
    }

    @Override
    public void updateEmployeeDays(Employee employee) {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = getConnection();
            Statement stmt = conn.createStatement();
            boolean day1 = employee.getDays()[0];
            boolean day2 = employee.getDays()[1];
            boolean day3 = employee.getDays()[2];
            boolean day4 = employee.getDays()[3];
            boolean day5 = employee.getDays()[4];
            boolean day6 = employee.getDays()[5];
            boolean day7 = employee.getDays()[6];
            boolean day8 = employee.getDays()[7];

            String SQL = "UPDATE Employee SET day1=" + day1 + ", day2=" + day2
                    + ", day3=" + day3 + ", day4=" + day4 + ", day5=" + day5
                    + ", day6=" + day6 + ", day7=" + day7 + ", day8=" + day8
                    + " where ID=" + employee.getId() + ";";
            System.out.println("query: " + SQL);
            stmt.execute(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
    }

    @Override
    public void deleteEmployee(Employee employee) {
        Connection conn = null;
        try {
            conn = getConnection();
            String SQL = "DELETE FROM Employee WHERE ID=?";
            PreparedStatement companyStatement = conn.prepareStatement(SQL);
            companyStatement.setInt(1, employee.getId());
            companyStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
    }

    @Override
    public List<CompanyInfo> getAllCompanyInfos() {
        Connection conn = null;
        try {
            int seasonId = getSeasonId();
            // conn = cp.getConnection();
            conn = getConnection();
            Statement stmt = conn.createStatement();
            String innerQuery = "SELECT company.id, company.name, company.size, count(*) AS REGISTERED, "
                    + "count(nullif(employee.day1, 0)) + "
                    + "count(nullif(employee.day2, 0)) + "
                    + "count(nullif(employee.day3, 0)) + "
                    + "count(nullif(employee.day4, 0)) + "
                    + "count(nullif(employee.day5, 0)) + "
                    + "count(nullif(employee.day6, 0)) + "
                    + "count(nullif(employee.day7, 0)) + "
                    + "count(nullif(employee.day8, 0)) AS TOTALMARKERS"
                    + " FROM company"
                    + " LEFT JOIN employee"
                    + " ON company.id=employee.company_id"
                    + " WHERE SEASON_ID ="
                    + seasonId
                    + " GROUP BY company.id ";
            String SQL = "SELECT id, name, size, registered, totalmarkers, round(totalmarkers*1.0/size, 2) as RATIO"
                    + " FROM (" + innerQuery + ")"
                    + " ORDER by ratio desc "
                    + " LIMIT 5;";
            System.out.println("query: " + SQL);
            ResultSet set = stmt.executeQuery(SQL);
            List<CompanyInfo> companies = new ArrayList<CompanyInfo>();
            while (set.next()) {
                CompanyInfo company = new CompanyInfo();
                company.setId(set.getInt("ID"));
                company.setName(set.getString("NAME"));
                company.setSize(set.getInt("SIZE"));
                company.setRegistered(set.getInt("REGISTERED"));
                company.setTotalMarkers(set.getInt("TOTALMARKERS"));
                company.setRatio(set.getDouble("RATIO"));
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
        return null;
    }

    @Override
    public List<Company> getAllCompanies() {
        return getAllCompanies(-1, -1);
    }

    @Override
    public List<Company> getAllCompanies(int minSize, int maxSize) {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            int seasonId = getSeasonId();
            conn = getConnection();
            PreparedStatement stmt = null;
            if (minSize != -1 && maxSize != -1) {
                String SQL = "SELECT * FROM Company WHERE SIZE >= ? AND SIZE <= ? AND SEASON_ID=?";
                stmt = conn.prepareStatement(SQL);
                stmt.setInt(1, minSize);
                stmt.setInt(2, maxSize);
                stmt.setInt(3, seasonId);
            } else if (minSize != -1 && maxSize == -1) {
                String SQL = "SELECT * FROM Company WHERE SIZE >= ? AND SEASON_ID=?";
                stmt = conn.prepareStatement(SQL);
                stmt.setInt(1, minSize);
                stmt.setInt(2, seasonId);
            } else if (minSize == -1 && maxSize != -1) {
                String SQL = "SELECT * FROM Company WHERE SIZE <= ? AND SEASON_ID=?";
                stmt = conn.prepareStatement(SQL);
                stmt.setInt(1, maxSize);
                stmt.setInt(2, seasonId);
            } else {
                String SQL = "SELECT * FROM Company WHERE SEASON_ID=?";
                stmt = conn.prepareStatement(SQL);
                stmt.setInt(1, seasonId);
            }
            stmt.toString();
            ResultSet set = stmt.executeQuery();
            List<Company> companies = new ArrayList<Company>();
            while (set.next()) {
                Company company = convertResultSetToCopmany(set);
                if (company != null) {
                    String SQL = "SELECT * FROM EMPLOYEE WHERE COMPANY_ID=?";
                    stmt = conn.prepareStatement(SQL);
                    stmt.setInt(1, company.getId());
                    ResultSet employeeSet = stmt.executeQuery();
                    List<Employee> employees = convertResultSetToEmployees(employeeSet);
                    company.setEmployees(employees);
                }
                companies.add(company);
            }
            return companies;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
        return null;
    }
}

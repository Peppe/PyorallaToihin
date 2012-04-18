package fi.valonia.pyorallatoihin.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class CompanyService implements ICompanyService {

    // private final JdbcConnectionPool cp;

    public CompanyService() {
        // cp = JdbcConnectionPool.create("jdbc:h2:~/pyorallatoihin", "sa", "");
    }

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
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
            String SQL = "SELECT * FROM Company WHERE TOKEN= ?";
            PreparedStatement companyStatement = conn.prepareStatement(SQL);
            companyStatement.setString(1, token);
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

                Sport sport = Sport.BICYCLE;
                String sportString = set.getString("SPORT");
                if ("BICYCLE".equals(sportString)) {
                    sport = Sport.BICYCLE;
                } else if ("WALKING".equals(sportString)) {
                    sport = Sport.WALKING;
                } else if ("KICK_SCOOTER".equals(sportString)) {
                    sport = Sport.KICK_SCOOTER;
                } else if ("ROLLER_BLADES".equals(sportString)) {
                    sport = Sport.ROLLER_BLADES;
                } else if ("ROWING".equals(sportString)) {
                    sport = Sport.ROWING;
                } else if ("WITH_HORSE".equals(sportString)) {
                    sport = Sport.WITH_HORSE;
                } else if ("WITH_DOG_SLED".equals(sportString)) {
                    sport = Sport.WITH_DOG_SLED;
                } else if ("OTHER".equals(sportString)) {
                    sport = Sport.OTHER;
                }
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
            conn = getConnection();

            String token = generateString();
            company.setToken(token);
            String SQL = "INSERT INTO Company (SEASON_ID, TOKEN, NAME, SIZE, STREET, ZIP, CITY, CONTACT_NAME, CONTACT_EMAIL, CONTACT_PHONE, FIRST_TIME, HEARD_FROM) values (1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, company.getToken());
            stmt.setString(2, company.getName());
            stmt.setInt(3, company.getSize());
            stmt.setString(4, company.getStreetAddress());
            stmt.setString(5, company.getZip());
            stmt.setString(6, company.getCity());
            stmt.setString(7, company.getContactName());
            stmt.setString(8, company.getContactEmail());
            stmt.setString(9, company.getContactPhone());
            stmt.setBoolean(10, company.isFirstTime());
            stmt.setString(11, company.getHeardFrom());
            stmt.execute();
            SQL = "SELECT * FROM COMPANY WHERE TOKEN='" + token + "'";
            ResultSet set = stmt.executeQuery(SQL);
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
            Statement stmt = conn.createStatement();
            String name = employee.getName();
            int company_id = company.getId();
            double distance = employee.getDistance();
            String sport = "BICYCLE";
            if (employee.getSport() == Sport.BICYCLE) {
                sport = "BICYCLE";
            } else if (employee.getSport() == Sport.WALKING) {
                sport = "WALKING";
            } else if (employee.getSport() == Sport.KICK_SCOOTER) {
                sport = "KICK_SCOOTER";
            } else if (employee.getSport() == Sport.ROLLER_BLADES) {
                sport = "ROLLER_BLADES";
            } else if (employee.getSport() == Sport.ROWING) {
                sport = "ROWING";
            } else if (employee.getSport() == Sport.WITH_HORSE) {
                sport = "WITH_HORSE";
            } else if (employee.getSport() == Sport.WITH_DOG_SLED) {
                sport = "WITH_DOG_SLED";
            } else if (employee.getSport() == Sport.OTHER) {
                sport = "OTHER";
            }

            String SQL = "INSERT INTO Employee  (NAME , COMPANY_ID, SPORT, DISTANCE) values ('"
                    + name
                    + "', "
                    + company_id
                    + ", '"
                    + sport
                    + "', "
                    + distance + ");";
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
    public void updateEmployee(Company company, Employee employee) {
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
    public List<CompanyInfo> getAllCompanyInfos() {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = getConnection();
            Statement stmt = conn.createStatement();
            String SQL = "SELECT company.id, company.name, company.size, count(*) AS REGISTERED,"
                    + "count(nullif(employee.day1, 0)) + "
                    + "count(nullif(employee.day2, 0)) + "
                    + "count(nullif(employee.day3, 0)) + "
                    + "count(nullif(employee.day4, 0)) + "
                    + "count(nullif(employee.day5, 0)) + "
                    + "count(nullif(employee.day6, 0)) + "
                    + "count(nullif(employee.day7, 0)) + "
                    + "count(nullif(employee.day8, 0)) AS TOTALMARKERS "
                    + "FROM company "
                    + "LEFT JOIN employee "
                    + "ON company.id=employee.company_id "
                    + "group by company.id "
                    + "order by totalmarkers desc "
                    + "limit 5;";
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
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = getConnection();
            Statement stmt = conn.createStatement();
            String SQL = "SELECT * FROM Company";
            ResultSet set = stmt.executeQuery(SQL);
            List<Company> companies = new ArrayList<Company>();
            while (set.next()) {
                Company company = convertResultSetToCopmany(set);
                if (company != null) {
                    SQL = "SELECT * FROM EMPLOYEE WHERE COMPANY_ID='"
                            + company.getId() + "'";
                    set = stmt.executeQuery(SQL);
                    List<Employee> employees = convertResultSetToEmployees(set);
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

package fi.valonia.pyorallatoihin.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.data.Company.CompanySize;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Sport;
import fi.valonia.pyorallatoihin.interfaces.CompanyNameInUseException;
import fi.valonia.pyorallatoihin.interfaces.EmployeeExistsInCompanyException;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;

public class CompanyService implements ICompanyService {

    public Company findCompany(String token) {
        if (token == null || token.equals("")) {
            return null;
        }
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement stmt = conn.createStatement();
            String SQL = "SELECT * FROM Company WHERE TOKEN='" + token + "'";
            ResultSet set = stmt.executeQuery(SQL);
            Company company = convertResultSetToCopmany(set);
            if (company != null) {
                SQL = "SELECT * FROM EMPLOYEE WHERE COMPANY_ID='"
                        + company.getId() + "'";
                set = stmt.executeQuery(SQL);
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
        if (set != null && set.next()) {
            Company company = new Company();
            company.setId(set.getInt("ID"));
            company.setSeasonId(set.getInt("SEASON_ID"));
            company.setToken(set.getString("TOKEN"));
            company.setName(set.getString("NAME"));
            CompanySize size;
            switch (set.getInt("SIZE")) {
            case 1: {
                size = CompanySize.S1_4;
                break;
            }
            case 2: {
                size = CompanySize.S5_20;
                break;
            }
            case 3: {
                size = CompanySize.S21_100;
                break;
            }
            case 4: {
                size = CompanySize.S_OVER_100;
                break;
            }
            default: {
                size = CompanySize.S1_4;
            }
            }
            company.setSize(size);
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
                employee.setDistance(set.getInt("DISTANCE"));
                for (int i = 1; i <= 8; i++) {
                    employee.getDays()[i - 1] = set.getBoolean("DAY" + i);
                }
                employees.add(employee);
            }
            return employees;
        }
        return null;
    }

    public void createCompany(Company company) throws CompanyNameInUseException {
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement stmt = conn.createStatement();
            String token = generateString();
            company.setToken(token);
            String name = company.getName();
            String street = company.getStreetAddress();
            String zip = company.getZip();
            String city = company.getCity();
            String cp_name = company.getContactName();
            String cp_email = company.getContactEmail();
            String cp_phone = company.getContactPhone();
            int size = -1;
            if (company.getSize() == CompanySize.S1_4) {
                size = 1;
            } else if (company.getSize() == CompanySize.S5_20) {
                size = 2;
            } else if (company.getSize() == CompanySize.S21_100) {
                size = 3;
            } else if (company.getSize() == CompanySize.S_OVER_100) {
                size = 4;
            }

            String SQL = "INSERT INTO Company (SEASON_ID, TOKEN, NAME, SIZE, STREET, ZIP, CITY, CONTACT_NAME, CONTACT_EMAIL, CONTACT_PHONE) values (1, '"
                    + token
                    + "', '"
                    + name
                    + "', "
                    + size
                    + ", '"
                    + street
                    + "', '"
                    + zip
                    + "', '"
                    + city
                    + "', '"
                    + cp_name
                    + "', '"
                    + cp_email + "', '" + cp_phone + "')";
            stmt.execute(SQL);
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

    public void addEmployee(Company company, Employee employee)
            throws EmployeeExistsInCompanyException {
        // TODO Auto-generated method stub
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement stmt = conn.createStatement();
            String name = employee.getName();
            int company_id = company.getId();
            double distance = employee.getDistance();
            boolean day1 = employee.getDays()[0];
            boolean day2 = employee.getDays()[1];
            boolean day3 = employee.getDays()[2];
            boolean day4 = employee.getDays()[3];
            boolean day5 = employee.getDays()[4];
            boolean day6 = employee.getDays()[5];
            boolean day7 = employee.getDays()[6];
            boolean day8 = employee.getDays()[7];
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
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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

    public void updateEmployee(Company company, Employee employee) {
        // TODO Auto-generated method stub
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement stmt = conn.createStatement();
            String name = employee.getName();
            int company_id = company.getId();
            double distance = employee.getDistance();
            boolean day1 = employee.getDays()[0];
            boolean day2 = employee.getDays()[1];
            boolean day3 = employee.getDays()[2];
            boolean day4 = employee.getDays()[3];
            boolean day5 = employee.getDays()[4];
            boolean day6 = employee.getDays()[5];
            boolean day7 = employee.getDays()[6];
            boolean day8 = employee.getDays()[7];
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

            String SQL = "UPDATE Employee SET day1=" + day1 + ", day2=" + day2
                    + ", day3=" + day3 + ", day4=" + day4 + ", day5=" + day5
                    + ", day6=" + day6 + ", day7=" + day7 + ", day8=" + day8
                    + " where ID=" + employee.getId() + ";";
            System.out.println("query: " + SQL);
            stmt.execute(SQL);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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

}

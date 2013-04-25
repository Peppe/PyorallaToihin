package fi.valonia.pyorallatoihin.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Company implements Serializable {
    private static final long serialVersionUID = -8303292229895712730L;
    private int id;
    private int seasonId;
    private String token;
    private String name = "";
    private int size = 0;
    private String streetAddress = "";
    private String zip = "";
    private String city = "";

    private String contactName = "";
    private String contactEmail = "";
    private String contactPhone = "";

    private boolean firstTime = false;
    private String heardFrom = "";

    private List<Employee> employees = new ArrayList<Employee>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getHeardFrom() {
        return heardFrom;
    }

    public void setHeardFrom(String heardFrom) {
        this.heardFrom = heardFrom;
    }

    public int getTotalMarkers() {
        int totalMarkers = 0;
        for (Employee employee : employees) {
            totalMarkers = totalMarkers + employee.getTotalMarkers();
        }
        return totalMarkers;
    }

    public double getTotalKm() {
        double totalKm = 0;
        for (Employee employee : employees) {

            totalKm = totalKm
                    + (employee.getDistance() * employee.getTotalMarkers());
        }
        double roundedTotal = Math.round(totalKm * 100) / 100d;
        return roundedTotal;
    }

}

package fi.valonia.pyorallatoihin.data;

public class CompanyInfo {

    private int id;
    private String name;
    private int size;
    private int registered;
    private int totalMarkers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public int getTotalMarkers() {
        return totalMarkers;
    }

    public void setTotalMarkers(int totalMarkers) {
        this.totalMarkers = totalMarkers;
    }

}

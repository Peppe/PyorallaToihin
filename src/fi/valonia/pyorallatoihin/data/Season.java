package fi.valonia.pyorallatoihin.data;

import java.io.Serializable;
import java.util.Date;

public class Season implements Serializable {

    private int id;
    private String name;
    private Date startDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

}

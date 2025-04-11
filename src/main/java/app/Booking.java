package app;

import java.util.UUID;

public class Booking {
    private String uuid;
    private String customerName;
    private int tableSize;
    private String date;
    private String time;

    public Booking() {
    }

    public String getUuid() {
        return uuid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTableSize() {
        return tableSize;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isValid() {
        return customerName != null && !customerName.isEmpty()
                && tableSize > 0 && date != null && time != null;
    }

    //Setters
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

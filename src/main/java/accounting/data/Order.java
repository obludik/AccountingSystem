package accounting.data;

import java.util.Date;

/**
 * Transfer object for sending and receiving a order JSON data from/to client.
 */
public class Order {

    private String referenceNumber;
    private String currency;
    private Date date;

    public Order(String referenceNumber, String currency, Date date) {
        this.referenceNumber = referenceNumber;
        this.currency = currency;
        this.date = date;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

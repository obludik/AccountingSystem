package accounting.data;

/**
 * Transfer object for sending and receiving a order products JSON data from/to client.
 */
public class OrderItem {

    private String productCode;
    private Integer productQuantity;
    private Integer productUnitPrice;

    public OrderItem(String productCode, Integer productQuantity, Integer productUnitPrice) {
        this.productCode = productCode;
        this.productQuantity = productQuantity;
        this.productUnitPrice = productUnitPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(Integer productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }
}

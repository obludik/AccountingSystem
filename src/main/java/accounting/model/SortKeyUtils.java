package accounting.model;

/**
 * Utility methods for construction value for {@link CustomerOrderId#getTypeKey()} column
 * in the {@link CustomerOrder} table.
 */
public class SortKeyUtils {

    public static final String DELIMITER = "#";

    /**
     * Returns a sort key value for search of customer data in the {@link CustomerOrder} table.
     *
     * @param customerId customer Id
     * @return sort key value
     */
    public static String createIdForCustomerData(String customerId) {
        return RecordType.CUSTOMER_DATA + DELIMITER + customerId;
    }

    /**
     * Returns a sort key value for search of order data in the {@link CustomerOrder} table.
     *
     * @param orderReferenceNumber order reference number
     * @return sort key value
     */
    public static String createIdForOrder(String orderReferenceNumber) {
        return RecordType.ORDER_DATA + DELIMITER + orderReferenceNumber;
    }

    /**
     * Returns a sort key value for search of order product data in the {@link CustomerOrder} table.
     *
     * @param orderReferenceNumber order reference number
     * @param productCode          order product code
     * @return sort key value
     */
    public static String createIdForOrderItem(String orderReferenceNumber, String productCode) {
        return RecordType.ORDER_ITEM + DELIMITER + orderReferenceNumber + DELIMITER + productCode;
    }

    /**
     * Returns a start of sort key value for search of all data connected to an order in the {@link CustomerOrder} table.
     *
     * @param orderReferenceNumber order reference number
     * @return start of sort key value
     */
    public static String createStartOfIdForOrderItem(String orderReferenceNumber) {
        return RecordType.ORDER_ITEM + DELIMITER + orderReferenceNumber + DELIMITER;
    }
}

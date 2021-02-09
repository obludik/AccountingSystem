package accounting.model;

/**
 * Type of record in {@link CustomerOrder} database table.
 * Types - customer data, order data or order product data.
 */
public enum RecordType {
    CUSTOMER_DATA,
    ORDER_DATA,
    ORDER_ITEM
}

package accounting.service;

import accounting.AccountingSystemTestDBSetup;
import accounting.data.Customer;
import accounting.data.Order;
import accounting.data.OrderItem;
import accounting.model.CustomerOrder;
import accounting.model.CustomerOrderId;
import accounting.model.RecordType;
import accounting.model.SortKeyUtils;
import accounting.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * Returns the list of customer's orders.
     *
     * @param customerId customer ID
     * @return the list of customer's orders
     */
    public List<CustomerOrder> getCustomerOrders(String customerId) {
        return customerOrderRepository.findByCustomerIdAndTypeKeyStartsWith(customerId, RecordType.ORDER_DATA.toString());
    }

    /**
     * Returns data about a customer's order.
     *
     * @param customerId customer ID
     * @param refNum     order reference number
     * @return data about a customer's order
     */
    public CustomerOrder getOrder(String customerId, String refNum) {
        Optional<CustomerOrder> order = customerOrderRepository.findById(
                new CustomerOrderId(customerId, SortKeyUtils.createIdForOrder(refNum)));
        return order.orElse(null);
    }

    /**
     * Saves order data.
     *
     * @param customerId customer ID
     * @param order      order data
     * @return reference number
     */
    public String saveOrder(String customerId, Order order) {
        CustomerOrder newOrder = new CustomerOrder();
        newOrder.setOrderData(customerId, order.getDate(), order.getCurrency(), order.getReferenceNumber());
        customerOrderRepository.save(newOrder);
        return newOrder.getOrderReferenceNumber();
    }

    /**
     * Updates order data.
     *
     * @param customerId customer ID
     * @param order      order data
     * @return true/false success/not found
     */
    public boolean updateOrder(String customerId, Order order) {
        if (getOrder(customerId, order.getReferenceNumber()) == null) {
            return false;
        }
        CustomerOrder newOrder = new CustomerOrder();
        newOrder.setOrderData(customerId, order.getDate(), order.getCurrency(), order.getReferenceNumber());
        customerOrderRepository.save(newOrder);
        return true;
    }

    /**
     * Deletes order and order's products.
     *
     * @param customerId customer ID
     * @param refNum     order reference number
     * @return true/false success/not found
     */
    public boolean deleteOrder(String customerId, String refNum) {
        if (getOrder(customerId, refNum) == null) {
            return false;
        }
        // delete order items
        customerOrderRepository.deleteByCustomerIdAndTypeKeyStartsWith(customerId,
                SortKeyUtils.createStartOfIdForOrderItem(refNum));
        // delete order
        customerOrderRepository.deleteById(new CustomerOrderId(customerId, SortKeyUtils.createIdForOrder(refNum)));
        return true;
    }
}

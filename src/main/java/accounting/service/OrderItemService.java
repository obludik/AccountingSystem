package accounting.service;

import accounting.data.OrderItem;
import accounting.model.CustomerOrder;
import accounting.model.CustomerOrderId;
import accounting.model.RecordType;
import accounting.model.SortKeyUtils;
import accounting.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderService orderService;

    /**
     * Returns list of order's products.
     *
     * @param customerId customer ID
     * @param refNum     order reference number
     * @return list of order's products
     */
    public List<CustomerOrder> getOrderItems(String customerId, String refNum) {
        return customerOrderRepository.findByCustomerIdAndTypeKeyStartsWith(
                customerId, RecordType.ORDER_ITEM.toString());
    }

    /**
     * Returns order's product by code.
     *
     * @param customerId customer ID
     * @param refNum     order reference number
     * @param code       product code
     * @return product item or null
     */
    public CustomerOrder getOrderItem(String customerId, String refNum, String code) {
        Optional<CustomerOrder> orderItem = customerOrderRepository.findById(
                new CustomerOrderId(customerId, SortKeyUtils.createIdForOrderItem(refNum, code)));
        return orderItem.orElse(null);
    }

    /**
     * Saves a order's product.
     *
     * @param customerId customer ID
     * @param refNum     order reference number
     * @param orderItem  data about product
     * @return order reference number
     * @return true/false order success/not found
     */
    public boolean saveOrderItem(String customerId, String refNum, OrderItem orderItem) {
        if (orderService.getOrder(customerId, refNum) == null) {
            return false;
        }
        CustomerOrder newOrder = new CustomerOrder();
        newOrder.setOrderItemData(customerId, refNum, orderItem.getProductCode(), orderItem.getProductQuantity(),
                orderItem.getProductUnitPrice());
        customerOrderRepository.save(newOrder);
        return true;
    }

    /**
     * Update a order's product.
     *
     * @param customerId  customer ID
     * @param refNum      order reference number
     * @param productCode product code
     * @param orderItem   data about product
     * @return true/false success/not found
     */
    public boolean updateOrderItem(String customerId, String refNum, String productCode, OrderItem orderItem) {
        if (getOrderItem(customerId, refNum, productCode) == null) {
            return false;
        }
        CustomerOrder newOrder = new CustomerOrder();
        newOrder.setOrderItemData(customerId, refNum, productCode, orderItem.getProductQuantity(),
                orderItem.getProductUnitPrice());
        customerOrderRepository.save(newOrder);
        return true;
    }

    /**
     * Deletes a order's product.
     *
     * @param customerId  customer ID
     * @param refNum      order reference number
     * @param productCode product code
     * @return true/false success/not found
     */
    public boolean deleteOrderItem(String customerId, String refNum, String productCode) {
        if (getOrderItem(customerId, refNum, productCode) == null) {
            return false;
        }
        customerOrderRepository.deleteById(new CustomerOrderId(customerId, SortKeyUtils.createIdForOrderItem(refNum, productCode)));
        return true;
    }
}

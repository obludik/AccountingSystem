package accounting.service;

import accounting.data.Customer;
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
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    /**
     * Returns the list of all customers.
     *
     * @return the list of all customers
     */
    public List<CustomerOrder> getAllCustomers() {
        Iterable<CustomerOrder> customerList = customerOrderRepository.findByTypeKeyStartsWith(
                RecordType.CUSTOMER_DATA.toString());
        List<CustomerOrder> customers = new ArrayList<>();
        customerList.forEach(customers::add);
        return customers;
    }

    /**
     * Returns customer by ID.
     *
     * @param customerId customer ID
     * @return customer data
     */
    public CustomerOrder getCustomer(String customerId) {
        Optional<CustomerOrder> customer = customerOrderRepository.findById(
                new CustomerOrderId(customerId, SortKeyUtils.createIdForCustomerData(customerId)));
        if (customer.isPresent()) {
            return customer.get();
        } else {
            return null;
        }
    }

    /**
     * Returns orders of a customer by ID.
     *
     * @param customerId customer ID
     * @return orders of a customer
     */
    public List<CustomerOrder> getCustomerOrders(String customerId) {
        return customerOrderRepository.findByCustomerIdAndTypeKeyStartsWith(customerId, RecordType.ORDER_DATA.toString());
    }

    /**
     * Saves customer data.
     *
     * @param customer customer data
     * @return customer ID
     */
    public String saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID().toString());
        }
        CustomerOrder cust = new CustomerOrder();
        cust.setCustomerData(customer.getId(), customer.getName(), customer.getAddress());
        customerOrderRepository.save(cust);
        return cust.getCustomerId();
    }

    /**
     * Updates customer data.
     *
     * @param customer customer data
     * @return true/false success/not found
     */
    public boolean updateCustomer(Customer customer) {
        if (getCustomer(customer.getId()) == null) {
            return false;
        }
        CustomerOrder cust = new CustomerOrder();
        cust.setCustomerData(customer.getId(), customer.getName(), customer.getAddress());
        customerOrderRepository.save(cust);
        return true;
    }

    /**
     * Delete all data about customer.
     *
     * @param customerId customer ID
     * @return true/false success/not found
     */
    public boolean deleteCustomer(String customerId) {
        if (getCustomer(customerId) == null) {
            return false;
        }
        customerOrderRepository.deleteByCustomerId(customerId);
        return true;
    }
}

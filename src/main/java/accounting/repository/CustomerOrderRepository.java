package accounting.repository;

import accounting.model.CustomerOrder;
import accounting.model.CustomerOrderId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface using Spring CRUD Repository for defining methods to read, update and delete data in the
 * {@link CustomerOrder} database table.
 */
@EnableScan
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, CustomerOrderId> {

    /**
     * Finds customer data by ID.
     *
     * @param customerOrderId customer composite ID
     * @return customer data
     */
    Optional<CustomerOrder> findById(CustomerOrderId customerOrderId);

    /**
     * Finds data by start of sort key.
     *
     * @param typeKey start of sort key
     * @return customer order data
     */
    List<CustomerOrder> findByTypeKeyStartsWith(String typeKey);

    /**
     * Finds data by customer ID and start of sort key.
     *
     * @param customerId customer ID
     * @param typeKey    start of sort key
     * @return list of customer order data
     */
    List<CustomerOrder> findByCustomerIdAndTypeKeyStartsWith(String customerId, String typeKey);

    /**
     * Deletes all customer data and orders.
     *
     * @param customerId customer ID
     */
    void deleteByCustomerId(String customerId);

    /**
     * Deletes data by customer ID and start of sort key.
     *
     * @param customerId customer ID
     * @param typeKey    start of sort key
     */
    void deleteByCustomerIdAndTypeKeyStartsWith(String customerId, String typeKey);
}
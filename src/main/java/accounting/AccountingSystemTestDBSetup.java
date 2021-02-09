package accounting;

import accounting.model.CustomerOrder;
import accounting.repository.CustomerOrderRepository;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts test Dynamo DB server locally. Inserts test data.
 */
@Component
public class AccountingSystemTestDBSetup implements CommandLineRunner {

    private static final Logger LOG = Logger.getLogger(AccountingSystemTestDBSetup.class.getName());

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    private DynamoDBProxyServer server;

    @Value("${amazon.dynamodb.localDBPort}")
    private String amazonDynamoDBPort;

    @Value("${amazon.dynamodb.startLocalDB}")
    private boolean startLocalDB;

    @Value("${nativeLibs.path}")
    private String nativeLibsPath;

    @Override
    public void run(String... args) {
        if (startLocalDB) {
            // Local Dynamo DB start and settings
            LOG.info("Starting Dynamo DB server...");
            System.setProperty("sqlite4java.library.path", nativeLibsPath);
            try {
                server = ServerRunner.createServerFromCommandLineArgs(
                        new String[]{"-inMemory", "-port", amazonDynamoDBPort});
                server.start();
                createTestData();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Cannot create database.", e);
            }
        } else {
            LOG.info("Starting Local Dynamo DB server is disabled...");
        }
    }

    @PreDestroy
    public void closeDynamoDBServer() {
        try {
            LOG.info("Stopping Dynamo DB server...");
            server.stop();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot stop database.", e);
        }
    }

    /**
     * Creates database table and insert test data.
     */
    public void createTestData() {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(CustomerOrder.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

        createCustomerData("customer1", "Test customer", "Široká 15, Člunek");
        String customerId = "customer2";
        String orderNum = "ref-2054558";
        createCustomerData(customerId, "Test customer 2", "Hladká 78, Olomouc");
        createOrderData(customerId, "203", "ref-20213658");
        createOrderData(customerId, "203", orderNum);
        createOrderItem(customerId, orderNum, "454566-89", 4, 4100);
        createOrderItem(customerId, orderNum, "454566-8999", 1, 48800);
    }

    /**
     * Inserts data about order to the database.
     *
     * @param customerId customer Id
     * @param currency   currency
     * @param refNum     order reference number
     */
    public void createOrderData(String customerId, String currency, String refNum) {
        CustomerOrder custOrder = new CustomerOrder();
        custOrder.setOrderData(customerId, new Date(), currency, refNum);
        customerOrderRepository.save(custOrder);
    }

    /**
     * Inserts data about customer to the database.
     *
     * @param customerId customer Id
     * @param name       customer name
     * @param address    customer address
     */
    public void createCustomerData(String customerId, String name, String address) {
        CustomerOrder cust = new CustomerOrder();
        cust.setCustomerData(customerId, name, address);
        customerOrderRepository.save(cust);
    }

    /**
     * Inserts data about order's product to the database.
     *
     * @param customerId  customer Id
     * @param refNum      order reference number
     * @param productCode product code
     * @param quantity    product quantity
     * @param unitPrice   product unit price
     */
    public void createOrderItem(String customerId, String refNum, String productCode, Integer quantity,
                                Integer unitPrice) {
        CustomerOrder custOrderItem = new CustomerOrder();
        custOrderItem.setOrderItemData(customerId, refNum, productCode, quantity, unitPrice);
        customerOrderRepository.save(custOrderItem);
    }
}

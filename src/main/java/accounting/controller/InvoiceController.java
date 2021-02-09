package accounting.controller;

import accounting.data.*;
import accounting.model.CustomerOrder;
import accounting.service.CustomerService;
import accounting.service.OrderItemService;
import accounting.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing invoices with GET method with returns data for a invoice.
 */
@RestController
public class InvoiceController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/invoices/{customerId}/{refNum}")
    public ResponseEntity<Object> getDataForInvoice(@PathVariable String customerId, @PathVariable String refNum) {
        InvoiceData invoice = new InvoiceData();
        CustomerOrder customer = customerService.getCustomer(customerId);
        if (customer == null) {
            return new ResponseEntity<>(new Result(ResponseDescription.CUSTOMER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        invoice.setCustomer(new Customer(customer.getCustomerId(), customer.getName(), customer.getAddress()));
        CustomerOrder order = orderService.getOrder(customerId, refNum);
        if (order == null) {
            return new ResponseEntity<>(new Result(ResponseDescription.ORDER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        invoice.setOrder(new Order(order.getOrderReferenceNumber(), order.getOrderCurrency(), order.getOrderDate()));
        List<CustomerOrder> items = orderItemService.getOrderItems(customerId, refNum);
        invoice.setOrderItems(items.stream().map(i -> new OrderItem(i.getProductCode(), i.getProductQuantity(),
                i.getProductUnitPrice())).collect(Collectors.toList()));
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
}

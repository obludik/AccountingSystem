package accounting.controller;

import accounting.data.Order;
import accounting.data.ResponseDescription;
import accounting.data.Result;
import accounting.model.CustomerOrder;
import accounting.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing orders of customers with CRUD methods.
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/{customerId}")
    public List<Order> getCustomerOrders(@PathVariable String customerId) {
        List<CustomerOrder> orders = orderService.getCustomerOrders(customerId);
        return orders.stream().map(i -> new Order(i.getOrderReferenceNumber(),
                i.getOrderCurrency(), i.getOrderDate())).collect(Collectors.toList());
    }

    @GetMapping("/orders/{customerId}/{refNum}")
    public Order getCustomerOrder(@PathVariable String customerId, @PathVariable String refNum) {
        CustomerOrder order = orderService.getOrder(customerId, refNum);
        return order != null ? new Order(order.getOrderReferenceNumber(),
                order.getOrderCurrency(), order.getOrderDate()) : null;
    }

    @PutMapping
    @RequestMapping(value = "/orders/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Object createCustomerOrder(@PathVariable String customerId,
                                      @RequestBody Order order) {
        orderService.saveOrder(customerId, order);
        return order;
    }

    @PutMapping(value = "/orders/{customerId}/{refNum}")
    public ResponseEntity<Object> updateCustomerOrder(@PathVariable String customerId,
                                                      @RequestBody Order order) {
        if (!orderService.updateOrder(customerId, order)) {
            return new ResponseEntity<>(new Result(ResponseDescription.ORDER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.OK);
    }


    @DeleteMapping(value = "/orders/{customerId}/{refNum}")
    public ResponseEntity<Object> deleteCustomerOrder(@PathVariable String customerId,
                                                      @PathVariable String refNum) {
        if (!orderService.deleteOrder(customerId, refNum)) {
            return new ResponseEntity<>(new Result(ResponseDescription.ORDER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.OK);
    }

}

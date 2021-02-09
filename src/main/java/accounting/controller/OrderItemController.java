package accounting.controller;

import accounting.data.OrderItem;
import accounting.data.ResponseDescription;
import accounting.data.Result;
import accounting.model.CustomerOrder;
import accounting.service.OrderItemService;
import accounting.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing products connected to orders with CRUD methods.
 */
@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/orderItems/{customerId}/{refNum}")
    public List<OrderItem> getOrderItems(@PathVariable String customerId, @PathVariable String refNum) {
        List<CustomerOrder> items = orderItemService.getOrderItems(customerId, refNum);
        return items.stream().map(i -> new OrderItem(i.getProductCode(),
                i.getProductQuantity(), i.getProductUnitPrice())).collect(Collectors.toList());
    }

    @PutMapping
    @RequestMapping(value = "/orderItems/{customerId}/{refNum}")
    public ResponseEntity<Object> addOrderItem(@PathVariable String customerId, @PathVariable String refNum,
                               @RequestBody OrderItem orderItem) {
        if (!orderItemService.saveOrderItem(customerId, refNum, orderItem)) {
            return new ResponseEntity<>(new Result(ResponseDescription.ORDER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.CREATED);
    }

    @PutMapping(value = "/orderItems/{customerId}/{refNum}/{productCode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateOrderItem(@PathVariable String customerId, @PathVariable String refNum,
                                                  @PathVariable String productCode,
                                                  @RequestBody OrderItem orderItem) {
        if (!orderItemService.updateOrderItem(customerId, refNum, productCode, orderItem)) {
            return new ResponseEntity<>(new Result(ResponseDescription.ORDER_ITEM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.OK);
    }


    @DeleteMapping(value = "/orderItems/{customerId}/{refNum}/{productCode}")
    public ResponseEntity<Object> deleteOrderItem(@PathVariable String customerId,
                                                  @PathVariable String refNum,
                                                  @PathVariable String productCode) {
        if (!orderItemService.deleteOrderItem(customerId, refNum, productCode)) {
            return new ResponseEntity<>(new Result(ResponseDescription.ORDER_ITEM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.OK);
    }

}

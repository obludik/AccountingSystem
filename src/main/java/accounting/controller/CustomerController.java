package accounting.controller;

import accounting.data.Customer;
import accounting.data.ResponseDescription;
import accounting.data.Result;
import accounting.model.CustomerOrder;
import accounting.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing customers with CRUD methods.
 */
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        List<CustomerOrder> customers = customerService.getAllCustomers();
        return customers.stream().map(i -> new Customer(i.getCustomerId(),
                i.getName(), i.getAddress())).collect(Collectors.toList());
    }

    @GetMapping("/customers/{id}")
    public Object getCustomer(@PathVariable String id) {
        CustomerOrder customer = customerService.getCustomer(id);
        return customer != null ? new Customer(id, customer.getName(), customer.getAddress()) :
                new Result(ResponseDescription.CUSTOMER_NOT_FOUND);
    }

    @PutMapping
    @RequestMapping(value = "/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Object createCustomer(@RequestBody Customer customer){
        String id = customerService.saveCustomer(customer);
        customer.setId(id);
        return customer;
    }

    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable(value = "id") String id,
                                 @RequestBody Customer customer){
        customer.setId(id);
        if (!customerService.updateCustomer(customer)) {
            return new ResponseEntity<>(new Result(ResponseDescription.CUSTOMER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.OK);
    }

    @DeleteMapping(value = "/customers/{id}")
    public ResponseEntity<Object> deleteCustomerWithOrders(@PathVariable(value = "id") String id){
        if (!customerService.deleteCustomer(id)) {
            return new ResponseEntity<>(new Result(ResponseDescription.CUSTOMER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Result(ResponseDescription.OK), HttpStatus.OK);
    }
}

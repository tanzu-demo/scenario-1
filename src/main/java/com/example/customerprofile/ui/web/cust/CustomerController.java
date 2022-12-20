package com.example.customerprofile.ui.web.cust;

import com.example.customerprofile.ui.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class CustomerController {

    @Autowired
    ApplicationProperties properties;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/protected/customers")
    public String customers(Model model) {
        String host = properties.getServiceHost();
        try {
            Customer[] customers = restTemplate.getForObject(String.format("%s/api/customer-profiles/", host), Customer[].class);
            model.addAttribute("customers", customers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "protected/customers.html";
    }

    @GetMapping("/protected/customers-add")
    public String newCustomers(Model model) {
        model.addAttribute("customer", new Customer());
        return "protected/customers-add.html";
    }

    @PostMapping("/protected/customers-add")
    public String addUser(Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        String host = properties.getServiceHost();
        restTemplate.postForObject(String.format("%s/api/customer-profiles/", host), customer, Customer.class);
        return "redirect:/protected/customers";
    }

    @GetMapping("/protected/customers-edit/{id}")
    public String editCustomer(@PathVariable("id") String id, Model model) {
        String host = properties.getServiceHost();
        Customer customer = new Customer();
        try {
            customer = restTemplate.getForObject(String.format("%s/api/customer-profiles/%s", host, id), Customer.class);
            model.addAttribute("customer", customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("customer", customer);
        return "protected/customers-edit.html";
    }

    @PostMapping("/protected/customers-edit/{id}")
    public String editCustomer(Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "customers";
        }

        String host = properties.getServiceHost();
        restTemplate.patchForObject(String.format("%s/api/customer-profiles/%s", host, customer.getId()), customer, Customer.class);
        return "redirect:/protected/customers";
    }

    @GetMapping("/protected/customers-delete/{id}")
    public String deleteCustomer(@PathVariable("id") String id, Model model) {
        String host = properties.getServiceHost();
        Customer customer = new Customer();
        try {
            customer = restTemplate.getForObject(String.format("%s/api/customer-profiles/%s", host, id), Customer.class);
            model.addAttribute("customer", customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("customer", customer);
        return "protected/customers-delete.html";
    }

    @PostMapping(value = "/protected/customers-delete/{id}", params = "action=submit")
    public String deleteCustomer(Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "customers";
        }

        String host = properties.getServiceHost();
        restTemplate.delete(String.format("%s/api/customer-profiles/%s", host, customer.getId()));
        return "redirect:/protected/customers";
    }

    @PostMapping(value = "/protected/customers-delete/{id}", params = "action=cancel")
    public String cancelDeleteCustomer(Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "customers";
        }
        return "redirect:/protected/customers";
    }
}

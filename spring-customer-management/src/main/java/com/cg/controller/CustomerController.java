package com.cg.controller;

import com.cg.model.Customer;
import com.cg.service.CustomerService;
import com.cg.service.ICustomerService;
import com.cg.utils.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard/customers")
public class CustomerController {
    private final ICustomerService customerService = new CustomerService();

    @GetMapping
    public ModelAndView showList(){
        List<Customer> customers = customerService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        modelAndView.addObject("customers",customers);

        return modelAndView;
    }

    @GetMapping("/info/{id}")
    public String showCustomerInfo(@PathVariable Long id, Model model){
        Customer customer = customerService.findById(id);
        model.addAttribute("customer",customer);
        return "customer/info";
    }

    @GetMapping("/create")
    public String showCreate(Model model){
        Customer customer = new Customer();
        Map<String,String> errorsMap = new HashMap<>();

        model.addAttribute("customer",customer);
        model.addAttribute("errorsMap",errorsMap);

        return "customer/create";
    }

    @PostMapping("/create")
    public String doCreate(RedirectAttributes redirectAttributes,
                           @ModelAttribute Customer customer,
                           Model model){
        Map<String,String> errorsMap = new HashMap<>();
        validateInfo(customer, errorsMap);

        if (errorsMap.isEmpty()){
            Long maxId = 1L;
            for (Customer customer1: customerService.findAll()){
                if (customer1.getId() > maxId){
                    maxId = customer1.getId();
                }
            }
            customer.setId(maxId+1);
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("createMess","Thêm thành công!");
            return "redirect:/dashboard/customers";
        }else {
            model.addAttribute("errorsMap",errorsMap);
            model.addAttribute("customer",customer);

            return "customer/create";
        }
    }

    private static void validateInfo(Customer customer, Map<String, String> errorsMap) {
        if (!ValidateUtil.isNameValid(customer.getName())){
            errorsMap.put("nameInvalid","Tên không hợp lệ");
        }
        if (!ValidateUtil.isEmailValid(customer.getEmail())){
            errorsMap.put("emailInvalid","Email không hợp lệ");
        }
        if (!ValidateUtil.isAddressValid(customer.getAddress())){
            errorsMap.put("addressInvalid","Địa chỉ không hợp lệ");
        }
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model){
        Customer customer = customerService.findById(id);
        Map<String,String> errorsMap = new HashMap<>();

        model.addAttribute("customer",customer);
        model.addAttribute("errorsMap",errorsMap);
        return "customer/edit";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Long id, RedirectAttributes redirectAttributes,
                         Model model,
                         @ModelAttribute Customer customer){
        Map<String,String> errorsMap = new HashMap<>();
        validateInfo(customer,errorsMap);

        if (errorsMap.isEmpty()){
            customerService.update(id,customer);
            redirectAttributes.addFlashAttribute("editMess","Sửa thành công");

            return "redirect:/dashboard/customers";
        }else {
            model.addAttribute("customer",customer);
            model.addAttribute("errorsMap",errorsMap);
            return "customer/edit";
        }
    }
    @PostMapping("/delete/{id}")
    public String doDelete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        customerService.remove(id);
        redirectAttributes.addFlashAttribute("deleteMess","Xóa thành công");

        return "redirect:/dashboard/customers";
    }
}

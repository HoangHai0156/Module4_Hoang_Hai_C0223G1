package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.CustomerService;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import com.cg.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private ITransferService transferService;

    @Autowired
    private IWithdrawService withdrawService;

    @GetMapping
    public ModelAndView showList(){
        List<Customer> customers = customerService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        modelAndView.addObject("customers",customers);

        return modelAndView;
    }
    @GetMapping("/search")
    public ModelAndView showListSearch(@RequestParam String kw){
        kw = "%"+kw+"%";
        List<Customer> customers = customerService.findAllByNameLikeOrAddressLikeOrEmailLikeOrPhoneLike(kw,kw,kw,kw);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        modelAndView.addObject("customers",customers);

        return modelAndView;
    }

    @GetMapping("/info/{id}")
    public String showCustomerInfo(@PathVariable Long id, Model model){
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()){
            return "errors/404";
        }
        Customer customer = customerOptional.get();
        if (customer.isDeleted()){
            return "redirect:/errors/404";
        }

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
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("mess","createMess");

            return "redirect:/customers";
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
        Optional<Customer> customerOptional = customerService.findById(id);
        Map<String,String> errorsMap = new HashMap<>();

        if (customerOptional.isEmpty()){
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        if (customer.isDeleted()){
            return "redirect:/errors/404";
        }

        model.addAttribute("customer",customer);
        model.addAttribute("errorsMap",errorsMap);
        return "customer/edit";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(RedirectAttributes redirectAttributes,
                         Model model,
                         @PathVariable Long id,
                         @ModelAttribute Customer customer){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        Customer customer1 = optionalCustomer.get();
        customer.setBalance(customer1.getBalance());

        Map<String,String> errorsMap = new HashMap<>();
        validateInfo(customer,errorsMap);

        if (errorsMap.isEmpty()){
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("mess","editMess");

            return "redirect:/customers";
        }else {
            model.addAttribute("customer",customer);
            model.addAttribute("errorsMap",errorsMap);
            return "customer/edit";
        }
    }
    @PostMapping("/delete/{id}")
    public String doDelete(@PathVariable Long id, RedirectAttributes redirectAttributes){

        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()){
            return "redirect:/errors/404";
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()){
            return "redirect:/errors/404";
        }

        customer.setDeleted(true);
        customer.setId(id);

        customerService.save(customer);
        redirectAttributes.addFlashAttribute("mess","deleteMess");

        return "redirect:/customers";
    }

    @GetMapping("/deposit/{id}")
    public ModelAndView showDeposit(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()){
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()){
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Deposit deposit = new Deposit();
        deposit.setCustomer(customer);

        modelAndView.setViewName("customer/deposit");
        modelAndView.addObject("deposit",deposit);

        return modelAndView;
    }
    @PostMapping("/deposit/{id}")
    public ModelAndView doDeposit(@ModelAttribute Deposit deposit,
                                  @PathVariable Long id){
        Customer customer = deposit.getCustomer();
        BigDecimal transAmount = deposit.getTransactionAmount();

        ModelAndView modelAndView = new ModelAndView();

        if (transAmount.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal balance = customer.getBalance();
            balance = balance.add(transAmount);
            customer.setBalance(balance);

            customer.setId(id);
            deposit.setId(null);

            customerService.save(customer);
            depositService.save(deposit);
            modelAndView.addObject("mess","success");
        }else {
            modelAndView.addObject("mess","fail");
        }

        modelAndView.addObject("deposit",deposit);
        modelAndView.setViewName("customer/deposit");

        return modelAndView;
    }
    @GetMapping("/withdraw/{id}")
    public ModelAndView showWithdraw(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()){
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()){
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setCustomer(customer);

        modelAndView.setViewName("customer/withdraw");
        modelAndView.addObject("withdraw",withdraw);

        return modelAndView;
    }
    @PostMapping("/withdraw/{id}")
    public ModelAndView doWithdraw(@ModelAttribute Withdraw withdraw,
                                  @PathVariable Long id){
        Customer customer = withdraw.getCustomer();
        BigDecimal transAmount = withdraw.getTransactionAmount();

        ModelAndView modelAndView = new ModelAndView();

        if (transAmount.compareTo(BigDecimal.ZERO) > 0 && transAmount.compareTo(customer.getBalance()) < 0){
            BigDecimal balance = customer.getBalance();
            balance = balance.subtract(transAmount);
            customer.setBalance(balance);

            withdraw.setId(null);
            customer.setId(id);

            customerService.save(customer);
            withdrawService.save(withdraw);

            modelAndView.addObject("mess","success");
        }else {
            modelAndView.addObject("mess","fail");
        }

        modelAndView.addObject("withdraw",withdraw);
        modelAndView.setViewName("customer/withdraw");

        return modelAndView;
    }

    @GetMapping("/transfer/{id}")
    public ModelAndView showTransfer(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()){
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()){
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Transfer transfer = new Transfer();
        transfer.setSender(customer);

        List<Customer> customers = customerService.findAll();
        List<Customer> customerList = customers.stream().filter(customer1 -> !Objects.equals(customer1.getId(), id)).collect(Collectors.toList());

        modelAndView.setViewName("customer/transfer");
        modelAndView.addObject("transfer",transfer);
        modelAndView.addObject("customerList",customerList);
        return modelAndView;
    }

    @PostMapping("/transfer/{id}")
    public ModelAndView doTransfer(@PathVariable Long id,
                                   @ModelAttribute Transfer transfer){

        ModelAndView modelAndView = new ModelAndView();

        Customer sender = transfer.getSender();
        BigDecimal transactionAmount = transfer.getTransactionAmount();
        BigDecimal transAmount = transfer.getTransferAmount();

        if (transactionAmount.compareTo(sender.getBalance()) > 0 || transactionAmount.compareTo(BigDecimal.ZERO) < 0){
            modelAndView.addObject("mess","fail");
        }else {
            Long recipientId = transfer.getRecipient().getId();
            Optional<Customer> recipientOptional = customerService.findById(recipientId);
            if (recipientOptional.isEmpty()){
                modelAndView.setViewName("errors/404");
                return modelAndView;
            }

            Customer recipient = recipientOptional.get();
            if (recipient.isDeleted()){
                modelAndView.setViewName("errors/404");
                return modelAndView;
            }

            recipient.setBalance(transAmount.add(recipient.getBalance()));
            sender.setBalance(sender.getBalance().subtract(transactionAmount));

            transfer.setId(null);
            transfer.setRecipient(recipient);
            transfer.setFeeAmount(transactionAmount.subtract(transAmount));
            sender.setId(id);
            recipient.setId(recipientId);

            customerService.save(sender);
            customerService.save(recipient);
            transferService.save(transfer);

            modelAndView.addObject("mess","success");
        }

        List<Customer> customers = customerService.findAll();
        List<Customer> customerList = customers.stream().filter(customer1 -> !Objects.equals(customer1.getId(), id)).collect(Collectors.toList());

        modelAndView.setViewName("customer/transfer");
        modelAndView.addObject("customer",sender);
        modelAndView.addObject("customerList",customerList);
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView showHistory(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/history");
        List<Transfer> transfers = transferService.findAll();
        modelAndView.addObject("transfers",transfers);

        BigDecimal totalFee = BigDecimal.ZERO;
        for (Transfer transfer: transfers){
            totalFee = totalFee.add(transfer.getFeeAmount());
        }

        modelAndView.addObject("totalFee",totalFee);

        return modelAndView;
    }

}

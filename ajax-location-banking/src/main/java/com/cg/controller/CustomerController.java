package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import com.cg.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    public ModelAndView showList() {
        List<Customer> customers = customerService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        modelAndView.addObject("customers", customers);

        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView showListSearch(@RequestParam String kw) {
        kw = "%" + kw + "%";
        List<Customer> customers = customerService.findAllByFullNameLikeOrAddressLikeOrEmailLikeOrPhoneLike(kw, kw, kw, kw);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        modelAndView.addObject("customers", customers);

        return modelAndView;
    }

    @GetMapping("/info/{id}")
    public String showCustomerInfo(@PathVariable Long id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return "errors/404";
        }
        Customer customer = customerOptional.get();
        if (customer.isDeleted()) {
            return "errors/404";
        }

        model.addAttribute("customer", customer);
        return "modalInfo";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Customer customer = new Customer();
        Map<String, String> errorsMap = new HashMap<>();

        model.addAttribute("customer", customer);
        model.addAttribute("errorsMap", errorsMap);

        return "modalCreate";
    }

    @PostMapping("/create")
    public String doCreate(RedirectAttributes redirectAttributes,
                           @ModelAttribute Customer customer,
                           Model model) {
        Map<String, String> errorsMap = new HashMap<>();
        customer.setId(-1L);
        validateInfo(customer, errorsMap);

        if (errorsMap.isEmpty()) {
            customer.setId(null);
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("mess", "createMess");

            return "redirect:/customers";
        } else {
            model.addAttribute("errorsMap", errorsMap);
            model.addAttribute("customer", customer);

            return "modalCreate";
        }
    }

    private void validateInfo(Customer customer, Map<String, String> errorsMap) {
        if (!ValidateUtil.isNameValid(customer.getFullName())) {
            errorsMap.put("nameInvalid", "Tên không hợp lệ. Tên trong khoảng 7-30 ký tự, không được phép chứa chữ số");
        }
        if (!ValidateUtil.isEmailValid(customer.getEmail())) {
            errorsMap.put("emailInvalid", "Email không hợp lệ. Nhập theo định dạng abc@abc.abc");
        }
//        if (!ValidateUtil.isAddressValid(customer.getAddress())) {
//            errorsMap.put("addressInvalid", "Địa chỉ không hợp lệ. Địa chỉ trong khoảng 3-50 ký tự");
//        }
        if (!ValidateUtil.isPhoneValid(customer.getPhone())) {
            errorsMap.put("phoneInvalid", "Phone không hợp lệ. Nhập theo định dạng +x (xxx) xxx-xxxx");
        }
        if (customerService.existsByEmailAndIdNot(customer.getEmail(), customer.getId())) {
            errorsMap.put("emailInvalid", "Email đã tồn tại");
        }
        if (customerService.existsByPhoneAndIdNot(customer.getPhone(), customer.getId())) {
            errorsMap.put("phoneInvalid", "Phone đã tồn tại");
        }
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(id);
        Map<String, String> errorsMap = new HashMap<>();

        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        if (customer.isDeleted()) {
            return "redirect:/errors/404";
        }

        model.addAttribute("customer", customer);
        model.addAttribute("errorsMap", errorsMap);
        return "customer/edit";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(RedirectAttributes redirectAttributes,
                         Model model,
                         @PathVariable Long id,
                         @ModelAttribute Customer customer) {

        Map<String, String> errorsMap = new HashMap<>();
        validateInfo(customer, errorsMap);

        if (errorsMap.isEmpty()) {
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("mess", "editMess");

            return "redirect:/customers";
        } else {
            model.addAttribute("customer", customer);
            model.addAttribute("errorsMap", errorsMap);
            return "customer/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String doDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()) {
            return "redirect:/errors/404";
        }

        customer.setDeleted(true);
        customer.setId(id);

        customerService.save(customer);
        redirectAttributes.addFlashAttribute("mess", "deleteMess");

        return "redirect:/customers";
    }

    @GetMapping("/deposit/{id}")
    public ModelAndView showDeposit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Deposit deposit = new Deposit();
        deposit.setCustomer(customer);

        modelAndView.setViewName("customer/deposit");
        modelAndView.addObject("deposit", deposit);

        return modelAndView;
    }

    @PostMapping("/deposit/{id}")
    public ModelAndView doDeposit(@Valid @ModelAttribute Deposit deposit,
                                  BindingResult bindingResult,
                                  @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()){
            modelAndView.addObject("mess", "failText");
            modelAndView.addObject("deposit", deposit);
            modelAndView.setViewName("customer/deposit");

            return modelAndView;
        }

        Customer customer = deposit.getCustomer();
        BigDecimal transAmount = deposit.getTransactionAmount();



        if (transAmount == null) {
            modelAndView.addObject("mess", "failBlank");
        } else {
            if (transAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal balance = customer.getBalance();
                balance = balance.add(transAmount);
                customer.setBalance(balance);

                customer.setId(id);
                deposit.setId(null);
                deposit.setCustomer(customer);

                try {
                    customerService.doDeposit(deposit, customer);
                } catch (Exception exception) {
                    modelAndView.setViewName("errors/500");
                    return modelAndView;
                }

                modelAndView.addObject("mess", "success");
                deposit.setTransactionAmount(BigDecimal.ZERO);
            } else {
                modelAndView.addObject("mess", "failNegative");
            }
        }

        modelAndView.addObject("deposit", deposit);
        modelAndView.setViewName("customer/deposit");

        return modelAndView;
    }

    @GetMapping("/withdraw/{id}")
    public ModelAndView showWithdraw(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setCustomer(customer);

        modelAndView.setViewName("customer/withdraw");
        modelAndView.addObject("withdraw", withdraw);

        return modelAndView;
    }

    @PostMapping("/withdraw/{id}")
    public ModelAndView doWithdraw(@Valid @ModelAttribute Withdraw withdraw,
                                   BindingResult bindingResult,
                                   @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            modelAndView.addObject("mess", "failText");
            modelAndView.addObject("withdraw", withdraw);
            modelAndView.setViewName("customer/withdraw");

            return modelAndView;
        }

        Customer customer = withdraw.getCustomer();
        BigDecimal transAmount = withdraw.getTransactionAmount();

        if (transAmount == null) {
            modelAndView.addObject("mess", "failBlank");
        } else {
            if (transAmount.compareTo(BigDecimal.ZERO) > 0 && transAmount.compareTo(customer.getBalance()) < 0) {
                BigDecimal balance = customer.getBalance();
                balance = balance.subtract(transAmount);
                customer.setBalance(balance);

                withdraw.setId(null);
                customer.setId(id);
                withdraw.setCustomer(customer);

                try {
                    customerService.doWithdraw(withdraw, customer);
                } catch (Exception exception) {
                    modelAndView.setViewName("errors/500");
                    return modelAndView;
                }

                modelAndView.addObject("mess", "success");
                withdraw.setTransactionAmount(BigDecimal.ZERO);
            } else if (transAmount.compareTo(BigDecimal.ZERO) <= 0 ){
                modelAndView.addObject("mess", "failNegative");
            }else {
                modelAndView.addObject("mess", "failOverload");
            }
        }

        modelAndView.addObject("withdraw", withdraw);
        modelAndView.setViewName("customer/withdraw");

        return modelAndView;
    }

    @GetMapping("/transfer/{id}")
    public ModelAndView showTransfer(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Customer customer = customerOptional.get();
        if (customer.isDeleted()) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }

        Transfer transfer = new Transfer();
        transfer.setSender(customer);

        List<Customer> customers = customerService.findAll();
        List<Customer> customerList = customers.stream().filter(customer1 -> !Objects.equals(customer1.getId(), id)).collect(Collectors.toList());

        modelAndView.setViewName("customer/transfer");
        modelAndView.addObject("transfer", transfer);
        modelAndView.addObject("customerList", customerList);
        return modelAndView;
    }

    @PostMapping("/transfer/{id}")
    public ModelAndView doTransfer(@PathVariable Long id,
                                   @Valid @ModelAttribute Transfer transfer,
                                   BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();

        Customer sender = transfer.getSender();
        List<Customer> customers = customerService.findAll();
        List<Customer> customerList = customers.stream().filter(customer1 -> !Objects.equals(customer1.getId(), id)).collect(Collectors.toList());

        modelAndView.setViewName("customer/transfer");
        modelAndView.addObject("customer", sender);
        modelAndView.addObject("customerList", customerList);

        if (bindingResult.hasErrors()){
            modelAndView.addObject("mess", "failText");

            return modelAndView;
        }
        String idRecipientStr = transfer.getRecipient().getId() + "";
        Long recipientId = Long.parseLong(idRecipientStr);

        if (id.equals(transfer.getRecipient().getId())){
            modelAndView.addObject("mess", "failDuplicate");
            return modelAndView;
        }

        BigDecimal transAmount = transfer.getTransferAmount();

        if (transAmount == null){
            modelAndView.addObject("mess", "failBlank");
        }else {
            BigDecimal transactionAmount = transAmount.multiply(BigDecimal.valueOf(1.1));

            if (transactionAmount.compareTo(sender.getBalance()) > 0) {
                modelAndView.addObject("mess", "failOverload");
            }else if (transactionAmount.compareTo(BigDecimal.ZERO) <= 0){
                modelAndView.addObject("mess", "failNegative");
            }
            else {
//                Long recipientId = transfer.getRecipient().getId();
                Optional<Customer> recipientOptional = customerService.findById(recipientId);
                if (recipientOptional.isEmpty()) {
                    modelAndView.setViewName("errors/404");
                    return modelAndView;
                }

                Customer recipient = recipientOptional.get();
                if (recipient.isDeleted()) {
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

                try {
                    customerService.doTransfer(transfer, sender, recipient);
                } catch (Exception e) {
                    modelAndView.setViewName("errors/500");
                    return modelAndView;
                }

                transfer.setTransferAmount(BigDecimal.ZERO);
                transfer.setTransactionAmount(BigDecimal.ZERO);
                modelAndView.addObject("mess", "success");
            }
        }

        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView showHistory() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/history");
        List<Transfer> transfers = transferService.findAll();
        modelAndView.addObject("transfers", transfers);

        BigDecimal totalFee = BigDecimal.ZERO;
        for (Transfer transfer : transfers) {
            totalFee = totalFee.add(transfer.getFeeAmount());
        }

        modelAndView.addObject("totalFee", totalFee);

        return modelAndView;
    }

}


package com.example.chp_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chp_system.entity.Account;
import com.example.chp_system.entity.Budget;
import com.example.chp_system.entity.ChpUnit;
import com.example.chp_system.entity.Contact;
import com.example.chp_system.entity.Invoice;
import com.example.chp_system.entity.Purchase;
import com.example.chp_system.entity.Telemetry;
import com.example.chp_system.repository.AccountRepository;
import com.example.chp_system.repository.BudgetRepository;
import com.example.chp_system.repository.ChpUnitRepository;
import com.example.chp_system.repository.ContactRepository;
import com.example.chp_system.repository.InvoiceRepository;
import com.example.chp_system.repository.PurchaseRepository;
import com.example.chp_system.repository.TelemetryRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chp")
public class ChpController {

    private final ChpUnitRepository chpUnitRepository;
    private final ContactRepository contactRepository;
    private final TelemetryRepository telemetryRepository;
    private final PurchaseRepository purchaseRepository;
    private final InvoiceRepository invoiceRepository;
    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;

    public ChpController(
            ChpUnitRepository chpUnitRepository,
            ContactRepository contactRepository,
            TelemetryRepository telemetryRepository,
            PurchaseRepository purchaseRepository,
            InvoiceRepository invoiceRepository,
            AccountRepository accountRepository,
            BudgetRepository budgetRepository) {

        this.chpUnitRepository = chpUnitRepository;
        this.contactRepository = contactRepository;
        this.telemetryRepository = telemetryRepository;
        this.purchaseRepository = purchaseRepository;
        this.invoiceRepository = invoiceRepository;
        this.accountRepository = accountRepository;
        this.budgetRepository = budgetRepository;
    }


    // =====================================================
    // CHP UNIT
    // =====================================================

    @PostMapping("/units")
    public ChpUnit addUnit(@RequestBody ChpUnit unit) {
        return chpUnitRepository.save(unit);
    }

    @GetMapping("/units")
    public List<ChpUnit> getUnits() {
        return chpUnitRepository.findAll();
    }


    // =====================================================
    // CONTACTS
    // =====================================================

    @PostMapping("/contacts")
    public Contact addContact(@RequestBody Contact contact) {
        return contactRepository.save(contact);
    }

    @GetMapping("/contacts")
    public List<Contact> getContacts() {
        return contactRepository.findAll();
    }


    // =====================================================
    // TELEMETRY
    // =====================================================

    @PostMapping("/telemetry")
    public Telemetry addTelemetry(@RequestBody Telemetry telemetry) {
        return telemetryRepository.save(telemetry);
    }

    @GetMapping("/telemetry")
    public List<Telemetry> getTelemetry() {
        return telemetryRepository.findAll();
    }


    // =====================================================
    // HYDROGEN PURCHASE
    // =====================================================

    @PostMapping("/purchases")
    public Purchase addPurchase(@RequestBody Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @GetMapping("/purchases")
    public List<Purchase> getPurchases() {
        return purchaseRepository.findAll();
    }


    // =====================================================
    // CUSTOMER INVOICE
    // =====================================================

    @PostMapping("/invoices")
    public Invoice addInvoice(@RequestBody Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @GetMapping("/invoices")
    public List<Invoice> getInvoices() {
        return invoiceRepository.findAll();
    }


    // =====================================================
    // ACCOUNTING
    // =====================================================

    @PostMapping("/accounts")
    public Account addAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }


    // =====================================================
    // BUDGET
    // =====================================================

    @PostMapping("/budgets")
    public Budget addBudget(@RequestBody Budget budget) {
        return budgetRepository.save(budget);
    }

    @GetMapping("/budgets")
    public List<Budget> getBudgets() {
        return budgetRepository.findAll();
    }


    // =====================================================
    // PROFIT & LOSS REPORT
    // =====================================================

    @GetMapping("/reports/pnl")
    public String getProfitAndLoss() {

        // Get all accounting records from the database
        List<Account> accounts = accountRepository.findAll();

        double totalRevenue = 0;
        double totalExpense = 0;

        // Check every accounting record
        for (Account account : accounts) {

            // Income accounts use CREDIT as revenue
            if (account.getAccountType().equalsIgnoreCase("INCOME")) {
                totalRevenue += account.getCredit();
            }

            // Expense accounts use DEBIT as expense
            if (account.getAccountType().equalsIgnoreCase("EXPENSE")) {
                totalExpense += account.getDebit();
            }
        }

        // Profit or Loss
        double profitOrLoss = totalRevenue - totalExpense;

        // Return the report
        return "===== MICRO-CHP PROFIT & LOSS REPORT =====\n"
                + "Total Revenue: ₹" + totalRevenue + "\n"
                + "Total Expense: ₹" + totalExpense + "\n"
                + "Profit/Loss: ₹" + profitOrLoss;
    }
    @GetMapping("/reports/balance-sheet")
public String getBalanceSheet() {

    List<Account> accounts = accountRepository.findAll();

    double totalAssets = 0;
    double totalLiabilities = 0;

    for (Account account : accounts) {

        if (account.getAccountType().equalsIgnoreCase("ASSET")) {
            totalAssets += account.getDebit();
        }

        if (account.getAccountType().equalsIgnoreCase("LIABILITY")) {
            totalLiabilities += account.getCredit();
        }
    }

    return "===== MICRO-CHP BALANCE SHEET =====\n"
            + "Total Assets: ₹" + totalAssets + "\n"
            + "Total Liabilities: ₹" + totalLiabilities;
}
@GetMapping("/reports/budget")
public String getBudgetReport() {

    List<Budget> budgets = budgetRepository.findAll();

    if (budgets.isEmpty()) {
        return "No budget records found.";
    }

    Budget budget = budgets.get(0);

    return "===== CHP BUDGET REPORT =====\n"
            + "Analytic Account: " + budget.getAnalyticAccount() + "\n"
            + "Planned Hydrogen Cost: ₹" + budget.getPlannedHydrogenCost() + "\n"
            + "Actual Hydrogen Cost: ₹" + budget.getActualHydrogenCost() + "\n"
            + "Planned Revenue: ₹" + budget.getPlannedRevenue() + "\n"
            + "Actual Revenue: ₹" + budget.getActualRevenue() + "\n"
            + "Variance: ₹" + budget.getVariance();
}
}
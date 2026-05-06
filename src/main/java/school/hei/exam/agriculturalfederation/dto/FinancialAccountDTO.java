package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;

public class FinancialAccountDTO {
    private String id;
    private String accountType;
    private BigDecimal amount;
    private String holderName;
    private String bankName;
    private String bankCode;
    private String branchCode;
    private String accountNumber;
    private String ribKey;
    private String mobileService;
    private String mobileNumber;

    public FinancialAccountDTO() {
    }

    public FinancialAccountDTO id(String id) {
        this.id = id;
        return this;
    }

    public FinancialAccountDTO accountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public FinancialAccountDTO amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public FinancialAccountDTO holderName(String holderName) {
        this.holderName = holderName;
        return this;
    }

    public FinancialAccountDTO bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public FinancialAccountDTO bankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public FinancialAccountDTO branchCode(String branchCode) {
        this.branchCode = branchCode;
        return this;
    }

    public FinancialAccountDTO accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public FinancialAccountDTO ribKey(String ribKey) {
        this.ribKey = ribKey;
        return this;
    }

    public FinancialAccountDTO mobileService(String mobileService) {
        this.mobileService = mobileService;
        return this;
    }

    public FinancialAccountDTO mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getRibKey() {
        return ribKey;
    }

    public String getMobileService() {
        return mobileService;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}

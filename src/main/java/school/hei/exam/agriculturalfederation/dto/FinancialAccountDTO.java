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

    private FinancialAccountDTO(Builder builder) {
        this.id = builder.id;
        this.accountType = builder.accountType;
        this.amount = builder.amount;
        this.holderName = builder.holderName;
        this.bankName = builder.bankName;
        this.bankCode = builder.bankCode;
        this.branchCode = builder.branchCode;
        this.accountNumber = builder.accountNumber;
        this.ribKey = builder.ribKey;
        this.mobileService = builder.mobileService;
        this.mobileNumber = builder.mobileNumber;
    }

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
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

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder accountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder holderName(String holderName) {
            this.holderName = holderName;
            return this;
        }

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder bankCode(String bankCode) {
            this.bankCode = bankCode;
            return this;
        }

        public Builder branchCode(String branchCode) {
            this.branchCode = branchCode;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder ribKey(String ribKey) {
            this.ribKey = ribKey;
            return this;
        }

        public Builder mobileService(String mobileService) {
            this.mobileService = mobileService;
            return this;
        }

        public Builder mobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public FinancialAccountDTO build() {
            return new FinancialAccountDTO(this);
        }
    }
}

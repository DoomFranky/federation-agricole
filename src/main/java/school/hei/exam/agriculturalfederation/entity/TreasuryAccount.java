package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryAccount {
    private String id;
    private String collectivityId;
    private String federationId;
    private AccountType accountType;
    private BigDecimal balanceMga;
    private LocalDate asOfDate;
    private String currency;
    
    private BankAccountDetail bankAccountDetail;
    private MobileMoneyAccountDetail mobileMoneyAccountDetail;

    public enum AccountType {
        CASH, BANK, MOBILE_MONEY
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BankAccountDetail {
        private String accountHolderName;
        private BankName bankName;
        private String bankCode;
        private String branchCode;
        private String accountNumber;
        private String ribKey;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MobileMoneyAccountDetail {
        private String accountHolderName;
        private MobileMoneyProvider provider;
        private String phoneNumber;
    }

    public enum BankName {
        BRED, MCB, BMOI, BOA, BGFI, AFG, ACCES_BANQUE, BAOBAB, SIPEM
    }

    public enum MobileMoneyProvider {
        ORANGE_MONEY, MVOLA, AIRTEL_MONEY
    }
}
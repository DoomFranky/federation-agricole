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

}
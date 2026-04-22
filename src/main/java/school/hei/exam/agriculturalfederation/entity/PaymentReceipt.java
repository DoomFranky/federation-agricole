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
public class PaymentReceipt {
    private String id;
    private String membershipId;
    private String duesRuleId;
    private BigDecimal amountMga;
    private PaymentMethod paymentMethod;
    private LocalDate collectedAt;
    private String collectedByTreasurer;
    private String notes;

    public enum PaymentMethod {
        CASH, BANK_TRANSFER, MOBILE_MONEY
    }
}
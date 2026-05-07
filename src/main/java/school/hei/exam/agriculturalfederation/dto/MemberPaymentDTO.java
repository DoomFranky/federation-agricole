package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MemberPaymentDTO(
    String id,
    BigDecimal amount,
    String paymentMode,
    FinancialAccountDTO accountCredited,
    LocalDate creationDate
) {}
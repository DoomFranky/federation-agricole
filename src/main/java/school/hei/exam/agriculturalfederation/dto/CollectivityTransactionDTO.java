package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CollectivityTransactionDTO(
    String id,
    LocalDate creationDate,
    BigDecimal amount,
    String paymentMode,
    FinancialAccountDTO accountCredited,
    String memberId,
    String memberName
) {}
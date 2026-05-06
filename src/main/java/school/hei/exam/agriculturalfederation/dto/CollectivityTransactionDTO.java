package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import school.hei.exam.agriculturalfederation.entity.Member;

public record CollectivityTransactionDTO(
    String id,
    LocalDate creationDate,
    BigDecimal amount,
    String paymentMode,
    FinancialAccountDTO accountCredited,
    Member memberDebited
) {}
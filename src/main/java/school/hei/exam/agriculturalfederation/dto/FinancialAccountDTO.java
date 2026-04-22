package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;

public record FinancialAccountDTO(
    String id,
    String accountType,
    BigDecimal amount,
    String holderName,
    String bankName,
    String bankCode,
    String branchCode,
    String accountNumber,
    String ribKey,
    String mobileService,
    String mobileNumber
) {}
package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;

public record CreateMemberPaymentDTO(
    BigDecimal amount,
    String membershipFeeIdentifier,
    String accountCreditedIdentifier,
    String paymentMode
) {}
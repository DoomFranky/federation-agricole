package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MembershipFeeDTO(
    String id,
    LocalDate eligibleFrom,
    String frequency,
    BigDecimal amount,
    String label,
    String status
) {}
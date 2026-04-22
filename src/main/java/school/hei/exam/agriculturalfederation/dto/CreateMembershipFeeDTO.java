package school.hei.exam.agriculturalfederation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateMembershipFeeDTO(
    LocalDate eligibleFrom,
    String frequency,
    BigDecimal amount,
    String label
) {}
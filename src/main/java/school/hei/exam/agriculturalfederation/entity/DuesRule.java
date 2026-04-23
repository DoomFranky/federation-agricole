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
public class DuesRule {
    private String id;
    private String collectivityId;
    private DuesFrequency frequency;
    private BigDecimal amountMga;
    private String label;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private boolean active;

    public enum DuesFrequency {
        MONTHLY, ANNUALLY, WEEKLY, PUNCTUALLY
    }
}
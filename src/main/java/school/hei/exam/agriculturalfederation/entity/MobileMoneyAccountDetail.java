package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MobileMoneyAccountDetail {
    private String accountHolderName;
    private MobileMoneyProvider provider;
    private String phoneNumber;
}

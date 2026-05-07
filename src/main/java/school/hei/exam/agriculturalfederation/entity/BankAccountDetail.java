package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDetail {
    private String accountHolderName;
    private BankName bankName;
    private String bankCode;
    private String branchCode;
    private String accountNumber;
    private String ribKey;
}

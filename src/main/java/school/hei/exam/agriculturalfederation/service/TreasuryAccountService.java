package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.FinancialAccountDTO;
import school.hei.exam.agriculturalfederation.entity.TreasuryAccount;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.TreasuryAccountRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreasuryAccountService {
    private final TreasuryAccountRepository treasuryAccountRepository;
    private final CollectivityRepository collectivityRepository;

    public TreasuryAccountService(TreasuryAccountRepository treasuryAccountRepository, CollectivityRepository collectivityRepository) {
        this.treasuryAccountRepository = treasuryAccountRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<FinancialAccountDTO> getFinancialAccounts(String collectivityId, LocalDate atDate) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        List<TreasuryAccount> accounts;
        if (atDate != null) {
            accounts = treasuryAccountRepository.findByCollectivityAtDate(collectivityId, atDate);
        } else {
            accounts = treasuryAccountRepository.findByCollectivity(collectivityId);
        }

        return accounts.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private FinancialAccountDTO toDto(TreasuryAccount account) {
        String holderName = null;
        String bankName = null;
        String bankCode = null;
        String branchCode = null;
        String accountNumber = null;
        String ribKey = null;
        String mobileService = null;
        String mobileNumber = null;

        if (account.getAccountType() == TreasuryAccount.AccountType.BANK && account.getBankAccountDetail() != null) {
            holderName = account.getBankAccountDetail().getAccountHolderName();
            bankName = account.getBankAccountDetail().getBankName() != null ? account.getBankAccountDetail().getBankName().name() : null;
            bankCode = account.getBankAccountDetail().getBankCode();
            branchCode = account.getBankAccountDetail().getBranchCode();
            accountNumber = account.getBankAccountDetail().getAccountNumber();
            ribKey = account.getBankAccountDetail().getRibKey();
        } else if (account.getAccountType() == TreasuryAccount.AccountType.MOBILE_MONEY && account.getMobileMoneyAccountDetail() != null) {
            holderName = account.getMobileMoneyAccountDetail().getAccountHolderName();
            mobileService = account.getMobileMoneyAccountDetail().getProvider() != null ? account.getMobileMoneyAccountDetail().getProvider().name() : null;
            mobileNumber = account.getMobileMoneyAccountDetail().getPhoneNumber();
        }

        return new FinancialAccountDTO(
            account.getId(),
            account.getAccountType().name(),
            account.getBalanceMga(),
            holderName,
            bankName,
            bankCode,
            branchCode,
            accountNumber,
            ribKey,
            mobileService,
            mobileNumber
        );
    }
}
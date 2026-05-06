package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CollectivityTransactionDTO;
import school.hei.exam.agriculturalfederation.dto.FinancialAccountDTO;
import school.hei.exam.agriculturalfederation.entity.AccountType;
import school.hei.exam.agriculturalfederation.entity.PaymentReceipt;
import school.hei.exam.agriculturalfederation.entity.TreasuryAccount;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.PaymentReceiptRepository;
import school.hei.exam.agriculturalfederation.repository.TreasuryAccountRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final PaymentReceiptRepository paymentRepository;
    private final TreasuryAccountRepository accountRepository;
    private final CollectivityRepository collectivityRepository;

    public TransactionService(
            PaymentReceiptRepository paymentRepository,
            TreasuryAccountRepository accountRepository,
            CollectivityRepository collectivityRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityTransactionDTO> getTransactions(String collectivityId, LocalDate from, LocalDate to) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found");
        }
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are required");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date must be before 'to' date");
        }

        return paymentRepository.findByCollectivityInPeriod(collectivityId, from, to).stream()
            .map(receipt -> {
                FinancialAccountDTO acc = null;
                if (receipt.getMembershipId() != null) {
                    AccountType accountType = paymentMethodToAccountType(receipt.getPaymentMethod());
                    TreasuryAccount ta = accountRepository.findByCollectivityAndType(collectivityId, accountType);
                    if (ta == null) {
                        ta = accountRepository.findByCollectivity(collectivityId).stream()
                            .findFirst().orElse(null);
                    }
                    if (ta != null) {
                        acc = FinancialAccountDTO.builder()
                            .id(ta.getId())
                            .accountType(ta.getAccountType().name())
                            .amount(ta.getBalanceMga())
                            .build();
                    }
                }
                return new CollectivityTransactionDTO(
                    receipt.getId(),
                    receipt.getCollectedAt(),
                    receipt.getAmountMga(),
                    receipt.getPaymentMethod().name(),
                    acc,
                    receipt.getMember()
                );
            })
            .collect(Collectors.toList());
    }

    private AccountType paymentMethodToAccountType(PaymentReceipt.PaymentMethod method) {
        return switch (method) {
            case CASH -> AccountType.CASH;
            case BANK_TRANSFER -> AccountType.BANK;
            case MOBILE_MONEY -> AccountType.MOBILE_MONEY;
        };
    }
}
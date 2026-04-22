package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CreateMemberPaymentDTO;
import school.hei.exam.agriculturalfederation.dto.FinancialAccountDTO;
import school.hei.exam.agriculturalfederation.dto.MemberPaymentDTO;
import school.hei.exam.agriculturalfederation.entity.PaymentReceipt;
import school.hei.exam.agriculturalfederation.entity.TreasuryAccount;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityMembershipRepository;
import school.hei.exam.agriculturalfederation.repository.PaymentReceiptRepository;
import school.hei.exam.agriculturalfederation.repository.TreasuryAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentReceiptRepository paymentRepository;
    private final TreasuryAccountRepository accountRepository;
    private final CollectivityMembershipRepository membershipRepository;

    public PaymentService(
            PaymentReceiptRepository paymentRepository,
            TreasuryAccountRepository accountRepository,
            CollectivityMembershipRepository membershipRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.membershipRepository = membershipRepository;
    }

    public List<MemberPaymentDTO> createPayments(String membershipId, List<CreateMemberPaymentDTO> dtos) {
        return dtos.stream()
            .map(dto -> createPayment(membershipId, dto))
            .collect(Collectors.toList());
    }

    private MemberPaymentDTO createPayment(String membershipId, CreateMemberPaymentDTO dto) {
        validatePayment(dto);

        PaymentReceipt receipt = PaymentReceipt.builder()
            .membershipId(membershipId)
            .duesRuleId(dto.membershipFeeIdentifier())
            .amountMga(dto.amount())
            .paymentMethod(PaymentReceipt.PaymentMethod.valueOf(dto.paymentMode().toUpperCase()))
            .collectedAt(LocalDate.now())
            .build();

        receipt = paymentRepository.create(receipt);

        if (dto.accountCreditedIdentifier() != null) {
            TreasuryAccount account = accountRepository.findById(dto.accountCreditedIdentifier());
            if (account != null) {
                BigDecimal newBalance = account.getBalanceMga().add(dto.amount());
                accountRepository.updateBalance(account.getId(), newBalance);
                account.setBalanceMga(newBalance);
            }
        }

        FinancialAccountDTO accDto = null;
        if (dto.accountCreditedIdentifier() != null) {
            TreasuryAccount acc = accountRepository.findById(dto.accountCreditedIdentifier());
            if (acc != null) {
                accDto = new FinancialAccountDTO(
                    acc.getId(),
                    acc.getAccountType().name(),
                    acc.getBalanceMga(),
                    null, null, null, null, null, null, null, null
                );
            }
        }

        return new MemberPaymentDTO(
            receipt.getId(),
            receipt.getAmountMga(),
            receipt.getPaymentMethod().name(),
            accDto,
            receipt.getCollectedAt()
        );
    }

    private void validatePayment(CreateMemberPaymentDTO dto) {
        if (dto.amount() == null || dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }
        if (dto.paymentMode() == null || dto.paymentMode().isBlank()) {
            throw new BadRequestException("Payment mode is required");
        }
        String mode = dto.paymentMode().toUpperCase();
        if (!mode.equals("CASH") && !mode.equals("MOBILE_BANKING") && !mode.equals("BANK_TRANSFER")) {
            throw new BadRequestException("Invalid payment mode: " + dto.paymentMode());
        }
        if (dto.accountCreditedIdentifier() == null || dto.accountCreditedIdentifier().isBlank()) {
            throw new BadRequestException("Account credited is required");
        }
        TreasuryAccount acc = accountRepository.findById(dto.accountCreditedIdentifier());
        if (acc == null) {
            throw new NotFoundException("Account not found");
        }
    }

    public List<MemberPaymentDTO> getPaymentsForMembership(String membershipId) {
        return paymentRepository.findByMembership(membershipId).stream()
            .map(r -> new MemberPaymentDTO(
                r.getId(),
                r.getAmountMga(),
                r.getPaymentMethod().name(),
                null,
                r.getCollectedAt()
            ))
            .collect(Collectors.toList());
    }
}
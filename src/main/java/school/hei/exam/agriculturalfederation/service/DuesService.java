package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CreateMembershipFeeDTO;
import school.hei.exam.agriculturalfederation.dto.MembershipFeeDTO;
import school.hei.exam.agriculturalfederation.entity.DuesRule;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.DuesRuleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DuesService {
    private final DuesRuleRepository duesRuleRepository;
    private final CollectivityRepository collectivityRepository;

    public DuesService(DuesRuleRepository duesRuleRepository, CollectivityRepository collectivityRepository) {
        this.duesRuleRepository = duesRuleRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<MembershipFeeDTO> getFees(String collectivityId) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found");
        }
        return duesRuleRepository.findActiveByCollectivity(collectivityId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<MembershipFeeDTO> createFees(String collectivityId, List<CreateMembershipFeeDTO> dtos) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found");
        }
        return dtos.stream()
            .map(dto -> createFee(collectivityId, dto))
            .collect(Collectors.toList());
    }

    private MembershipFeeDTO createFee(String collectivityId, CreateMembershipFeeDTO dto) {
        validateFee(dto);
        
        for (DuesRule existing : duesRuleRepository.findActiveByCollectivity(collectivityId)) {
            if (existing.getFrequency() == DuesRule.DuesFrequency.valueOf(dto.frequency())) {
                existing.setActive(false);
                existing.setEffectiveTo(LocalDate.now().minusDays(1));
                duesRuleRepository.deactivate(existing.getId());
            }
        }

        DuesRule rule = DuesRule.builder()
            .collectivityId(collectivityId)
            .frequency(DuesRule.DuesFrequency.valueOf(dto.frequency().toUpperCase()))
            .amountMga(dto.amount())
            .label(dto.label())
            .effectiveFrom(dto.eligibleFrom() != null ? dto.eligibleFrom() : LocalDate.now())
            .active(true)
            .build();

        rule = duesRuleRepository.create(rule);
        return toDTO(rule);
    }

    private void validateFee(CreateMembershipFeeDTO dto) {
        if (dto.frequency() == null || dto.frequency().isBlank()) {
            throw new BadRequestException("Frequency is required");
        }
        String freq = dto.frequency().toUpperCase();
        if (!freq.equals("MONTHLY") && !freq.equals("ANNUAL") && !freq.equals("PUNCTUAL")) {
            throw new BadRequestException("Invalid frequency: " + dto.frequency());
        }
        if (dto.amount() == null || dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }
    }

    private MembershipFeeDTO toDTO(DuesRule rule) {
        return new MembershipFeeDTO(
            rule.getId(),
            rule.getEffectiveFrom(),
            rule.getFrequency().name(),
            rule.getAmountMga(),
            rule.getLabel(),
            rule.isActive() ? "ACTIVE" : "INACTIVE"
        );
    }
}
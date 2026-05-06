package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CollectivityStructureDTO;
import school.hei.exam.agriculturalfederation.dto.IdentityCollectivityDTO;
import school.hei.exam.agriculturalfederation.dto.InputCollectivityDTO;
import school.hei.exam.agriculturalfederation.entity.Collectivity;
import school.hei.exam.agriculturalfederation.entity.CollectivityStructure;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.ConflictException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityMembershipRepository;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectivityService {
    private final int MIN_MEMBERS = 10;
    private final int MEMBERS_WITH_SENIORITY = 5;
    private final int SENIORITY_DAYS = 180;

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final CollectivityMembershipRepository membershipRepository;

    public CollectivityService(
            CollectivityRepository collectivityRepository,
            MemberRepository memberRepository,
            CollectivityMembershipRepository membershipRepository
    ) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
    }

    public List<Collectivity> createCollectivity(List<InputCollectivityDTO> dto) {
        List<Collectivity> transformedCollectivity = new ArrayList<>();

        for (InputCollectivityDTO d : dto) {
            validateCollectivityCreation(d);
        }

        for (InputCollectivityDTO d : dto) {
            Collectivity collectivity = new Collectivity();
            collectivity.setLocation(d.location());
            collectivity.setAgriculturalSpecialty(d.specialization());
            collectivity.setCreatedAt(LocalDate.now());
            transformedCollectivity.add(collectivity);
        }

        List<Collectivity> created = collectivityRepository.create(transformedCollectivity);

        for (int i = 0; i < dto.size(); i++) {
            InputCollectivityDTO d = dto.get(i);
            Collectivity collectivity = created.get(i);

            if (d.members() != null && !d.members().isEmpty()) {
                List<String> structureMemberIds = getStructureMemberIds(d.structure());
                for (String memberId : d.members()) {
                    OccupationEnum occupation = structureMemberIds.contains(memberId)
                        ? getOccupationForStructureMember(d.structure(), memberId)
                        : OccupationEnum.JUNIOR;
                    membershipRepository.createMembership(
                            memberId,
                            collectivity.getId(),
                            occupation,
                            java.util.UUID.randomUUID()
                    );
                }
            }

            if (d.structure() != null) {
                if (!structureHasAllPositions(d.structure())) {
                    throw new BadRequestException("All specific posts (president, vice president, treasurer, secretary) must be filled");
                }
                assignStructure(collectivity.getId(), d.structure());
            }
        }

        return collectivityRepository.findByIds(created.stream()
                .map(Collectivity::getId)
                .collect(Collectors.toList()));
    }

    private void validateCollectivityCreation(InputCollectivityDTO dto) {
        if (dto.federationApproval() == null || !dto.federationApproval()) {
            throw new BadRequestException("Federation approval is required to create a collectivity");
        }
        if (dto.location() == null || dto.location().isBlank()) {
            throw new BadRequestException("Location is required");
        }
        if (dto.members() == null || dto.members().size() < MIN_MEMBERS) {
            throw new BadRequestException(
                "At least " + MIN_MEMBERS + " members are required to create a collectivity. " +
                "Provided: " + (dto.members() == null ? 0 : dto.members().size())
            );
        }
        
        int seniorMembers = 0;
        List<String> seniorIds = membershipRepository.findConfirmedMemberIdsWithMinTenure(
            null, SENIORITY_DAYS
        );
        for (String memberId : dto.members()) {
            if (seniorIds.contains(memberId)) {
                seniorMembers++;
            }
        }

        if (seniorMembers < MEMBERS_WITH_SENIORITY) {
            throw new BadRequestException(
                "At least " + MEMBERS_WITH_SENIORITY + " members with at least " +
                SENIORITY_DAYS + " days of seniority are required. Found: " + seniorMembers
            );
        }

        validateStructure(dto.structure());
    }

    private void validateStructure(CollectivityStructureDTO structure) {
        if (structure == null) {
            return;
        }

        if (!structureHasAllPositions(structure)) {
            throw new BadRequestException(
                "All specific posts (president, vice president, treasurer, secretary) must be filled"
            );
        }

        List<String> requiredPosts = List.of(
            structure.president(),
            structure.vicePresident(),
            structure.treasurer(),
            structure.secretary()
        );

        List<String> distinctPosts = requiredPosts.stream()
            .filter(p -> p != null && !p.isBlank())
            .distinct()
            .collect(Collectors.toList());

        if (distinctPosts.size() != 4) {
            throw new BadRequestException(
                "Each specific post must be occupied by a different member"
            );
        }
    }

    private boolean structureHasAllPositions(CollectivityStructureDTO structure) {
        return structure.president() != null && !structure.president().isBlank()
            && structure.vicePresident() != null && !structure.vicePresident().isBlank()
            && structure.treasurer() != null && !structure.treasurer().isBlank()
            && structure.secretary() != null && !structure.secretary().isBlank();
    }

    public void assignIdentity(String collectivityId, IdentityCollectivityDTO identity) {
        Collectivity existing = collectivityRepository.findById(collectivityId);
        if (existing == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        if (identity.number() == null) {
            throw new BadRequestException("Number is required");
        }
        if (identity.name() == null || identity.name().isBlank()) {
            throw new BadRequestException("Name is required");
        }

        if (existing.getNumber() != null && !existing.getNumber().equals(identity.number())) {
            throw new ConflictException("Number already assigned and cannot be changed for collectivity: " + collectivityId);
        }
        if (existing.getName() != null && !existing.getName().equals(identity.name())) {
            throw new ConflictException("Name already assigned and cannot be changed for collectivity: " + collectivityId);
        }

        Collectivity byNumber = collectivityRepository.findByNumber(identity.number());
        if (byNumber != null && !byNumber.getId().equals(collectivityId)) {
            throw new ConflictException("Number already used by another collectivity: " + identity.number());
        }

        Collectivity byName = collectivityRepository.findByName(identity.name());
        if (byName != null && !byName.getId().equals(collectivityId)) {
            throw new ConflictException("Name already used by another collectivity: " + identity.name());
        }

        collectivityRepository.updateIdentity(collectivityId, identity.number(), identity.name());
    }

    public Collectivity getCollectivity(String id) {
        Collectivity collectivity = collectivityRepository.findById(id);
        if (collectivity == null) {
            throw new NotFoundException("Collectivity not found: " + id);
        }
        return collectivity;
    }

    public List<Collectivity> getAllCollectivities() {
        return collectivityRepository.findAll();
    }

    private void assignStructure(String collectivityId, CollectivityStructureDTO structure) {
        CollectivityStructure cs = new CollectivityStructure();

        if (structure.president() != null) {
            validateMandateLimit(collectivityId, structure.president(), "PRESIDENT");
            Member president = memberRepository.findById(structure.president());
            if (president != null) cs.setPresident(president);
        }
        if (structure.vicePresident() != null) {
            validateMandateLimit(collectivityId, structure.vicePresident(), "VICE_PRESIDENT");
            Member vp = memberRepository.findById(structure.vicePresident());
            if (vp != null) cs.setVicePresident(vp);
        }
        if (structure.treasurer() != null) {
            validateMandateLimit(collectivityId, structure.treasurer(), "TREASURER");
            Member treasurer = memberRepository.findById(structure.treasurer());
            if (treasurer != null) cs.setTreasurer(treasurer);
        }
        if (structure.secretary() != null) {
            validateMandateLimit(collectivityId, structure.secretary(), "SECRETARY");
            Member secretary = memberRepository.findById(structure.secretary());
            if (secretary != null) cs.setSecretary(secretary);
        }

        if (cs.getPresident() != null || cs.getVicePresident() != null
            || cs.getTreasurer() != null || cs.getSecretary() != null) {
            collectivityRepository.saveStructure(collectivityId, cs);
        }
    }

    private void validateMandateLimit(String collectivityId, String memberId, String occupation) {
        int mandateCount = membershipRepository.countMandates(collectivityId, memberId, occupation);
        if (mandateCount >= 2) {
            throw new BadRequestException(
                "Member " + memberId + " already has 2 mandates for position " + occupation + " in collectivity " + collectivityId
            );
        }
    }

    private List<String> getStructureMemberIds(CollectivityStructureDTO structure) {
        if (structure == null) {
            return List.of();
        }
        return List.of(
            structure.president(),
            structure.vicePresident(),
            structure.treasurer(),
            structure.secretary()
        ).stream()
            .filter(id -> id != null && !id.isBlank())
            .collect(Collectors.toList());
    }

    private OccupationEnum getOccupationForStructureMember(CollectivityStructureDTO structure, String memberId) {
        if (memberId.equals(structure.president())) return OccupationEnum.PRESIDENT;
        if (memberId.equals(structure.vicePresident())) return OccupationEnum.VICE_PRESIDENT;
        if (memberId.equals(structure.treasurer())) return OccupationEnum.TREASURER;
        if (memberId.equals(structure.secretary())) return OccupationEnum.SECRETARY;
        return OccupationEnum.SENIOR;
    }
}
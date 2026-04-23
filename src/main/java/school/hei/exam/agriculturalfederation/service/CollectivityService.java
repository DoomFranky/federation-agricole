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
                for (String memberId : d.members()) {
                    membershipRepository.createMembership(
                            memberId,
                            collectivity.getId(),
                            OccupationEnum.JUNIOR
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
        if (dto.members() != null && dto.members().size() < MIN_MEMBERS) {
            throw new BadRequestException(
                "At least " + MIN_MEMBERS + " members are required to create a collectivity. " +
                "Provided: " + dto.members().size()
            );
        }
        if (dto.members() != null) {
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
            Member president = memberRepository.findById(structure.president());
            if (president != null) cs.setPresident(president);
        }
        if (structure.vicePresident() != null) {
            Member vp = memberRepository.findById(structure.vicePresident());
            if (vp != null) cs.setVicePresident(vp);
        }
        if (structure.treasurer() != null) {
            Member treasurer = memberRepository.findById(structure.treasurer());
            if (treasurer != null) cs.setTreasurer(treasurer);
        }
        if (structure.secretary() != null) {
            Member secretary = memberRepository.findById(structure.secretary());
            if (secretary != null) cs.setSecretary(secretary);
        }

        if (cs.getPresident() != null || cs.getVicePresident() != null
            || cs.getTreasurer() != null || cs.getSecretary() != null) {
            collectivityRepository.saveStructure(collectivityId, cs);
        }
    }
}
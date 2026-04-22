package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.InputCollectivityDTO;
import school.hei.exam.agriculturalfederation.dto.IdentityCollectivityDTO;
import school.hei.exam.agriculturalfederation.entity.Collectivity;
import school.hei.exam.agriculturalfederation.entity.CollectivityStructure;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityService(CollectivityRepository collectivityRepository, MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<Collectivity> createCollectivity(List<InputCollectivityDTO> newCollectivityList) {
        List<Collectivity> collectivitiesToSave = new ArrayList<>();
        
        for (InputCollectivityDTO dto : newCollectivityList) {
            if (dto.federationApproval() == null || !dto.federationApproval() || dto.structure() == null) {
                throw new BadRequestException("Collectivity without federation approval or structure missing.");
            }
            
            List<String> allMemberIds = new ArrayList<>();
            if (dto.members() != null) {
                allMemberIds.addAll(dto.members());
            }
            if (dto.structure().president() != null) allMemberIds.add(dto.structure().president());
            if (dto.structure().vicePresident() != null) allMemberIds.add(dto.structure().vicePresident());
            if (dto.structure().secretary() != null) allMemberIds.add(dto.structure().secretary());
            if (dto.structure().treasurer() != null) allMemberIds.add(dto.structure().treasurer());

            List<Member> existingMembers = memberRepository.findMembersById(allMemberIds);
            if (existingMembers.size() < allMemberIds.stream().distinct().count()) {
                throw new NotFoundException("Member not found.");
            }

            CollectivityStructure structure = CollectivityStructure.builder()
                .president(existingMembers.stream().filter(m -> m.getId().equals(dto.structure().president())).findFirst().orElse(null))
                .vicePresident(existingMembers.stream().filter(m -> m.getId().equals(dto.structure().vicePresident())).findFirst().orElse(null))
                .secretary(existingMembers.stream().filter(m -> m.getId().equals(dto.structure().secretary())).findFirst().orElse(null))
                .treasurer(existingMembers.stream().filter(m -> m.getId().equals(dto.structure().treasurer())).findFirst().orElse(null))
                .build();

            List<Member> collectivityMembers = existingMembers.stream()
                .filter(m -> dto.members() != null && dto.members().contains(m.getId()))
                .collect(Collectors.toList());

            Collectivity collectivity = Collectivity.builder()
                .location(dto.location())
                .memberList(collectivityMembers)
                .structure(structure)
                .build();
            
            collectivitiesToSave.add(collectivity);
        }

        return collectivityRepository.saveCollectivity(collectivitiesToSave);
    }

    public Collectivity attributeIdentity(String id, IdentityCollectivityDTO identity) {
        Collectivity collectivity = collectivityRepository.findById(id);
        if (collectivity == null) {
            throw new NotFoundException("Collectivity not found.");
        }
        if (collectivity.getNumber() != null && collectivity.getNumber() != 0) {
            throw new BadRequestException("Collectivity already has a number.");
        }
        if (collectivity.getName() != null && !collectivity.getName().isEmpty()) {
            throw new BadRequestException("Collectivity already has a name.");
        }
        if (identity.number() == null || identity.name() == null || identity.name().isEmpty()) {
            throw new BadRequestException("Invalid input for identity.");
        }
        
        collectivityRepository.updateIdentity(id, identity.number(), identity.name());
        collectivity.setNumber(identity.number());
        collectivity.setName(identity.name());
        return collectivity;
    }
}

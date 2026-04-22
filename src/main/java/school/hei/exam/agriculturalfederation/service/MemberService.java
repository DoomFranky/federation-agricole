package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CreateMemberDTO;
import school.hei.exam.agriculturalfederation.dto.MemberRestDTO;
import school.hei.exam.agriculturalfederation.entity.GenderEnum;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityMembershipRepository;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    private static final int MIN_REFEREES = 2;
    private static final int REFEREE_MIN_TENURE_DAYS = 90;

    private final MemberRepository memberRepository;
    private final CollectivityRepository collectivityRepository;
    private final CollectivityMembershipRepository membershipRepository;

    public MemberService(MemberRepository memberRepository, 
            CollectivityRepository collectivityRepository,
            CollectivityMembershipRepository membershipRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
        this.membershipRepository = membershipRepository;
    }

    public MemberRestDTO createMember(CreateMemberDTO dto) {
        validateCreateMemberInput(dto);

        Member newMember = createMemberEntity(dto.getInformation());
        String membershipId = membershipRepository.createMembership(
            newMember.getId(), 
            dto.getCollectivityIdentifier(), 
            OccupationEnum.JUNIOR
        );

        List<CollectivityMembershipRepository.RefereeInfo> refereeInfos = buildRefereeInfos(dto.getReferees(), dto.getCollectivityIdentifier());
        validateReferees(refereeInfos);
        membershipRepository.addReferees(membershipId, refereeInfos);

        List<Member> refereeMembers = new ArrayList<>();
        for (var ref : refereeInfos) {
            Member referee = memberRepository.findById(ref.memberId());
            if (referee != null) {
                refereeMembers.add(referee);
            }
        }

        return new MemberRestDTO(
            newMember.getId(),
            newMember.getFirstName(),
            newMember.getLastName(),
            newMember.getBirthDate(),
            newMember.getGender().name(),
            newMember.getAddress(),
            newMember.getProfession(),
            newMember.getPhoneNumber(),
            newMember.getEmail(),
            newMember.getOccupation().name(),
            buildRefereeDTOs(refereeMembers, refereeInfos)
        );
    }

    private void validateCreateMemberInput(CreateMemberDTO dto) {
        if (dto.getInformation() == null) {
            throw new BadRequestException("Member information is required");
        }
        if (dto.getCollectivityIdentifier() == null || dto.getCollectivityIdentifier().isBlank()) {
            throw new BadRequestException("Collectivity identifier is required");
        }
        if (dto.getReferees() == null || dto.getReferees().size() < MIN_REFEREES) {
            throw new BadRequestException("At least " + MIN_REFEREES + " referees are required");
        }
        if (dto.getRegistrationFeePaid() == null || !dto.getRegistrationFeePaid()) {
            throw new BadRequestException("Registration fee of 50000 MGA must be paid");
        }
    }

    private void validateReferees(List<CollectivityMembershipRepository.RefereeInfo> referees) {
        if (referees.size() < MIN_REFEREES) {
            throw new BadRequestException("At least " + MIN_REFEREES + " referees are required");
        }

        for (var referee : referees) {
            if (memberRepository.findById(referee.memberId()) == null) {
                throw new NotFoundException("Referee not found: " + referee.memberId());
            }
            List<String> confirmedIds = membershipRepository.findConfirmedMemberIdsWithMinTenure(
                referee.collectivityId(), REFEREE_MIN_TENURE_DAYS
            );
            if (!confirmedIds.contains(referee.memberId())) {
                throw new BadRequestException(
                    "Referee must be confirmed with " + REFEREE_MIN_TENURE_DAYS + " days seniority"
                );
            }
        }
    }

    private List<CollectivityMembershipRepository.RefereeInfo> buildRefereeInfos(
            List<CreateMemberDTO.RefereeDTO> refereeDTOs, String collectivityId) {
        List<CollectivityMembershipRepository.RefereeInfo> infos = new ArrayList<>();
        for (CreateMemberDTO.RefereeDTO ref : refereeDTOs) {
            infos.add(new CollectivityMembershipRepository.RefereeInfo(
                ref.getMemberIdentifier(),
                collectivityId,
                ref.getRelationshipNature()
            ));
        }
        return infos;
    }

    private Member createMemberEntity(CreateMemberDTO.MemberInformationDTO info) {
        Member member = new Member();
        member.setFirstName(info.getFirstName());
        member.setLastName(info.getLastName());
        member.setBirthDate(info.getBirthDate());
        member.setGender(GenderEnum.valueOf(info.getGender().toUpperCase()));
        member.setAddress(info.getAddress());
        member.setProfession(info.getProfession());
        member.setPhoneNumber(info.getPhoneNumber());
        member.setEmail(info.getEmail());
        member.setOccupation(OccupationEnum.JUNIOR);
        return memberRepository.create(member);
    }

    private List<MemberRestDTO.RefereeDTO> buildRefereeDTOs(
            List<Member> members, List<CollectivityMembershipRepository.RefereeInfo> infos) {
        List<MemberRestDTO.RefereeDTO> dtos = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            Member ref = members.get(i);
            String relation = (i < infos.size()) ? infos.get(i).relationshipNature() : "";
            dtos.add(new MemberRestDTO.RefereeDTO(ref.getId(), ref.getFirstName(), ref.getLastName(), relation));
        }
        return dtos;
    }

    public Member getMemberById(String id) {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new NotFoundException("Member not found: " + id);
        }
        return member;
    }
}
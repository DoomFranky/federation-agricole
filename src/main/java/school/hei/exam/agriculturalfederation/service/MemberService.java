package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CreateMemberDTO;
import school.hei.exam.agriculturalfederation.dto.MemberInformationDTO;
import school.hei.exam.agriculturalfederation.dto.MemberRestDTO;
import school.hei.exam.agriculturalfederation.dto.RefereeDTO;
import school.hei.exam.agriculturalfederation.entity.GenderEnum;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;
import school.hei.exam.agriculturalfederation.entity.RefereeInfo;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityMembershipRepository;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    private final int MIN_REFEREES = 2;
    private final int REFEREE_MIN_TENURE_DAYS = 90;

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

    public List<MemberRestDTO> createMember(List<CreateMemberDTO> dto) {
        List<MemberRestDTO> listOfMember = new ArrayList<>();

        for (CreateMemberDTO createMemberDTO : dto) {
            createMemberDTO.setInformation(
                new MemberInformationDTO(
                    createMemberDTO.getFirstName(),
                    createMemberDTO.getLastName(),
                    createMemberDTO.getBirthDate(),
                    createMemberDTO.getGender().toString(),
                    createMemberDTO.getAddress(),
                    createMemberDTO.getProfession(),
                    createMemberDTO.getPhoneNumber(),
                    createMemberDTO.getEmail(),
                    createMemberDTO.getOccupation().toString()
                )
            );

            validateCreateMemberInput(createMemberDTO);

            var collectivity = collectivityRepository.findById(createMemberDTO.getCollectivityIdentifier());
            if (collectivity == null) {
                collectivity = collectivityRepository.findByNumber(Integer.parseInt(createMemberDTO.getCollectivityIdentifier()));
            }
            if (collectivity == null) {
                collectivity = collectivityRepository.findByName(createMemberDTO.getCollectivityIdentifier());
            }
            if (collectivity == null) {
                throw new NotFoundException("Collectivity not found: " + createMemberDTO.getCollectivityIdentifier());
            }

            List<RefereeInfo> refereeInfos = buildRefereeInfos(createMemberDTO.getReferees());
            validateReferees(refereeInfos, collectivity.getId());

            UUID uuid = UUID.randomUUID();
            Member newMember = createMemberEntity(createMemberDTO.getInformation(), uuid);

            UUID memberShipUUID = UUID.randomUUID();
            membershipRepository.createMembership(
                newMember.getId(),
                collectivity.getId(),
                OccupationEnum.JUNIOR,
                memberShipUUID
            );
            String membershipId = memberShipUUID.toString();

            membershipRepository.addReferees(membershipId, refereeInfos);

            List<Member> refereeMembers = new ArrayList<>();
            for (var ref : refereeInfos) {
                Member referee = memberRepository.findById(ref.memberId());
                if (referee != null) {
                    refereeMembers.add(referee);
                }
            }
            listOfMember.add(
                new MemberRestDTO(
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
                    buildRefereeDTOs(refereeMembers)
                )
            );
        }
        return listOfMember;
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
            throw new BadRequestException("Registration fee must be paid");
        }
        if (dto.getMembershipDuesPaid() == null || !dto.getMembershipDuesPaid()) {
            throw new BadRequestException("Membership dues must be paid");
        }
    }

    private void validateReferees(List<RefereeInfo> referees, String targetCollectivityId) {
        if (referees.size() < MIN_REFEREES) {
            throw new BadRequestException("At least " + MIN_REFEREES + " referees are required");
        }

        int targetCollectivityCount = 0;
        int otherCollectivityCount = 0;

        for (var referee : referees) {
            if (memberRepository.findById(referee.memberId()) == null) {
                throw new NotFoundException("Referee not found: " + referee.memberId());
            }
            List<String> confirmedIds = membershipRepository.findConfirmedMemberIdsWithMinTenure(
                null, REFEREE_MIN_TENURE_DAYS
            );
            if (!confirmedIds.contains(referee.memberId())) {
                throw new BadRequestException(
                    "Referee must be confirmed with " + REFEREE_MIN_TENURE_DAYS + " days seniority"
                );
            }
            if (targetCollectivityId.equals(referee.collectivityId())) {
                targetCollectivityCount++;
            } else {
                otherCollectivityCount++;
            }
        }

        if (targetCollectivityCount < otherCollectivityCount) {
            throw new BadRequestException(
                "Number of referees from target collectivity (" + targetCollectivityCount +
                ") must be at least equal to referees from other collectivities (" + otherCollectivityCount + ")"
            );
        }
    }

    private List<RefereeInfo> buildRefereeInfos(List<RefereeDTO> refereeDTOs) {
        List<RefereeInfo> infos = new ArrayList<>();
        for (RefereeDTO ref : refereeDTOs) {
            String collectivityId = membershipRepository.findMemberCollectivityId(ref.getMemberIdentifier());
            if (collectivityId == null) {
                throw new NotFoundException("Referee is not an active member of any collectivity: " + ref.getMemberIdentifier());
            }
            infos.add(new RefereeInfo(
                null,
                ref.getMemberIdentifier(),
                collectivityId,
                ref.getRelationshipNature()
            ));
        }
        return infos;
    }

    private Member createMemberEntity(MemberInformationDTO info,UUID uuid) {
        Member member = new Member();
        member.setId(uuid.toString());
        member.setFirstName(info.getFirstName());
        member.setLastName(info.getLastName());
        member.setBirthDate(info.getBirthDate());
        member.setGender(GenderEnum.valueOf(info.getGender().toUpperCase()));
        member.setAddress(info.getAddress());
        member.setProfession(info.getProfession());
        member.setPhoneNumber(info.getPhoneNumber());
        member.setEmail(info.getEmail());
        member.setOccupation(OccupationEnum.JUNIOR);
        memberRepository.create(member,uuid);
        return memberRepository.findById(uuid.toString());
    }

    private List<Member> buildRefereeDTOs(List<Member> members) {
        return members;
    }

    public Member getMemberById(String id) {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new NotFoundException("Member not found: " + id);
        }
        return member;
    }
}
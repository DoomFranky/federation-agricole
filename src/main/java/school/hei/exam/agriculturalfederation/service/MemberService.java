package school.hei.exam.agriculturalfederation.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.MemberInscription;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;

@Service
public class MemberService {
    MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public List<Member> createMembers (List<MemberInscription> membersInscription) {
        try {
            for (MemberInscription memberInscription : membersInscription) {
            if (memberInscription.getReferees().isEmpty()||memberInscription.getReferees()==null) {
                throw new BadRequestException("Member with bad referees or without proper payment is not allowed");
            }
            if (membersInscription.isEmpty()) {
                throw new NotFoundException("member not found");
            }
        }
        return memberRepository.createMembers(membersInscription);    
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
}

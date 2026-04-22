package school.hei.exam.agriculturalfederation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import school.hei.exam.agriculturalfederation.dto.MemberRest;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.MemberInscription;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.service.MemberService;

@RestController
public class MemberController {
    MemberService memberService;
    
    public MemberController(MemberService memberService){
        this.memberService = memberService; 
    }

    @PostMapping("/members")
    public ResponseEntity<?> postMembers (@RequestBody List<MemberInscription> memberInscription) {
        try {
            List<Member> listOfMember = memberService.createMembers(memberInscription);
            return ResponseEntity.status(HttpStatus.OK)
                .body(listOfMember.stream().map(member->new MemberRest(
                    member.getFirstName(),member.getLastName(),member.getBirthday(),member.getGender(),
                    member.getAddress(),member.getProfession(),member.getPhoneNumber(),
                    member.getEmail(),member.getOccupation(),member.getId(),
                    member.getReferees().stream().map(m->m.getReferee().getFirstName()).collect(Collectors.toList()) 
                )).collect(Collectors.toList()));
        } catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

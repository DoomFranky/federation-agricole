package school.hei.exam.agriculturalfederation.entity;

import java.time.LocalDate;
import java.util.List;

public class MemberInscription extends Member{
    private Boolean registrationFeePaid;
    private Boolean membershipDuestPaid;
    public MemberInscription(String id, String firstName,String lastName ,LocalDate birthday, GenderEnum gender, String address,
            String profession, String phoneNumber, String email, OccupationEnum occupation, List<Reference> referees,
            Boolean registrationFeePaid, Boolean membershipDuestPaid) {
        super(id, firstName,lastName ,birthday, gender, address, profession, phoneNumber, email, occupation, referees);
        this.registrationFeePaid = registrationFeePaid;
        this.membershipDuestPaid = membershipDuestPaid;
    }
    public Boolean getRegistrationFeePaid() {
        return registrationFeePaid;
    }
    public void setRegistrationFeePaid(Boolean registrationFeePaid) {
        this.registrationFeePaid = registrationFeePaid;
    }
    public Boolean getMembershipDuestPaid() {
        return membershipDuestPaid;
    }
    public void setMembershipDuestPaid(Boolean membershipDuestPaid) {
        this.membershipDuestPaid = membershipDuestPaid;
    }
    
}

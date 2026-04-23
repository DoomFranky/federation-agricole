package school.hei.exam.agriculturalfederation.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateMemberDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private String occupation;
    private MemberInformationDTO information;
    private String collectivityIdentifier;
    private List<String> referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;

    public CreateMemberDTO() {
    }

    public CreateMemberDTO(MemberInformationDTO information, String collectivityIdentifier,
                           List<String> referees, Boolean registrationFeePaid, Boolean membershipDuesPaid) {
        this.information = information;
        this.collectivityIdentifier = collectivityIdentifier;
        this.referees = referees;
        this.registrationFeePaid = registrationFeePaid;
        this.membershipDuesPaid = membershipDuesPaid;
    }
    

    public CreateMemberDTO(String firstName, String lastName, LocalDate birthDate, String gender, String address,
            String profession, String phoneNumber, String email, String occupation, MemberInformationDTO information,
            String collectivityIdentifier, List<String> referees, Boolean registrationFeePaid,
            Boolean membershipDuesPaid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.occupation = occupation;
        this.information = information;
        this.collectivityIdentifier = collectivityIdentifier;
        this.referees = referees;
        this.registrationFeePaid = registrationFeePaid;
        this.membershipDuesPaid = membershipDuesPaid;
    }

    public MemberInformationDTO getInformation() {
        return information;
    }

    public void setInformation(MemberInformationDTO information) {
        this.information = information;
    }

    public String getCollectivityIdentifier() {
        return collectivityIdentifier;
    }

    public void setCollectivityIdentifier(String collectivityIdentifier) {
        this.collectivityIdentifier = collectivityIdentifier;
    }

    public List<String> getReferees() {
        return referees;
    }

    public void setReferees(List<String> referees) {
        this.referees = referees;
    }

    public Boolean getRegistrationFeePaid() {
        return registrationFeePaid;
    }

    public void setRegistrationFeePaid(Boolean registrationFeePaid) {
        this.registrationFeePaid = registrationFeePaid;
    }

    public Boolean getMembershipDuesPaid() {
        return membershipDuesPaid;
    }

    public void setMembershipDuesPaid(Boolean membershipDuesPaid) {
        this.membershipDuesPaid = membershipDuesPaid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

}
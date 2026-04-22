package school.hei.exam.agriculturalfederation.dto;

import java.time.LocalDate;
import java.util.List;

import school.hei.exam.agriculturalfederation.entity.GenderEnum;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;

public class MemberRest {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private GenderEnum gender;
    private String address;
    private String professtion;
    private String phoneNumber;
    private String email;
    private OccupationEnum occupation;
    private String id;
    private List<String> referees;
    public MemberRest(String firstName, String lastName, LocalDate birthDate, GenderEnum gender, String address,
            String professtion, String phoneNumber, String email, OccupationEnum occupation, String id,
            List<String> referees) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.professtion = professtion;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.occupation = occupation;
        this.id = id;
        this.referees = referees;
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
    public GenderEnum getGender() {
        return gender;
    }
    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getProfesstion() {
        return professtion;
    }
    public void setProfesstion(String professtion) {
        this.professtion = professtion;
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
    public OccupationEnum getOccupation() {
        return occupation;
    }
    public void setOccupation(OccupationEnum occupation) {
        this.occupation = occupation;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<String> getReferees() {
        return referees;
    }
    public void setReferees(List<String> referees) {
        this.referees = referees;
    }

    
}
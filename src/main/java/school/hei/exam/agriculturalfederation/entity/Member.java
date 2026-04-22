package school.hei.exam.agriculturalfederation.entity;

import java.time.LocalDate;
import java.util.List;

public class Member {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private GenderEnum gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private OccupationEnum occupation;
    private List<Reference> referees;
    
    
    public Member(String id, String firstName, String lastName, LocalDate birthday, GenderEnum gender, String address,
            String profession, String phoneNumber, String email, OccupationEnum occupation, List<Reference> referees) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.occupation = occupation;
        this.referees = referees;
    }
    

    public Member() {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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
    public OccupationEnum getOccupation() {
        return occupation;
    }
    public void setOccupation(OccupationEnum occupation) {
        this.occupation = occupation;
    }
    public List<Reference> getReferees() {
        return referees;
    }
    public void setReferees(List<Reference> referees) {
        this.referees = referees;
    }
    @Override
    public String toString() {
        return "Member [id=" + id + ", firstName=" + firstName + ", birthday=" + birthday + ", gender=" + gender
                + ", address=" + address + ", profession=" + profession + ", phoneNumber=" + phoneNumber + ", email="
                + email + ", occupation=" + occupation + ", referees=" + referees + "]";
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
}

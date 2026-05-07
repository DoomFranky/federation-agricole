package school.hei.exam.agriculturalfederation.dto;

public class MemberDescriptionDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;

    public MemberDescriptionDTO() {
    }

    public MemberDescriptionDTO id(String id) {
        this.id = id;
        return this;
    }

    public MemberDescriptionDTO firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public MemberDescriptionDTO lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public MemberDescriptionDTO email(String email) {
        this.email = email;
        return this;
    }

    public MemberDescriptionDTO occupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() {
        return occupation;
    }
}

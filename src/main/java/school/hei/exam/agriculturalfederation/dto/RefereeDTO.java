package school.hei.exam.agriculturalfederation.dto;

public class RefereeDTO {
    private String memberIdentifier;
    private String relationshipNature;

    public RefereeDTO() {
    }

    public RefereeDTO(String memberIdentifier, String relationshipNature) {
        this.memberIdentifier = memberIdentifier;
        this.relationshipNature = relationshipNature;
    }

    public String getMemberIdentifier() {
        return memberIdentifier;
    }

    public void setMemberIdentifier(String memberIdentifier) {
        this.memberIdentifier = memberIdentifier;
    }

    public String getRelationshipNature() {
        return relationshipNature;
    }

    public void setRelationshipNature(String relationshipNature) {
        this.relationshipNature = relationshipNature;
    }
}
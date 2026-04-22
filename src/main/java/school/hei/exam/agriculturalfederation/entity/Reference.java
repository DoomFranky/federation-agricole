package school.hei.exam.agriculturalfederation.entity;


public class Reference {
    private Member referee;
    private Member referedMember;
    private String relation;

    

    public Reference(Member referee, Member referedMember, String relation) {
        this.referee = referee;
        this.referedMember = referedMember;
        this.relation = relation;
    }

    
    public Member getReferee() {
        return referee;
    }

    public void setReferee(Member referee) {
        this.referee = referee;
    }

    public Reference() {
    }

    public Member getReferedMember() {
        return referedMember;
    }

    public void setReferedMember(Member referedMember) {
        this.referedMember = referedMember;
    }


    public String getRelation() {
        return relation;
    }


    public void setRelation(String relation) {
        this.relation = relation;
    }

    
}

package school.hei.exam.agriculturalfederation.entity;

public class Reference {
    private Member referencer;

    public Reference(Member referencer) {
        this.referencer = referencer;
    }

    public Member getReferencer() {
        return referencer;
    }

    public void setReferencer(Member referencer) {
        this.referencer = referencer;
    }

    public Reference() {
    }

    
}

package school.hei.exam.agriculturalfederation.dto;

public class CollectivityLocalStatisticsDTO {
    private MemberDescriptionDTO memberDescription;
    private Double earnedAmount;
    private Double unpaidAmount;
    private Double assiduityPercentage;

    public CollectivityLocalStatisticsDTO() {
    }

    public CollectivityLocalStatisticsDTO memberDescription(MemberDescriptionDTO memberDescription) {
        this.memberDescription = memberDescription;
        return this;
    }

    public CollectivityLocalStatisticsDTO earnedAmount(Double earnedAmount) {
        this.earnedAmount = earnedAmount;
        return this;
    }

    public CollectivityLocalStatisticsDTO unpaidAmount(Double unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
        return this;
    }

    public CollectivityLocalStatisticsDTO assiduityPercentage(Double assiduityPercentage) {
        this.assiduityPercentage = assiduityPercentage;
        return this;
    }

    public MemberDescriptionDTO getMemberDescription() {
        return memberDescription;
    }

    public Double getEarnedAmount() {
        return earnedAmount;
    }

    public Double getUnpaidAmount() {
        return unpaidAmount;
    }

    public Double getAssiduityPercentage() {
        return assiduityPercentage;
    }
}

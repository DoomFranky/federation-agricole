package school.hei.exam.agriculturalfederation.dto;

public class CollectivityOverallStatisticsDTO {
    private CollectivityInformationDTO collectivityInformation;
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
    private Double overallMemberAssiduityPercentage;

    public CollectivityOverallStatisticsDTO() {
    }

    public CollectivityOverallStatisticsDTO collectivityInformation(CollectivityInformationDTO collectivityInformation) {
        this.collectivityInformation = collectivityInformation;
        return this;
    }

    public CollectivityOverallStatisticsDTO newMembersNumber(Integer newMembersNumber) {
        this.newMembersNumber = newMembersNumber;
        return this;
    }

    public CollectivityOverallStatisticsDTO overallMemberCurrentDuePercentage(Double overallMemberCurrentDuePercentage) {
        this.overallMemberCurrentDuePercentage = overallMemberCurrentDuePercentage;
        return this;
    }

    public CollectivityOverallStatisticsDTO overallMemberAssiduityPercentage(Double overallMemberAssiduityPercentage) {
        this.overallMemberAssiduityPercentage = overallMemberAssiduityPercentage;
        return this;
    }

    public CollectivityInformationDTO getCollectivityInformation() {
        return collectivityInformation;
    }

    public Integer getNewMembersNumber() {
        return newMembersNumber;
    }

    public Double getOverallMemberCurrentDuePercentage() {
        return overallMemberCurrentDuePercentage;
    }

    public Double getOverallMemberAssiduityPercentage() {
        return overallMemberAssiduityPercentage;
    }
}

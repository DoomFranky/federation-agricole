package school.hei.exam.agriculturalfederation.dto;

public class MonthlyRecurrenceRuleDTO {
    private Integer weekOrdinal;
    private String dayOfWeek;

    public MonthlyRecurrenceRuleDTO() {}

    public MonthlyRecurrenceRuleDTO(Integer weekOrdinal, String dayOfWeek) {
        this.weekOrdinal = weekOrdinal;
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getWeekOrdinal() { return weekOrdinal; }
    public void setWeekOrdinal(Integer weekOrdinal) { this.weekOrdinal = weekOrdinal; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
}

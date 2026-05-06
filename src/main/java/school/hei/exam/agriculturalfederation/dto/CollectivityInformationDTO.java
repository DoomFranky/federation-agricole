package school.hei.exam.agriculturalfederation.dto;

public class CollectivityInformationDTO {
    private String name;
    private Integer number;

    public CollectivityInformationDTO() {
    }

    public CollectivityInformationDTO name(String name) {
        this.name = name;
        return this;
    }

    public CollectivityInformationDTO number(Integer number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }
}

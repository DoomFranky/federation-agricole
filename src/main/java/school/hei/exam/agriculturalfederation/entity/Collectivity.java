package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collectivity {
    private String id;
    private Integer number;
    private String name;
    private String location;
    private String agriculturalSpecialty;
    private LocalDate createdAt;
    private CollectivityStructure structure;
    private List<Member> members;
}
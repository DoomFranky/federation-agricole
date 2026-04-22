package school.hei.exam.agriculturalfederation.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collectivity {
    private String id;
    private Integer number;
    private String name;
    private String location;
    private String specialization;
    private List<Member> memberList;
    private CollectivityStructure structure;
}

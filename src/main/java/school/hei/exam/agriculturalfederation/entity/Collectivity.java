package school.hei.exam.agriculturalfederation.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collectivity {
    private String id;
    private String location;
    private String specialization;
    private List<Member> memberList;
    private CollectivityStructure structure;
}

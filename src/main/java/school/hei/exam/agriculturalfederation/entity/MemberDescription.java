package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDescription {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private OccupationEnum occupation;
}

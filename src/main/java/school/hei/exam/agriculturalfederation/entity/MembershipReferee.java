package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipReferee {
    private String id;
    private String refereeMemberId;
    private String refereeCollectivityId;
    private String relationshipNature;
}
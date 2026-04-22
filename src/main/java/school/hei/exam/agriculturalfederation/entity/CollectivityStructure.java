package school.hei.exam.agriculturalfederation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectivityStructure {
    private Member president;
    private Member vicePresident;
    private Member secretary;
    private Member treasurer;
}


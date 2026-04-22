package school.hei.exam.agriculturalfederation.DTO;

import java.util.List;

public record InputCollectivity(String location, List<String> members, CollectivityStructure structure, Boolean federationApproval) {
}

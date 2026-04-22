package school.hei.exam.agriculturalfederation.dto;

import java.util.List;

public record InputCollectivityDTO(
    String location,
    String specialization,
    List<String> members,
    CollectivityStructureDTO structure,
    Boolean federationApproval
) {}
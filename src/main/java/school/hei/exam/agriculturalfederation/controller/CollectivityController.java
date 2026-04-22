package school.hei.exam.agriculturalfederation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.agriculturalfederation.dto.InputCollectivityDTO;

import java.util.List;

@RestController
public class CollectivityController {

    private final school.hei.exam.agriculturalfederation.service.CollectivityService collectivityService;

    public CollectivityController(school.hei.exam.agriculturalfederation.service.CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping("/collectivities")
    public ResponseEntity<?> createCollectivity(@RequestBody List<InputCollectivityDTO> newCollectivityList) {
        List<school.hei.exam.agriculturalfederation.entity.Collectivity> result = collectivityService.createCollectivity(newCollectivityList);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/collectivities/{id}")
    public ResponseEntity<?> attributeIdentity(@org.springframework.web.bind.annotation.PathVariable String id, @RequestBody school.hei.exam.agriculturalfederation.dto.IdentityCollectivityDTO identity) {
        school.hei.exam.agriculturalfederation.entity.Collectivity result = collectivityService.attributeIdentity(id, identity);
        return ResponseEntity.ok(result);
    }
}

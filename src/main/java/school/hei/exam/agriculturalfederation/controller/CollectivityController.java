package school.hei.exam.agriculturalfederation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.agriculturalfederation.dto.InputCollectivityDTO;

import java.util.List;

@RestController
public class CollectivityController {

    @PostMapping("/collectivity")
    public ResponseEntity<?> createCollectivity(@RequestBody List<InputCollectivityDTO> newCollectivityList)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}

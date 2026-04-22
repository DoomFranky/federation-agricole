package school.hei.exam.agriculturalfederation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.agriculturalfederation.DTO.InputCollectivity;

import java.util.List;

@RestController
public class CollectivityController {

    @PostMapping("/collectivity")
    public ResponseEntity<?> createCollectivity(@RequestBody List<InputCollectivity> newCollectivityList)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}

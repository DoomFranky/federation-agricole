package school.hei.exam.agriculturalfederation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.agriculturalfederation.dto.CreateMembershipFeeDTO;
import school.hei.exam.agriculturalfederation.dto.MembershipFeeDTO;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.service.DuesService;

import java.util.List;

@RestController
public class MembershipFeeController {
    private final DuesService duesService;

    public MembershipFeeController(DuesService duesService) {
        this.duesService = duesService;
    }

    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> getFees(@PathVariable String id) {
        try {
            List<MembershipFeeDTO> fees = duesService.getFees(id);
            return ResponseEntity.ok(fees);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> createFees(@PathVariable String id, @RequestBody List<CreateMembershipFeeDTO> dtos) {
        try {
            List<MembershipFeeDTO> created = duesService.createFees(id, dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
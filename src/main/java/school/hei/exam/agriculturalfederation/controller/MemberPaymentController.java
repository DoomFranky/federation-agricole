package school.hei.exam.agriculturalfederation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.agriculturalfederation.dto.CreateMemberPaymentDTO;
import school.hei.exam.agriculturalfederation.dto.MemberPaymentDTO;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.service.PaymentService;

import java.util.List;

@RestController
public class MemberPaymentController {
    private final PaymentService paymentService;

    public MemberPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<?> createPayments(@PathVariable String id, @RequestBody List<CreateMemberPaymentDTO> dtos) {
        try {
            List<MemberPaymentDTO> created = paymentService.createPayments(id, dtos);
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
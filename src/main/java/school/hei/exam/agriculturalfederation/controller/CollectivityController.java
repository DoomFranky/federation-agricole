package school.hei.exam.agriculturalfederation.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.agriculturalfederation.dto.CollectivityTransactionDTO;
import school.hei.exam.agriculturalfederation.dto.FinancialAccountDTO;
import school.hei.exam.agriculturalfederation.dto.IdentityCollectivityDTO;
import school.hei.exam.agriculturalfederation.dto.InputCollectivityDTO;
import school.hei.exam.agriculturalfederation.entity.Collectivity;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.ConflictException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.service.CollectivityService;
import school.hei.exam.agriculturalfederation.service.TransactionService;
import school.hei.exam.agriculturalfederation.service.TreasuryAccountService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CollectivityController {
    private final CollectivityService collectivityService;
    private final TransactionService transactionService;
    private final TreasuryAccountService treasuryAccountService;

    public CollectivityController(CollectivityService collectivityService, TransactionService transactionService, TreasuryAccountService treasuryAccountService) {
        this.collectivityService = collectivityService;
        this.transactionService = transactionService;
        this.treasuryAccountService = treasuryAccountService;
    }

    @PostMapping("/collectivities")
    public ResponseEntity<?> createCollectivity(@RequestBody List<InputCollectivityDTO> dto) {
        try {
            List<Collectivity> created = collectivityService.createCollectivity(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<?> assignIdentity(@PathVariable String id, @RequestBody IdentityCollectivityDTO identity) {
        try {
            collectivityService.assignIdentity(id, identity);
            Collectivity updated = collectivityService.getCollectivity(id);
            return ResponseEntity.ok(updated);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<?> getTransactions(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        try {
            List<CollectivityTransactionDTO> transactions = transactionService.getTransactions(id, from, to);
            return ResponseEntity.ok(transactions);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}")
    public ResponseEntity<?> getCollectivity(@PathVariable String id) {
        try {
            Collectivity collectivity = collectivityService.getCollectivity(id);
            return ResponseEntity.ok(collectivity);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities")
    public ResponseEntity<?> getAllCollectivities() {
        try {
            List<Collectivity> collectivities = collectivityService.getAllCollectivities();
            return ResponseEntity.ok(collectivities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at
    ) {
        try {
            List<FinancialAccountDTO> accounts = treasuryAccountService.getFinancialAccounts(id, at);
            return ResponseEntity.ok(accounts);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
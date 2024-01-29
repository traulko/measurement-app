package traulka.test.utilitybill.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import traulka.test.utilitybill.dto.CreatePayerDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.service.PayerService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payer")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PayerController {

    PayerService service;

    @GetMapping
    @Operation(summary = "Get a payer list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the payer list"),
            @ApiResponse(responseCode = "500", description = "Some paging arg is not correct", content = @Content)
    })
    // need to add paging args validation in feature
    public ResponseEntity<Page<PayerDto>> findAllPaging(
            @RequestParam(required = false, defaultValue = "0") final Integer page,
            @RequestParam(required = false, defaultValue = "5") final Integer size,
            @RequestParam(required = false, defaultValue = "id") final String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping
    @Operation(summary = "Register a new payer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payer was registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<PayerDto> registerNewPayer(@Valid @RequestBody final CreatePayerDto createPayerDto) {
        return ResponseEntity.ok(service.createNewPayer(createPayerDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the payer"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payer not found", content = @Content)
    })
    public ResponseEntity<PayerDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}

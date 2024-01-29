package traulka.test.utilitybill.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import traulka.test.utilitybill.dto.CreateMeasurementDto;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.service.MeasurementService;
import traulka.test.utilitybill.service.facade.CreateNewMeasurementFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/measurement")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MeasurementController {

    MeasurementService service;

    CreateNewMeasurementFacade createNewMeasurementFacade;

    @GetMapping("/{id}")
    @Operation(summary = "Get a measurement by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the measurement"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Measurement not found", content = @Content)
    })
    public ResponseEntity<MeasurementDto> findById(@Parameter(description = "id of measurement to be searched")
                                                   @PathVariable final Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/history")
    @Operation(summary = "Get a measurement history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the measurement history"),
            @ApiResponse(responseCode = "500", description = "Some paging arg is not correct", content = @Content)
    })
    // need to add paging args validation in feature
    public ResponseEntity<Page<MeasurementDto>> findAllPaging(
            @RequestParam(required = false, defaultValue = "0") final Integer page,
            @RequestParam(required = false, defaultValue = "5") final Integer size,
            @RequestParam(required = false, defaultValue = "id") final String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping
    @Operation(summary = "Create a new measurement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurement was created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payer not found", content = @Content)
    })
    public ResponseEntity<MeasurementDto> createNewMeasurement(
            @Valid @RequestBody final CreateMeasurementDto createMeasurementDto) {
        return ResponseEntity.ok(createNewMeasurementFacade.createNewMeasurement(createMeasurementDto));
    }
}

package traulka.test.utilitybill.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import traulka.test.utilitybill.dto.CreateMeasurementDto;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.entity.type.MeasurementType;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.service.MeasurementService;
import traulka.test.utilitybill.service.facade.CreateNewMeasurementFacade;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasurementController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class MeasurementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeasurementService service;

    @MockBean
    CreateNewMeasurementFacade facade;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldReturn200CodeWhenMeasurementSuccessfullyFound() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 1, 1, 1);

        when(service.findById(1L)).thenReturn(MeasurementDto.builder()
                .setId(1L)
                .setType(MeasurementType.GAS)
                .setValue(10.0)
                .setCreatedTime(dateTime)
                .setPayer(PayerDto.builder()
                        .setId(1L)
                        .setFirstName("Ivan")
                        .setLastName("Ivanovich")
                        .setCreatedTime(dateTime)
                        .build())
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/measurement/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value(MeasurementType.GAS.name()))
                .andExpect(jsonPath("$.value").value(10.0))
                .andExpect(jsonPath("$.createdTime").value("2023-01-01T01:01:01"))
                .andExpect(jsonPath("$.payer.id").value(1L))
                .andExpect(jsonPath("$.payer.firstName").value("Ivan"))
                .andExpect(jsonPath("$.payer.lastName").value("Ivanovich"))
                .andExpect(jsonPath("$.payer.createdTime").value("2023-01-01T01:01:01"));
    }

    @Test
    public void shouldReturn404CodeWhenMeasurementNotFound() throws Exception {
        when(service.findById(1L)).thenThrow(DataNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/measurement/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200CodeWhenFindAllMeasurements() throws Exception {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));

        when(service.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(
                MeasurementDto.builder()
                        .setId(1L)
                        .build(),
                MeasurementDto.builder()
                        .setId(2L)
                        .build())));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/measurement/history")
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(5))
                .param("sort", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    public void shouldReturn200CodeWhenFindAllByPayerIdMeasurements() throws Exception {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));

        when(service.findAllByPayerId(pageable, 1L)).thenReturn(new PageImpl<>(Arrays.asList(
                MeasurementDto.builder()
                        .setId(1L)
                        .setPayer(PayerDto.builder()
                                .setId(1L)
                                .build())
                        .build(),
                MeasurementDto.builder()
                        .setId(2L)
                        .setPayer(PayerDto.builder()
                                .setId(1L)
                                .build())
                        .build())));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/measurement/" + 1L + "/history")
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(5))
                        .param("sort", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[0].payer.id").value(1L))
                .andExpect(jsonPath("$.content[1].payer.id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    public void shouldReturn200CreateNewMeasurementSuccessfully() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 1, 1, 1);

        CreateMeasurementDto createMeasurementDto = CreateMeasurementDto.builder()
                .setUserId(1L)
                .setValue(10.0)
                .setType("GAS")
                .build();
        MeasurementDto savedMeasurementAfterMappingToDto = MeasurementDto.builder()
                .setId(1L)
                .setType(MeasurementType.GAS)
                .setValue(10.0)
                .setCreatedTime(dateTime)
                .setPayer(PayerDto.builder()
                        .setId(1L)
                        .build())
                .build();

        when(facade.createNewMeasurement(createMeasurementDto)).thenReturn(savedMeasurementAfterMappingToDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/measurement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMeasurementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value(MeasurementType.GAS.name()))
                .andExpect(jsonPath("$.value").value(10.0))
                .andExpect(jsonPath("$.createdTime").value("2023-01-01T01:01:01"))
                .andExpect(jsonPath("$.payer.id").value(1L));
    }

    @Test
    public void shouldReturn400CreateNewMeasurementWithoutRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/measurement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
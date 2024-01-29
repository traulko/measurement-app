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
import traulka.test.utilitybill.dto.CreatePayerDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.service.PayerService;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PayerController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class PayerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PayerService service;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldReturn200CodeWhenPayerSuccessfullyFound() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 1, 1, 1);

        when(service.findById(1L)).thenReturn(PayerDto.builder()
                .setId(1L)
                .setFirstName("Ivan")
                        .setLastName("Ivanovich")
                        .setCreatedTime(dateTime)
                        .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payer/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanovich"))
                .andExpect(jsonPath("$.createdTime").value("2023-01-01T01:01:01"));
    }

    @Test
    public void shouldReturn404CodeWhenPayerNotFound() throws Exception {
        when(service.findById(1L)).thenThrow(DataNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payer/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200CodeWhenFindAllPayers() throws Exception {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));

        when(service.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(
                PayerDto.builder()
                        .setId(1L)
                        .build(),
                PayerDto.builder()
                        .setId(2L)
                        .build())));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payer")
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(5))
                        .param("sort", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    public void shouldReturn200CreateNewPayerSuccessfully() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 1, 1, 1);

        CreatePayerDto createMeasurementDto = CreatePayerDto.builder()
                .setFirstName("Ivan")
                .setLastName("Ivanovich")
                .build();
        PayerDto savedPayerAfterMappingToDto = PayerDto.builder()
                .setId(1L)
                .setFirstName("Ivan")
                .setLastName("Ivanovich")
                .setCreatedTime(dateTime)
                .build();

        when(service.createNewPayer(createMeasurementDto)).thenReturn(savedPayerAfterMappingToDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMeasurementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanovich"))
                .andExpect(jsonPath("$.createdTime").value("2023-01-01T01:01:01"));
    }

    @Test
    public void shouldReturn400CreateNewPayerWithoutRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
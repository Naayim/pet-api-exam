package com.example.exam.controller;

import com.example.exam.controller.PetController;
import com.example.exam.model.PetCreatedResponse;
import com.example.exam.model.PetRequest;
import com.example.exam.model.PetResponse;
import com.example.exam.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Test
    void getPet_returns200WithBody() throws Exception {
        when(petService.getPet(1L)).thenReturn(new PetResponse(1L, "Firulais", "available"));

        mockMvc.perform(get("/api/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("available"));
    }

    @Test
    void getPet_returns404WhenPetstoreNotFound() throws Exception {
        when(petService.getPet(99L))
                .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        mockMvc.perform(get("/api/pet/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pet no encontrado"));
    }

    @Test
    void createPet_returns201WithBody() throws Exception {
        UUID txId = UUID.randomUUID();
        when(petService.createPet(any(PetRequest.class)))
                .thenReturn(new PetCreatedResponse(txId, LocalDateTime.now(), "available", "Michi"));

        mockMvc.perform(post("/api/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id": 5, "name": "Michi", "status": "available"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(txId.toString()))
                .andExpect(jsonPath("$.name").value("Michi"))
                .andExpect(jsonPath("$.status").value("available"))
                .andExpect(jsonPath("$.dateCreated").exists());
    }
}
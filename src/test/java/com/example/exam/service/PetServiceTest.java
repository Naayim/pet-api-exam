package com.example.exam.service;

import com.example.exam.client.PetstoreClient;
import com.example.exam.client.dto.PetstorePet;
import com.example.exam.model.PetCreatedResponse;
import com.example.exam.model.PetRequest;
import com.example.exam.model.PetResponse;
import com.example.exam.service.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetstoreClient petstoreClient;

    @InjectMocks
    private PetService petService;

    @Test
    void getPet_mapsPetstoreResponse(){
        when(petstoreClient.getPetById(10L))
                .thenReturn(new PetstorePet(10L, "Firulais", "available"));

        PetResponse response = petService.getPet(10L);

        assertThat(response).isEqualTo(new PetResponse(10L, "Firulais", "available"));
    }

    @Test
    void getPet_propagatesNotFound(){
        when(petstoreClient.getPetById(99L))
                .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        assertThatThrownBy(() -> petService.getPet(99L))
                .isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void createPet_generatesUuidV4AndCurrentDate(){
        PetRequest request = new PetRequest(20L, "Michi", "pending");
        PetstorePet expectedToSend = new PetstorePet(20L, "Michi", "pending");
        when(petstoreClient.createPet(expectedToSend)).thenReturn(expectedToSend);

        LocalDateTime before = LocalDateTime.now();
        PetCreatedResponse response = petService.createPet(request);
        LocalDateTime after = LocalDateTime.now();

        verify(petstoreClient).createPet(expectedToSend);
        assertThat(response.transactionId()).isNotNull();
        assertThat(response.transactionId().version()).isEqualTo(4);
        assertThat(response.dateCreated()).isBetween(before, after);
        assertThat(response.name()).isEqualTo("Michi");
        assertThat(response.status()).isEqualTo("pending");
    }
}
package com.example.exam.client;

import com.example.exam.client.dto.PetstorePet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PetstoreClient {

    private final RestClient restClient;

    public PetstoreClient(RestClient.Builder builder, @Value("${petstore.base-url}") String baseUrl){
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    //el inyectado en esta version da el autoconfigurado para no tener que hacer todo el .creat()

    public PetstorePet getPetById(Long petId){
        return restClient.get()
                .uri("/pet/{petId}", petId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(PetstorePet.class);
    }

    public PetstorePet createPet(PetstorePet pet){
        return restClient.post()
                .uri("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(pet)
                .retrieve()
                .body(PetstorePet.class);
    }

}

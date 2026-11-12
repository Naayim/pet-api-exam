package com.example.exam.service;

import com.example.exam.client.PetstoreClient;
import com.example.exam.client.dto.PetstorePet;
import com.example.exam.model.PetCreatedResponse;
import com.example.exam.model.PetRequest;
import com.example.exam.model.PetResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PetService {

    private static final Logger log = LoggerFactory.getLogger(PetService.class);

    private final PetstoreClient petstoreClient;

    public PetService(PetstoreClient petstoreClient){
        this.petstoreClient = petstoreClient;
    }

    public PetResponse getPet(Long petId){
        PetstorePet pet = petstoreClient.getPetById(petId);
        log.info("Pet obtenido de la Petstore: {}", pet);
        return new PetResponse(pet.id(), pet.name(), pet.status());
    }

    public PetCreatedResponse createPet(PetRequest request){
        PetstorePet toCreate = new PetstorePet(request.id(), request.name(), request.status());
        PetstorePet created = petstoreClient.createPet(toCreate);
        log.info("Per creado en Pettstore: {}", created);

        return new PetCreatedResponse(
                UUID.randomUUID(),
                LocalDateTime.now(),
                created.status(),
                created.name());
    }

}

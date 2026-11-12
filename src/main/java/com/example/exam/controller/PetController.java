package com.example.exam.controller;

import com.example.exam.model.PetCreatedResponse;
import com.example.exam.model.PetRequest;
import com.example.exam.model.PetResponse;
import com.example.exam.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService){
        this.petService = petService;
    }

    @GetMapping("/{petId}")
    public PetResponse getPet(@PathVariable Long petId){
        return petService.getPet(petId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetCreatedResponse createdPet(@RequestBody PetRequest request){
        return petService.createPet(request);
    }

}

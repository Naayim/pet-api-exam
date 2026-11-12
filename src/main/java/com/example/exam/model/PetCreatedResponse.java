package com.example.exam.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PetCreatedResponse(UUID transactionId, LocalDateTime dateCreated, String status, String name) {
}

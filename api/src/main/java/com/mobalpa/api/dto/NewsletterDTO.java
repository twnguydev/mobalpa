package com.mobalpa.api.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class NewsletterDTO {
    private UUID id;
    private String email;
}
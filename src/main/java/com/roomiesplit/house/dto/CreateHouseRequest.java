package com.roomiesplit.house.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateHouseRequest(

        @NotBlank
        String name

) {}
package com.roomiesplit.house.dto;

import jakarta.validation.constraints.NotBlank;

public record JoinHouseRequest(

        @NotBlank
        String inviteCode

) {}
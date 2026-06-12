package com.roomiesplit.house.controller;

import com.roomiesplit.common.response.ApiResponse;
import com.roomiesplit.house.dto.*;
import com.roomiesplit.house.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ApiResponse<CreateHouseResponse>
    createHouse(
            @Valid
            @RequestBody
            CreateHouseRequest request,

            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "House created successfully",
                houseService.createHouse(
                        authentication.getName(),
                        request
                )
        );
    }

    @PostMapping("/join")
    public ApiResponse<Void> joinHouse(
            @Valid
            @RequestBody
            JoinHouseRequest request,

            Authentication authentication
    ) {
        System.out.println("JOIN HOUSE HIT"); // testing
        houseService.joinHouse(
                authentication.getName(),
                request
        );

        return new ApiResponse<>(
                true,
                "Joined house successfully",
                null
        );
    }

    @GetMapping("/me")
    public ApiResponse<HouseDetailsResponse>
    getMyHouse(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "House fetched successfully",
                houseService.getMyHouse(
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/leave")
    public ApiResponse<Void> leaveHouse(
            Authentication authentication
    ) {

        houseService.leaveHouse(
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Left house successfully",
                null
        );
    }

    @GetMapping("/me/members")
    public ApiResponse<List<MemberResponse>>
    getMembers(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Members fetched successfully",
                houseService.getMembers(
                        authentication.getName()
                )
        );
    }
}
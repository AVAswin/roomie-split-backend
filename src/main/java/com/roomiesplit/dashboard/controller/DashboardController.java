package com.roomiesplit.dashboard.controller;

import com.roomiesplit.common.response.ApiResponse;
import com.roomiesplit.dashboard.dto.DashboardSummaryResponse;
import com.roomiesplit.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary(Authentication authentication) {
        return new ApiResponse<>(
                true,
                "Dashboard fetched successfully",
                dashboardService.getSummary(
                        authentication.getName()
                )
        );
    }
}

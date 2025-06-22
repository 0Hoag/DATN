package com.fpl.datn.controller;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.response.*;
import com.fpl.datn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/List")
    public ApiResponse<DashboardResponse> getSummary() {
        return ApiResponse.<DashboardResponse>builder()
                .code(1000)
                .result(dashboardService.getShow())
                .build();
    }

}

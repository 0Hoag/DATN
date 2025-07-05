package com.fpl.datn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.service.SendMailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMailController {
    SendMailService mailService;

    @GetMapping("/{id}")
    ApiResponse<Void> getMethodName(@PathVariable Integer id) throws Exception {
        mailService.sendInvoiceToUser(id);
        return ApiResponse.<Void>builder().message("Send mail sucess!").build();
    }
}

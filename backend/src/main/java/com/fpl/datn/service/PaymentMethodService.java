package com.fpl.datn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.PaymentMethodResponse;
import com.fpl.datn.mapper.PaymentMethodMapper;
import com.fpl.datn.repository.PaymentMethodRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMethodService {
    PaymentMethodRepository repository;
    PaymentMethodMapper mapper;

    public List<PaymentMethodResponse> getAll() {
        return repository.findAll().stream()
                .map(method -> mapper.toLogResponse(method))
                .toList();
    }
}

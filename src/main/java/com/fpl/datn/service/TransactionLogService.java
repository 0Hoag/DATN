package com.fpl.datn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpl.datn.models.TransactionLog;
import com.fpl.datn.repository.TransactionLogRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionLogService {
    TransactionLogRepository repository;

    public List<TransactionLog> Get() {

        return repository.findAll();
    }
}

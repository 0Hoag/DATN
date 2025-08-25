package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.ActivityRequest;
import com.fpl.datn.dto.response.ActivityLogResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.ActivityLogMapper;
import com.fpl.datn.repository.ActivityRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivitylogService {
    AuthenticationService authenticationService;
    ActivityLogMapper mapper;
    ActivityRepository repo;
    UserRepository userRepository;

    public void create(ActivityRequest request) {
        var activity = mapper.toActivityLog(request);
        Integer userId = authenticationService.extractUserIdFromSecurityContext();
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        activity.setUserAction(user);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        repo.save(activity);
    }

    public PageResponse<ActivityLogResponse> Get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream().map(mapper::toActivity).collect(Collectors.toList());

        return PageResponse.<ActivityLogResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
}

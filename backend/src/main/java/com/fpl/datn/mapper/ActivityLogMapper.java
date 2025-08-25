package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.request.ActivityRequest;
import com.fpl.datn.dto.response.ActivityLogResponse;
import com.fpl.datn.models.ActivityLog;

@Mapper(
        componentModel = "spring",
        uses = {PermissionMapper.class})
public interface ActivityLogMapper {
    @Mapping(target = "userId", source = "userAction.id")
    @Mapping(target = "fullName", source = "userAction.fullName")
    ActivityLogResponse toActivity(ActivityLog entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    ActivityLog toActivityLog(ActivityRequest request);
}

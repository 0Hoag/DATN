package com.fpl.datn.dto.request;

import com.fpl.datn.enums.ActionActicityLog;
import com.fpl.datn.enums.ActionActicityModule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityRequest {
    ActionActicityLog action;
    String description;
    ActionActicityModule module;
    Integer objectID;
}

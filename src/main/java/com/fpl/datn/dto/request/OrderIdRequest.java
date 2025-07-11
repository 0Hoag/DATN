package com.fpl.datn.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderIdRequest {
    List<Integer> orderIds;
}

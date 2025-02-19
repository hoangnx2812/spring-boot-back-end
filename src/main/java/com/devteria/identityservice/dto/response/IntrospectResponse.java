package com.devteria.identityservice.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class IntrospectResponse {
    boolean valid;
}

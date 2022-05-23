package com.example.arifaservice.service.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import rolengi.platform.service.Query;

@Data
@AllArgsConstructor
public class GetUserSummaryByIdQuery implements Query {
    private String userId;
}

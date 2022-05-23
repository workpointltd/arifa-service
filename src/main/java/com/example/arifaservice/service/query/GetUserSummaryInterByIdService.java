package com.example.arifaservice.service.query;

import com.example.arifaservice.model.UserSummaryDto;
import org.springframework.stereotype.Service;
import rolengi.platform.decorator.query.QueryBaseService;

@Service
public class GetUserSummaryInterByIdService extends QueryBaseService<GetUserSummaryByIdQuery, UserSummaryDto> {
}

package com.demo.language_booking.users;

import com.demo.language_booking.users.dto.UserCreateRequest;
import com.demo.language_booking.users.dto.UserPublicResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    User mapToUser(UserCreateRequest userCreateRequest);
    UserPublicResponse mapToUserPublicResponse(User user);
}

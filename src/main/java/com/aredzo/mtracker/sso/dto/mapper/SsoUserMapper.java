package com.aredzo.mtracker.sso.dto.mapper;


import com.aredzo.mtracker.sso.dto.UserResponse;
import com.aredzo.mtracker.sso.entity.SsoUserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface SsoUserMapper {

    UserResponse ssoUserEntityToResponse(SsoUserEntity ssoUserEntity);
}

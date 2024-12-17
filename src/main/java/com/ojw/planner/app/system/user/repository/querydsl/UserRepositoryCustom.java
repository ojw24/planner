package com.ojw.planner.app.system.user.repository.querydsl;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserDto.UserSimpleDto;
import com.ojw.planner.app.system.user.domain.dto.UserFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    Page<User> findAll(UserFindDto findDto, Pageable pageable);

    List<UserSimpleDto> findSimples(UserFindDto findDto, String userId);

}

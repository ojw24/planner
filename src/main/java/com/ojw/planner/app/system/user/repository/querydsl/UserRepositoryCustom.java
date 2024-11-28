package com.ojw.planner.app.system.user.repository.querydsl;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserFindDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> findAll(UserFindDTO findDTO, Pageable pageable);

}

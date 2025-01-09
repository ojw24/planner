package com.ojw.planner.app.system.user.repository.setting;

import com.ojw.planner.app.system.user.domain.setting.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
}

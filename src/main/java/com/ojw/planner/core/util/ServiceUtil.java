package com.ojw.planner.core.util;

import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.core.enumeration.system.user.Authority;
import com.ojw.planner.exception.ResponseException;
import org.springframework.http.HttpStatus;

public final class ServiceUtil {

    public static void validateOwner(String userId) {
        CustomUserDetails cu = CustomUserDetails.getDetails();
        if(!checkAdmin(cu)) {
            if(!cu.getUserId().equalsIgnoreCase(userId))
                throw new ResponseException("not the current user's request", HttpStatus.FORBIDDEN);
        }
    }

    public static void validateOwnerByUuid(String uuid) {
        CustomUserDetails cu = CustomUserDetails.getDetails();
        if(!checkAdmin(cu)) {
            if(!cu.getUuid().equalsIgnoreCase(uuid))
                throw new ResponseException("not the current user's request", HttpStatus.FORBIDDEN);
        }
    }

    private static boolean checkAdmin(CustomUserDetails cu) {
        return cu.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase(Authority.ADMIN.getDescription()));
    }

}

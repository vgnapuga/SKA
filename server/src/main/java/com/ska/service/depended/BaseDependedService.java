package com.ska.service.depended;


import java.util.Base64;

import com.ska.exception.AccessDeniedException;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.model.syncable.BaseSyncableModel;
import com.ska.model.user.User;
import com.ska.service.BaseService;
import com.ska.service.UserService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class BaseDependedService extends BaseService {

    private final UserService userService;

    private static final String DATA_NULL_MESSAGE = "Data is <null>";
    private static final String DATA_BLANK_MESSAGE = "Data is <blank>";
    private static final String DATA_NOT_BASE64_MESSAGE = "Data is not <Base64>";

    protected final User checkUserExistence(Long userId) {
        return userService.checkUserExistence(userId);
    }

    protected final byte[] decodeBase64(String coded) {
        if (coded == null)
            throw new BusinessRuleViolationException(DATA_NULL_MESSAGE);
        else if (coded.isBlank())
            throw new BusinessRuleViolationException(DATA_BLANK_MESSAGE);

        try {
            return Base64.getDecoder().decode(coded);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleViolationException(DATA_NOT_BASE64_MESSAGE);
        }
    }

    protected final void checkPermissionToAccess(Long userId, final BaseSyncableModel entity) {
        if (!userId.equals(entity.getUser().getId()))
            throw new AccessDeniedException("Permission denied for user with ID: " + userId);
    }

}

package com.ska.service.depended;

import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.service.BaseService;
import com.ska.service.UserService;
import com.ska.util.LogTemplates;


@Slf4j
public abstract class DependedService extends BaseService {

    protected final UserService userService;


    protected DependedService(UserService userService) {
        log.debug(this.getClass().getSimpleName() + " initialization");
        this.userService = userService;
    }

    
    protected final User checkUserExistence(final Long userId) {
        log.debug(LogTemplates.checkStartLog("User existence"));

        User user = userService.getUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format(
                                "User id=%d not found to create note",
                                userId
                        )
                )
        );

        return user;
    }


    protected final byte[] decodeBase64(final String coded) {
        if (coded == null)
            throw new BusinessRuleViolationException("Data is <null>");
        else if (coded.isBlank())
            throw new BusinessRuleViolationException("Data is <blank>");

        try {
            return Base64.getDecoder().decode(coded);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleViolationException("Data is not <Base64>");
        }
    }

}

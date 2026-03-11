package com.ska.service;


import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.dto.entity.request.EntityCreateRequest;
import com.ska.dto.entity.request.EntityUpdateAllRequest;
import com.ska.dto.entity.request.EntityUpdateContentRequest;
import com.ska.dto.entity.request.EntityUpdateMetadataRequest;
import com.ska.exception.AccessDeniedException;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.syncable.Syncable;
import com.ska.model.syncable.vo.EncryptedContent;
import com.ska.model.syncable.vo.EncryptedMetadata;
import com.ska.model.user.User;
import com.ska.repository.EntityRepository;
import com.ska.service.contract.EntityService;
import com.ska.service.contract.UserService;
import com.ska.util.LogTemplates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public final class EntityServiceImpl extends BaseService implements EntityService {

    private final EntityRepository entityRepository;
    private final UserService userService;

    private static final String DATA_NULL_MESSAGE = "Data is <null>";
    private static final String DATA_BLANK_MESSAGE = "Data is <blank>";
    private static final String DATA_NOT_BASE64_MESSAGE = "Data is not <Base64>";

    // =========== Helper methods ========== //

    private final User checkUserExistenceAndGet(Long userId) {
        return userService.checkUserExistenceAndGet(userId);
    }

    private final byte[] decodeBase64(String coded) {
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

    private final void checkPermissionToAccess(Long userId, final Syncable entity) {
        if (!userId.equals(entity.getOwner().getId()))
            throw new AccessDeniedException("Permission denied for user with id=" + userId);
    }

    private final Syncable checkEntityExistenceAndGet(UUID uuid) {
        return entityRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Syncable with uuid=%s not found", uuid)));
    }

    // ===================================== //

    @Transactional
    @Override
    public Syncable create(Long userId, UUID uuid, EntityCreateRequest request) {
        log.info("Creating entity for user with ID: {}", userId);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.UserService.checkUserExistenceStartLog());
        User user = checkUserExistenceAndGet(userId);

        log.debug(LogTemplates.EntityService.checkBase64StartLog("Syncable metadata"));
        byte[] decodedMetadata = decodeBase64(request.encryptedMetadata());

        log.debug(LogTemplates.EntityService.checkBase64StartLog("Syncable content"));
        byte[] decodedContent = decodeBase64(request.encryptedContent());

        log.debug(LogTemplates.validationStartLog("Syncable metadata"));
        EncryptedMetadata metadata = new EncryptedMetadata(decodedMetadata);

        log.debug(LogTemplates.validationStartLog("Syncable content"));
        EncryptedContent content = new EncryptedContent(decodedContent);

        Syncable entity = new Syncable(user, uuid, metadata, content);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Syncable savedSyncable = entityRepository.save(entity);

        log.info("Syncable created successfully for user with ID: {}", userId);
        return savedSyncable;
    }

    // TODO: добавить пагинацию
    @Transactional(readOnly = true)
    @Override
    public List<Syncable> getAll(Long userId) {
        log.info("Getting all entities for user with ID: {}", userId);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        List<Syncable> retrievedEntities = entityRepository.getAllByUserId(userId);

        log.info("Retrieved {} entities for user with ID: {}", retrievedEntities.size(), userId);
        return retrievedEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public Syncable getByUuid(Long userId, UUID entityUuid) {
        log.info("Getting entity with UUID: {} for user with ID: {}", entityUuid, userId);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Syncable retrievedEntity = checkEntityExistenceAndGet(entityUuid);

        log.debug(LogTemplates.EntityService.checkPermissionStartLog("Get"));
        checkPermissionToAccess(userId, retrievedEntity);

        log.info("Syncable with UUID: {} for user with ID: {} retrieved successfully", entityUuid, userId);
        return retrievedEntity;
    }

    @Transactional
    @Override
    public Syncable updateMetadataAndContent(Long userId, UUID entityUuid, EntityUpdateAllRequest request) {
        log.info("Updating entity metadata and content for user with ID: {} and entity UUID: {}", userId, entityUuid);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.EntityService.checkBase64StartLog("New entity metadata"));
        byte[] decodedNewMetadata = decodeBase64(request.encryptedNewMetadata());

        log.debug(LogTemplates.EntityService.checkBase64StartLog("New entity content"));
        byte[] decodedNewContent = decodeBase64(request.encryptedNewContent());

        log.debug(LogTemplates.validationStartLog("New entity metadata"));
        EncryptedMetadata newMetadata = new EncryptedMetadata(decodedNewMetadata);

        log.debug(LogTemplates.validationStartLog("New entity content"));
        EncryptedContent newContent = new EncryptedContent(decodedNewContent);

        log.debug(LogTemplates.checkStartLog("Entity existence"));
        Syncable retrievedEntity = checkEntityExistenceAndGet(entityUuid);

        retrievedEntity.changeMetadata(newMetadata);
        retrievedEntity.changeContent(newContent);

        log.debug(LogTemplates.EntityService.checkPermissionStartLog("Update entity metadata and content"));
        checkPermissionToAccess(userId, retrievedEntity);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        entityRepository.save(retrievedEntity);

        log.info(
                "Syncable metadata and content was updated for user with ID: {} and entity UUID: {}",
                userId,
                entityUuid);
        return retrievedEntity;
    }

    @Transactional
    @Override
    public Syncable updateMetadata(Long userId, UUID entityUuid, EntityUpdateMetadataRequest request) {
        log.info("Updating entity metadata for user with ID: {} and entity UUID: {}", userId, entityUuid);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.EntityService.checkBase64StartLog("New entity metadata"));
        byte[] decodedNewMetadata = decodeBase64(request.encryptedNewMetadata());

        log.debug(LogTemplates.validationStartLog("New entity metadata"));
        EncryptedMetadata newMetadata = new EncryptedMetadata(decodedNewMetadata);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Syncable retrievedEntity = checkEntityExistenceAndGet(entityUuid);

        retrievedEntity.changeMetadata(newMetadata);

        log.debug(LogTemplates.EntityService.checkPermissionStartLog("Update entity metadata"));
        checkPermissionToAccess(userId, retrievedEntity);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        entityRepository.save(retrievedEntity);

        log.info("Syncable metadata was updated for user with ID: {} and entity UUID: {}", userId, entityUuid);
        return retrievedEntity;
    }

    @Transactional
    @Override
    public Syncable updateContent(Long userId, UUID entityUuid, EntityUpdateContentRequest request) {
        log.info("Updating entity content for user with ID: {} and entity UUID: {}", userId, entityUuid);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.EntityService.checkBase64StartLog("New entity content"));
        byte[] decodedNewContent = decodeBase64(request.encryptedNewContent());

        log.debug(LogTemplates.validationStartLog("New entity content"));
        EncryptedContent newContent = new EncryptedContent(decodedNewContent);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Syncable retrievedEntity = checkEntityExistenceAndGet(entityUuid);

        retrievedEntity.changeContent(newContent);

        log.debug(LogTemplates.EntityService.checkPermissionStartLog("Update entity content"));
        checkPermissionToAccess(userId, retrievedEntity);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        entityRepository.save(retrievedEntity);

        log.info("Syncable content was updated for user with ID: {} and entity UUID: {}", userId, entityUuid);
        return retrievedEntity;
    }

    @Transactional
    @Override
    public void delete(Long userId, UUID entityUuid) {
        log.info("Deleting entity for user with ID: {} and entity UUID: {}", userId, entityUuid);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Syncable retrievedEntity = checkEntityExistenceAndGet(entityUuid);

        log.debug(LogTemplates.EntityService.checkPermissionStartLog("Delete entity"));
        checkPermissionToAccess(userId, retrievedEntity);

        entityRepository.delete(retrievedEntity);
        log.info("Syncable was deleted successfully for user with ID: {} and entity with UUID: {}", userId, entityUuid);
    }

}

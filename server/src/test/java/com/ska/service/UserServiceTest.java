package com.ska.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import com.ska.constant.user.PasswordConstants;
import com.ska.dto.user.*;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.repository.UserRepository;
import com.ska.vo.user.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_RAW_PASSWORD = "qwerty123456";
    private static final String TEST_HASHED_PASSWORD = "$2a$10$validBcryptHashWith60Characters1234567890123456781234";

    private static final String USER_ID_IS_NULL_MESSAGE = "ID is <null>";
    private static final String USER_ID_IS_LESS_THEN_ONE_MESSAGE = "ID < 1";


    // === Helper-methods === //

    private void whenEncode() {
        when(passwordEncoder.encode(TEST_RAW_PASSWORD)).thenReturn(TEST_HASHED_PASSWORD);
    }

    private void whenSave() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void whenExistsByEmail(boolean isExists) {
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(isExists);
    }

    private void whenFindById(Optional<User> optional) {
        when(userRepository.findById(anyLong())).thenReturn(optional);
    }

    private void whenExistsById(boolean isExists) {
        when(userRepository.existsById(anyLong())).thenReturn(isExists);
    }


    // === userService.createUser(UserCreateRequest) --- tests === //

    @Test
    void testCreateUserSuccessfully() {
        whenEncode();
        whenSave();
        whenExistsByEmail(false);

        UserCreateRequest request = new UserCreateRequest(TEST_EMAIL, TEST_RAW_PASSWORD);
        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(TEST_EMAIL, result.getEmail().getValue());

        verify(userRepository, times(1)).existsByEmail(any(Email.class));
        verify(passwordEncoder, times(1)).encode(TEST_RAW_PASSWORD);
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    void testCreateUserWithExistingEmail() {
        whenExistsByEmail(true);
        UserCreateRequest request = new UserCreateRequest(TEST_EMAIL, TEST_RAW_PASSWORD);

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class, () -> userService.createUser(request)
        );
        assertEquals(
                String.format("User with email=%s already exists", new Email(request.email()).toString()),
                exception.getMessage()
        );

        verify(userRepository, times(1)).existsByEmail(any(Email.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1", "1a", "1a2", "1a2b", "1a2b3",
    })
    void testCreateUserWithTooShortPassword(String rawPassword) {
        whenExistsByEmail(false);

        UserCreateRequest request = new UserCreateRequest(TEST_EMAIL, rawPassword);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.createUser(request)
        );
        assertEquals(PasswordConstants.INVALID_LENGTH_MESSAGE, exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(any(Email.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }


    // === userService.getUserById(Long) --- tests === //

    @Test
    void testGetUserByIdSuccessfully() {
        User expectedUser = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

        whenFindById(Optional.of(expectedUser));

        Optional<User> result = userService.getUserById(TEST_USER_ID);

        assertTrue(result.isPresent());
        assertEquals(TEST_EMAIL, result.get().getEmail().getValue());

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetUserByIdNotFound() {
        whenFindById(Optional.empty());

        Optional<User> result = userService.getUserById(TEST_USER_ID);

        assertTrue(result.isEmpty());

        verify(userRepository,times(1)).findById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetUserByIdWithNullId() {
        Long nullId = null;

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.getUserById(nullId)
        );
        assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {
            0L, -1L, -100L, -1000L,
    })
    void testGetUserByIdWithIdLessThenOne(Long userId) {
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.getUserById(userId)
        );
        assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository);
    }


    // === userService.getAllUsers --- test === //

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());

        assertNotNull(userService.getAllUsers());

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }


    // === userService.updateUserEmail(UserUpdateEmailRequest) --- tests === //

    @Test
    void testUpdateUserEmailSuccessfully() {
        String oldEmail = "old@example.com";
        User user = new User(new Email(oldEmail), new Password(TEST_HASHED_PASSWORD));

        whenFindById(Optional.of(user));
        whenExistsByEmail(false);
        whenSave();

        UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_USER_ID, TEST_EMAIL);
        User result = userService.updateUserEmail(request);

        assertNotNull(result);
        assertNotEquals(oldEmail, result.getEmail().getValue());
        assertEquals(TEST_EMAIL, result.getEmail().getValue());

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verify(userRepository, times(1)).existsByEmail(any(Email.class));
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdateUserEmailWithNullId() {
        Long nullId = null;

        UserUpdateEmailRequest request = new UserUpdateEmailRequest(nullId, TEST_EMAIL);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.updateUserEmail(request)
        );
        assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {
            0L, -1L, -100L, -1000L,
    })
    void testUpdateUserEmailWithIdLessThenOne(Long userId) {
        UserUpdateEmailRequest request = new UserUpdateEmailRequest(userId, TEST_EMAIL);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.updateUserEmail(request)
        );
        assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @Test
    void testUpdateUserEmailNotFound() {
        whenFindById(Optional.empty());

        UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_USER_ID, TEST_EMAIL);
        
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> userService.updateUserEmail(request)
        );
        assertEquals("User id=" + TEST_USER_ID + " not found to update email", exception.getMessage());

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdateUserEmailAlreadyExists() {
        User userFirst = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));
        User userSecond = new User(new Email(TEST_EMAIL + "a"), new Password(TEST_HASHED_PASSWORD));

        whenFindById(Optional.of(userSecond));
        whenExistsByEmail(true);

        UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_USER_ID, userFirst.getEmail().getValue());

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class, () -> userService.updateUserEmail(request)
        );
        assertEquals("Email=" + userFirst.getEmail().toString() + " already exists", exception.getMessage());

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verify(userRepository, times(1)).existsByEmail(any(Email.class));
    }


    // === userService.updateUserPassword(UserUpdatePasswordRequest) --- tests === //

    @Test
    void testUpdateUserPasswordSuccessfully() {
        User user = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

        whenFindById(Optional.of(user));
        whenEncode();
        whenSave();

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_USER_ID, TEST_RAW_PASSWORD);
        User result = userService.updateUserPassword(request);

        assertNotNull(result);

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verify(passwordEncoder, times(1)).encode(request.newPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    void testUpdateUserPasswordWithNullId() {
        Long nullId = null;

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(nullId, TEST_RAW_PASSWORD);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,() -> userService.updateUserPassword(request)
        );
        assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @ParameterizedTest
    @ValueSource(longs = {
            0L, -1L, -100L, -1000L,
    })
    void testUpdateUserPasswordWithIdLessThenOne(Long userId) {
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(userId, TEST_RAW_PASSWORD);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.updateUserPassword(request)
        );
        assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void testUpdateUserPasswordNotFound() {
        whenFindById(Optional.empty());

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_USER_ID, TEST_RAW_PASSWORD);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> userService.updateUserPassword(request)
        );
        assertEquals("User id=" + TEST_USER_ID + " not found to update password", exception.getMessage());

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1", "1a", "1a2", "1a2b", "1a2b3",
    })
    void testUpdateUserPasswordTooShort(String rawPassword) {
        User user = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

        whenFindById(Optional.of(user));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_USER_ID, rawPassword);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.updateUserPassword(request)
        );
        assertEquals(PasswordConstants.INVALID_LENGTH_MESSAGE, exception.getMessage());

        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }


    // === userService.deleteUser(Long) --- tests === //

    @Test
    void testDeleteUserByIdSuccessfully() {
        whenExistsById(true);

        assertDoesNotThrow(() -> userService.deleteUserById(TEST_USER_ID));

        verify(userRepository, times(1)).existsById(TEST_USER_ID);
        verify(userRepository, times(1)).deleteById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteUserByIdNotFound() {
        whenExistsById(false);
        
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> userService.deleteUserById(TEST_USER_ID)
        );
        assertEquals("User id=" + TEST_USER_ID + " not found to delete", exception.getMessage());

        verify(userRepository, times(1)).existsById(TEST_USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteUserByIdWithNullId() {
        Long nullId = null;

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.deleteUserById(nullId)
        );
        assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {
            0L, -1L, -100L, -1000L,
    })
    void testDeleteUserByIdWithIdLessThenOne(Long userId) {
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class, () -> userService.deleteUserById(userId)
        );
        assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

        verifyNoInteractions(userRepository);
    }

}

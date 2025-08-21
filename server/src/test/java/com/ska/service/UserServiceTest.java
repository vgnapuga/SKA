package com.ska.service;

import org.junit.jupiter.api.Nested;
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

import com.ska.service.contract.crud.*;
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
    private static final String USER_ID_IS_LESS_THEN_ONE_MESSAGE = "ID is less than 1";


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


    @Nested
    class CreateUserTests implements CreateCrudBehaviorTest  {

        @Test
        @Override
        public void shouldReturnEntity_whenValidRequestData() {
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
        void shouldThrowException_whenAlreadyExistsEmail() {
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
        void shouldThrowException_whenPasswordTooShort(String rawPassword) {
            whenExistsByEmail(false);

            UserCreateRequest request = new UserCreateRequest(TEST_EMAIL, rawPassword);

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class, () -> userService.createUser(request)
            );
            assertEquals(PasswordConstants.Messages.INVALID_LENGTH_MESSAGE, exception.getMessage());

            verify(userRepository, times(1)).existsByEmail(any(Email.class));
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(passwordEncoder);
        }

    }
    

    @Nested
    class GetUserByIdTests implements GetCrudBehaviorTest {

        @Test
        @Override
        public void shouldReturnEntity_whenValidRequestData() {
            User expectedUser = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

            whenFindById(Optional.of(expectedUser));

            Optional<User> result = userService.getUserById(TEST_USER_ID);

            assertTrue(result.isPresent());
            assertEquals(TEST_EMAIL, result.get().getEmail().getValue());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void shouldReturnEmpty_whenNotFoundId() {
            whenFindById(Optional.empty());

            Optional<User> result = userService.getUserById(TEST_USER_ID);

            assertTrue(result.isEmpty());

            verify(userRepository,times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNullId() {
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
        @Override
        public void shouldThrowException_whenLessThanOneId(Long userId) {
            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class, () -> userService.getUserById(userId)
            );
            assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }

    }


    @Test
    void shouldReturnListOfEntities() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());

        assertNotNull(userService.getAllUsers());

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }


    @Nested
    class UpdateUserEmailTests implements UpdateCrudBehaviorTest {

        @Test
        @Override
        public void shouldReturnEntity_whenValidRequestData() {
            String oldEmail = "old@example.com";
            User user = new User(new Email(oldEmail), new Password(TEST_HASHED_PASSWORD));

            whenFindById(Optional.of(user));
            whenExistsByEmail(false);
            whenSave();

            UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_EMAIL);
            User result = userService.updateUserEmail(TEST_USER_ID, request);

            assertNotNull(result);
            assertNotEquals(oldEmail, result.getEmail().getValue());
            assertEquals(TEST_EMAIL, result.getEmail().getValue());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verify(userRepository, times(1)).existsByEmail(any(Email.class));
            verify(userRepository, times(1)).save(any(User.class));
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNullId() {
            Long nullId = null;

            UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_EMAIL);

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.updateUserEmail(nullId, request)
            );
            assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }

        @ParameterizedTest
        @ValueSource(longs = {
                0L, -1L, -100L, -1000L,
        })
        @Override
        public void shouldThrowException_whenLessThanOneId(Long userId) {
            UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_EMAIL);

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.updateUserEmail(userId, request)
            );
            assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNotFoundId() {
            whenFindById(Optional.empty());

            UserUpdateEmailRequest request = new UserUpdateEmailRequest(TEST_EMAIL);
            
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> userService.updateUserEmail(TEST_USER_ID, request)
            );
            assertEquals("User id=" + TEST_USER_ID + " not found", exception.getMessage());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void shouldThrowException_whenAlreadyExistsEmail() {
            User userFirst = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));
            User userSecond = new User(new Email(TEST_EMAIL + "a"), new Password(TEST_HASHED_PASSWORD));

            whenFindById(Optional.of(userSecond));
            whenExistsByEmail(true);

            UserUpdateEmailRequest request = new UserUpdateEmailRequest(userFirst.getEmail().getValue());

            ResourceAlreadyExistsException exception = assertThrows(
                    ResourceAlreadyExistsException.class,
                    () -> userService.updateUserEmail(TEST_USER_ID, request)
            );
            assertEquals("User with email=" + userFirst.getEmail().toString() + " already exists", exception.getMessage());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verify(userRepository, times(1)).existsByEmail(any(Email.class));
        }

    }

    @Nested
    class UpdateUserPasswordTests implements UpdateCrudBehaviorTest {

        @Test
        @Override
        public void shouldReturnEntity_whenValidRequestData() {
            User user = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

            whenFindById(Optional.of(user));
            whenEncode();
            whenSave();

            UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_RAW_PASSWORD);
            User result = userService.updateUserPassword(TEST_USER_ID, request);

            assertNotNull(result);

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verify(passwordEncoder, times(1)).encode(request.newPassword());
            verify(userRepository, times(1)).save(any(User.class));
            verifyNoMoreInteractions(userRepository, passwordEncoder);
        }

        @Test
        @Override
        public void shouldThrowException_whenNullId() {
            Long nullId = null;

            UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_RAW_PASSWORD);

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.updateUserPassword(nullId, request)
            );
            assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository, passwordEncoder);
        }

        @ParameterizedTest
        @ValueSource(longs = {
                0L, -1L, -100L, -1000L,
        })
        @Override
        public void shouldThrowException_whenLessThanOneId(Long userId) {
            UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_RAW_PASSWORD);

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.updateUserPassword(userId, request)
            );
            assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository, passwordEncoder);
        }

        @Test
        @Override
        public void shouldThrowException_whenNotFoundId() {
            whenFindById(Optional.empty());

            UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(TEST_RAW_PASSWORD);

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> userService.updateUserPassword(TEST_USER_ID, request)
            );
            assertEquals("User id=" + TEST_USER_ID + " not found", exception.getMessage());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(passwordEncoder);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "1", "1a", "1a2", "1a2b", "1a2b3",
        })
        void shouldThrowException_whenPasswordTooShort(String rawPassword) {
            User user = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

            whenFindById(Optional.of(user));

            UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(rawPassword);

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.updateUserPassword(TEST_USER_ID, request)
            );
            assertEquals(PasswordConstants.Messages.INVALID_LENGTH_MESSAGE, exception.getMessage());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(passwordEncoder);
        }

    }


    @Nested
    class DeleteUserByIdTests implements DeleteCrudBehavior {

        @Test
        @Override
        public void shouldDeleteEntity_whenValidRequestData() {
            whenFindById(Optional.of(new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD))));

            assertDoesNotThrow(() -> userService.deleteUserById(TEST_USER_ID));

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verify(userRepository, times(1)).deleteById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNullId() {
            Long nullId = null;

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class, () -> userService.deleteUserById(nullId)
            );
            assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNotFoundId() {
            whenFindById(Optional.empty());
            
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class, () -> userService.deleteUserById(TEST_USER_ID)
            );
            assertEquals("User id=" + TEST_USER_ID + " not found", exception.getMessage());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @ParameterizedTest
        @ValueSource(longs = {
                0L, -1L, -100L, -1000L,
        })
        @Override
        public void shouldThrowException_whenLessThanOneId(Long userId) {
            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class, () -> userService.deleteUserById(userId)
            );
            assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }
    }

}

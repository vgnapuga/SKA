package com.ska.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ska.dto.user.request.UserCreateRequest;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.model.user.vo.Email;
import com.ska.model.user.vo.Password;
import com.ska.repository.UserRepository;
import com.ska.service.contract.crud.CreateCrudBehaviorTest;
import com.ska.service.contract.crud.DeleteCrudBehavior;
import com.ska.service.contract.crud.GetCrudBehaviorTest;


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
    class CreateUserTests implements CreateCrudBehaviorTest {

        @Test
        @Override
        public void shouldReturnEntity_whenValidRequestData() {
            whenEncode();
            whenSave();
            whenExistsByEmail(false);

            UserCreateRequest request = new UserCreateRequest(TEST_EMAIL, TEST_RAW_PASSWORD);
            User result = userService.create(request);

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
                    ResourceAlreadyExistsException.class,
                    () -> userService.create(request));
            assertEquals(
                    String.format("User with email=%s already exists", new Email(request.email()).toString()),
                    exception.getMessage());

            verify(userRepository, times(1)).existsByEmail(any(Email.class));
            verifyNoMoreInteractions(userRepository, passwordEncoder);
        }

    }

    @Nested
    class GetUserByIdTests implements GetCrudBehaviorTest {

        @Test
        @Override
        public void shouldReturnEntity_whenValidRequestData() {
            User expectedUser = new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD));

            whenFindById(Optional.of(expectedUser));

            User result = userService.getById(TEST_USER_ID);

            assertEquals(TEST_EMAIL, result.getEmail().getValue());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNotFoundId() {
            whenFindById(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> userService.getById(TEST_USER_ID));
            assertEquals("User id=" + TEST_USER_ID + " not found", exception.getMessage());

            verify(userRepository, times(1)).findById(TEST_USER_ID);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @Override
        public void shouldThrowException_whenNullId() {
            Long nullId = null;

            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.getById(nullId));
            assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }

        @ParameterizedTest
        @ValueSource(longs = { 0L, -1L, -100L, -1000L, })
        @Override
        public void shouldThrowException_whenLessThanOneId(Long userId) {
            BusinessRuleViolationException exception = assertThrows(
                    BusinessRuleViolationException.class,
                    () -> userService.getById(userId));
            assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

            verifyNoInteractions(userRepository);
        }

        @Nested
        class DeleteUserByIdTests implements DeleteCrudBehavior {

            @Test
            @Override
            public void shouldDeleteEntity_whenValidRequestData() {
                whenFindById(Optional.of(new User(new Email(TEST_EMAIL), new Password(TEST_HASHED_PASSWORD))));

                assertDoesNotThrow(() -> userService.delete(TEST_USER_ID));

                verify(userRepository, times(1)).findById(TEST_USER_ID);
                verify(userRepository, times(1)).deleteById(TEST_USER_ID);
                verifyNoMoreInteractions(userRepository);
            }

            @Test
            @Override
            public void shouldThrowException_whenNullId() {
                Long nullId = null;

                BusinessRuleViolationException exception = assertThrows(
                        BusinessRuleViolationException.class,
                        () -> userService.delete(nullId));
                assertEquals(USER_ID_IS_NULL_MESSAGE, exception.getMessage());

                verifyNoInteractions(userRepository);
            }

            @Test
            @Override
            public void shouldThrowException_whenNotFoundId() {
                whenFindById(Optional.empty());

                ResourceNotFoundException exception = assertThrows(
                        ResourceNotFoundException.class,
                        () -> userService.delete(TEST_USER_ID));
                assertEquals("User id=" + TEST_USER_ID + " not found", exception.getMessage());

                verify(userRepository, times(1)).findById(TEST_USER_ID);
                verifyNoMoreInteractions(userRepository);
            }

            @ParameterizedTest
            @ValueSource(longs = { 0L, -1L, -100L, -1000L, })
            @Override
            public void shouldThrowException_whenLessThanOneId(Long userId) {
                BusinessRuleViolationException exception = assertThrows(
                        BusinessRuleViolationException.class,
                        () -> userService.delete(userId));
                assertEquals(USER_ID_IS_LESS_THEN_ONE_MESSAGE, exception.getMessage());

                verifyNoInteractions(userRepository);
            }
        }
    }

}

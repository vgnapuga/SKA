// package com.ska.controller;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.transaction.annotation.Transactional;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.ska.dto.user.UserCreateRequest;
// import com.ska.repository.UserRepository;

// @SpringBootTest
// @AutoConfigureMockMvc
// @Transactional
// @ActiveProfiles("test")
// class UserControllerIntegrationTest {

// @Autowired
// private MockMvc mockMvc;

// @Autowired
// private UserRepository userRepository;

// @Autowired
// private ObjectMapper objectMapper;

// private static final String BASE_URL = "/api/users";
// private static final String TEST_EMAIL = "test@example.com";
// private static final String TEST_PASSWORD = "qwerty123456";

// @Nested
// class CreateUserEndpoints {

// @Test
// void shouldReturn201_whenValidRequestData() throws Exception {
// UserCreateRequest request = new UserCreateRequest(TEST_EMAIL, TEST_PASSWORD);
// String requestJson = objectMapper.writeValueAsString(request);

// MvcResult result = mockMvc.perform(
// post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
// .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.email.value").value(TEST_EMAIL))
// .andReturn();

// assertEquals(1, userRepository.count());
// }

// }

// }
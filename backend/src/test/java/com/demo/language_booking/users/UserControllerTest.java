package com.demo.language_booking.users;


import com.demo.language_booking.SecurityConfig;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.users.dto.UserCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@TestPropertySource(
        properties = "auth.filter.enabled=false"
)
@Tag("Unit")
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private static UserCreateRequest.UserCreateRequestBuilder defaultCreateUserRequestBuilder() {
        return UserCreateRequest.builder()
                .username("defaultUser")
                .email("default@example.com")
                .password("defaultPassword");
    }

    @DisplayName("Get all users")
    @Test
    public void getAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }



    @DisplayName("Create a user")
    @Test
    public void createUser() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User mockUser = User.builder()
                .id(1L)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        Mockito.when(userService.create(Mockito.any(UserCreateRequest.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(request.getUsername()))
                .andExpect(jsonPath("$.email").value(request.getEmail()));

        Mockito.verify(userService).create(Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Create a user with invalid username")
    @Test
    public void createUserWithInvalidUsername() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .username("")
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).create(Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Create a user with invalid email")
    @Test
    public void createUserWithInvalidEmail() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .email("invalid-email")
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).create(Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Create a user with invalid password")
    @Test
    public void createUserWithInvalidPassword() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .password("")
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).create(Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Get a user by ID")
    @Test
    public void getUserById() throws Exception {
        User mockUser = User.builder()
                .id(1L)
                .username("defaultUser")
                .email("default@example.com")
                .password("defaultPassword")
                .build();

        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(mockUser.getUsername()))
                .andExpect(jsonPath("$.email").value(mockUser.getEmail()));

        Mockito.verify(userService).getById(1L);
    }

    @DisplayName("Get a user by ID not found")
    @Test
    public void getUserByIdNotFound() throws Exception {
        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(userService).getById(1L);
    }

    @DisplayName("Get all users returns users list")
    @Test
    public void getAllUsersReturnsUsersList() throws Exception {
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .password("password1")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .email("user2@example.com")
                .password("password2")
                .build();

        List<User> users = List.of(user1, user2);

        Mockito.when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        Mockito.verify(userService).getAll();
    }

    @DisplayName("Get all users returns empty list")
    @Test
    public void getAllUsersReturnsEmptyList() throws Exception {
        List<User> users = List.of();

        Mockito.when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(userService).getAll();
    }

    @DisplayName("Update a user")
    @Test
    public void updateUser() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .username("updatedUser")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        User mockUser = User.builder()
                .id(1L)
                .username("updatedUser")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(UserCreateRequest.class))).thenReturn(mockUser);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("updatedUser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        Mockito.verify(userService).update(eq(1L), Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Update a user not found")
    @Test
    public void updateUserNotFound() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .username("updatedUser")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(UserCreateRequest.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 1"));

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(userService).update(eq(1L), Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Update a user with invalid request data")
    @Test
    public void updateUserWithInvalidRequestData() throws Exception {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .username("")
                .email("invalid-email")
                .password("")
                .build();

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).update(Mockito.anyLong(), Mockito.any(UserCreateRequest.class));
    }

    @DisplayName("Delete a user")
    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).delete(1L);
    }

    @DisplayName("Delete a user not found")
    @Test
    public void deleteUserNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found with id: 1"))
                .when(userService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(userService).delete(1L);
    }
}

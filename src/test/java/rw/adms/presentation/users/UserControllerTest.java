package rw.adms.presentation.users;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rw.adms.application.users.usecases.ChangeUserEmailUseCase;
import rw.adms.application.users.usecases.ChangeUserNameUseCase;
import rw.adms.application.users.usecases.ChangeUserPasswordUseCase;
import rw.adms.application.users.usecases.CreateUserUseCase;
import rw.adms.application.users.usecases.DeleteUserUseCase;
import rw.adms.application.users.usecases.GetUserUseCase;
import rw.adms.application.users.usecases.GetUsersUseCase;
import rw.adms.domain.users.User;
import rw.adms.presentation.users.dto.CreateUserRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetUserUseCase getUserUseCase;

    @MockitoBean
    private GetUsersUseCase getUsersUseCase;

    @MockitoBean
    private ChangeUserNameUseCase changeUserNameUseCase;

    @MockitoBean
    private ChangeUserEmailUseCase changeUserEmailUseCase;

    @MockitoBean
    private ChangeUserPasswordUseCase changeUserPasswordUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    void shouldCreateUser() throws Exception {

        CreateUserRequest request = new CreateUserRequest(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(createUserUseCase).execute(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );
    }

    @Test
    void shouldRejectCreateUserWithBlankFields() throws Exception {

        CreateUserRequest request = new CreateUserRequest("", "", "", "");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUserById() throws Exception {

        User user = User.reconstitute(
                1L, "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        when(getUserUseCase.execute(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Bonheur"))
                .andExpect(jsonPath("$.email").value("bonheur@example.com"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {

        when(getUserUseCase.execute(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {

        User user = User.reconstitute(
                1L, "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        when(getUsersUseCase.execute()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase).execute(1L);
    }

    @Test
    void shouldReturn404WhenDeletingMissingUser() throws Exception {

        org.mockito.Mockito.doThrow(new IllegalArgumentException("User not found"))
                .when(deleteUserUseCase).execute(99L);

        mockMvc.perform(delete("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldChangeUserPassword() throws Exception {

        String body = objectMapper.writeValueAsString(
                new rw.adms.presentation.users.dto.ChangeUserPasswordRequest("newPassword")
        );

        mockMvc.perform(patch("/api/users/{id}/password", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        verify(changeUserPasswordUseCase).execute(1L, "newPassword");
    }
}

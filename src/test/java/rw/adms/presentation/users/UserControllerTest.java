package rw.adms.presentation.users;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
import rw.adms.application.users.usecases.ChangeUserEmailUseCase;
import rw.adms.application.users.usecases.ChangeUserNameUseCase;
import rw.adms.application.users.usecases.ChangeUserPasswordUseCase;
import rw.adms.application.users.usecases.ChangeUserRoleUseCase;
import rw.adms.application.users.usecases.CreateUserUseCase;
import rw.adms.application.users.usecases.DeleteUserUseCase;
import rw.adms.application.users.usecases.GetUserUseCase;
import rw.adms.application.users.usecases.GetUsersUseCase;
import rw.adms.application.users.usecases.GrantPermissionUseCase;
import rw.adms.application.users.usecases.RevokePermissionUseCase;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.Permission;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.presentation.users.dto.ChangeUserRoleRequest;
import rw.adms.presentation.users.dto.CreateUserRequest;
import rw.adms.presentation.users.dto.GrantPermissionRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private static final String SUPERADMIN_TOKEN = "superadmin-token";

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
    private ChangeUserRoleUseCase changeUserRoleUseCase;

    @MockitoBean
    private GrantPermissionUseCase grantPermissionUseCase;

    @MockitoBean
    private RevokePermissionUseCase revokePermissionUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    /**
     * Every @WebMvcTest slice loads the app's WebMvcConfigurer/argument
     * resolvers - including CurrentUserArgumentResolver, which depends on
     * this use case - even for controllers that don't declare it directly.
     */
    @MockitoBean
    private ValidateSessionUseCase validateSessionUseCase;

    private User superAdmin() {
        return User.reconstitute(
                99L, "Super", "Admin", "super@example.com", "hash", UserRole.SUPERADMIN, Set.of()
        );
    }

    private MockHttpServletRequestBuilder asSuperAdmin(MockHttpServletRequestBuilder builder) {
        when(validateSessionUseCase.execute(SUPERADMIN_TOKEN)).thenReturn(Optional.of(superAdmin()));
        return builder.header("Authorization", "Bearer " + SUPERADMIN_TOKEN);
    }

    @Test
    void shouldCreateUserWithoutActor() throws Exception {

        CreateUserRequest request = new CreateUserRequest(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        User createdUser = User.reconstitute(
                1L, "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        when(createUserUseCase.execute(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123", null
        )).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("bonheur@example.com"));

        verify(createUserUseCase).execute(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123", null
        );
    }

    @Test
    void shouldCreateUserAsSuperAdminActor() throws Exception {

        CreateUserRequest request = new CreateUserRequest(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        User createdUser = User.reconstitute(
                1L, "Bonheur", "Iradukunda", "bonheur@example.com", "password123"
        );

        when(createUserUseCase.execute(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123", UserRole.SUPERADMIN
        )).thenReturn(createdUser);

        mockMvc.perform(asSuperAdmin(post("/api/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(createUserUseCase).execute(
                "Bonheur", "Iradukunda", "bonheur@example.com", "password123", UserRole.SUPERADMIN
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
    void shouldDeleteUserAsSuperAdmin() throws Exception {

        mockMvc.perform(asSuperAdmin(delete("/api/users/{id}", 1L)))
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase).execute(1L, UserRole.SUPERADMIN);
    }

    @Test
    void shouldRejectDeleteWithoutAuthentication() throws Exception {

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn404WhenDeletingMissingUser() throws Exception {

        org.mockito.Mockito.doThrow(new IllegalArgumentException("User not found"))
                .when(deleteUserUseCase).execute(99L, UserRole.SUPERADMIN);

        mockMvc.perform(asSuperAdmin(delete("/api/users/{id}", 99L)))
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

    @Test
    void shouldChangeUserRoleAsSuperAdmin() throws Exception {

        String body = objectMapper.writeValueAsString(new ChangeUserRoleRequest(UserRole.SUPERADMIN));

        mockMvc.perform(asSuperAdmin(patch("/api/users/{id}/role", 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        verify(changeUserRoleUseCase).execute(1L, UserRole.SUPERADMIN, UserRole.SUPERADMIN);
    }

    @Test
    void shouldGrantPermissionAsSuperAdmin() throws Exception {

        String body = objectMapper.writeValueAsString(new GrantPermissionRequest(Permission.MANAGE_ITEMS));

        mockMvc.perform(asSuperAdmin(post("/api/users/{id}/permissions", 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        verify(grantPermissionUseCase).execute(1L, Permission.MANAGE_ITEMS, UserRole.SUPERADMIN);
    }

    @Test
    void shouldRevokePermissionAsSuperAdmin() throws Exception {

        mockMvc.perform(asSuperAdmin(delete("/api/users/{id}/permissions/{permission}", 1L, "MANAGE_ITEMS")))
                .andExpect(status().isNoContent());

        verify(revokePermissionUseCase).execute(1L, Permission.MANAGE_ITEMS, UserRole.SUPERADMIN);
    }
}

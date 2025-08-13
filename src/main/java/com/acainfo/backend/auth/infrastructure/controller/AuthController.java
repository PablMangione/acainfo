package com.acainfo.backend.auth.infrastructure.controller;

import com.acainfo.backend.auth.application.*;
import com.acainfo.backend.auth.infrastructure.controller.dto.*;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de autenticación y autorización.
 * Maneja login, registro, refresh de tokens y logout.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para autenticación y autorización")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterStudentUseCase registerStudentUseCase;
    private final RegisterTeacherUseCase registerTeacherUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;

    /**
     * Endpoint de login para estudiantes y profesores
     */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario (estudiante o profesor) con email y contraseña"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario inactivo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest) {

        log.info("Petición de login recibida para: {}", loginRequest.getEmail());

        try {
            LoginResponseDto response = loginUseCase.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint de registro para nuevos estudiantes
     */
    @PostMapping("/register/student")
    @Operation(
            summary = "Registrar estudiante",
            description = "Registra un nuevo estudiante y automáticamente lo autentica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Estudiante registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o contraseñas no coinciden",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ya registrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<LoginResponseDto> registerStudent(
            @Valid @RequestBody StudentRegisterRequestDto registerRequest) {

        log.info("Petición de registro de estudiante: {}", registerRequest.getEmail());

        try {
            LoginResponseDto response = registerStudentUseCase.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error en registro de estudiante: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint de registro para nuevos profesores (solo admin)
     */
    @PostMapping("/register/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Registrar profesor",
            description = "Registra un nuevo profesor. Requiere permisos de administrador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Profesor registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = TeacherOutputDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos de administrador",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ya registrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TeacherOutputDto> registerTeacher(
            @Valid @RequestBody TeacherInputDto registerRequest) {

        log.info("Petición de registro de profesor: {}", registerRequest.getEmail());

        try {
            TeacherOutputDto response = registerTeacherUseCase.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error en registro de profesor: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint para refrescar el access token
     */
    @PostMapping("/refresh")
    @Operation(
            summary = "Refrescar token",
            description = "Obtiene un nuevo access token usando el refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refrescado exitosamente",
                    content = @Content(schema = @Schema(implementation = TokenRefreshResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh token inválido o expirado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(
            @Valid @RequestBody TokenRefreshDto refreshRequest) {

        log.debug("Petición de refresh token recibida");

        try {
            TokenRefreshResponseDto response = refreshTokenUseCase.refresh(refreshRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en refresh token: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint de logout
     */
    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida el token actual del usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout exitoso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request) {
        // Extraer el token del header
        String token = extractTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            logoutUseCase.logout(token);
            log.info("Logout exitoso");

            return ResponseEntity.ok(new MessageResponse("Logout exitoso"));
        }

        return ResponseEntity.ok(new MessageResponse("No hay sesión activa"));
    }

    /**
     * Endpoint para validar un token
     */
    @GetMapping("/validate")
    @Operation(
            summary = "Validar token",
            description = "Verifica si un token JWT es válido y retorna información sobre el mismo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado de la validación",
                    content = @Content(schema = @Schema(implementation = TokenValidationResponseDto.class))
            )
    })
    public ResponseEntity<TokenValidationResponseDto> validateToken(
            @Parameter(description = "Token JWT a validar", required = true)
            @RequestHeader("Authorization") String authHeader) {

        String token = extractTokenFromHeader(authHeader);

        if (!StringUtils.hasText(token)) {
            return ResponseEntity.ok(
                    TokenValidationResponseDto.builder()
                            .valid(false)
                            .error("Token no proporcionado")
                            .build()
            );
        }

        TokenValidationResponseDto response = validateTokenUseCase.validate(token);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de verificación de salud del servicio de autenticación
     */
    @GetMapping("/health")
    @Operation(
            summary = "Health check",
            description = "Verifica que el servicio de autenticación esté funcionando"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Servicio funcionando correctamente"
    )
    public ResponseEntity<MessageResponse> health() {
        return ResponseEntity.ok(new MessageResponse("Servicio de autenticación funcionando correctamente"));
    }

    /**
     * Endpoint para obtener el usuario actual
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Obtener usuario actual",
            description = "Retorna la información del usuario autenticado actual"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Información del usuario",
                    content = @Content(schema = @Schema(implementation = CurrentUserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {

        String token = extractTokenFromHeader(authHeader);

        if (!StringUtils.hasText(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TokenValidationResponseDto validation = validateTokenUseCase.validate(token);

        if (!validation.isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CurrentUserResponse response = CurrentUserResponse.builder()
                .userId(validation.getUserId())
                .email(validation.getEmail())
                .roles(validation.getRoles())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Extrae el token del header Authorization
     */
    private String extractTokenFromHeader(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Extrae el token del request
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return extractTokenFromHeader(bearerToken);
    }

    /**
     * DTOs de respuesta simples
     */
    @Data
    @AllArgsConstructor
    @Schema(description = "Respuesta con mensaje simple")
    public static class MessageResponse {
        @Schema(description = "Mensaje de respuesta", example = "Operación exitosa")
        private String message;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "Respuesta de error")
    public static class ErrorResponse {
        @Schema(description = "Código de error HTTP", example = "401")
        private int status;

        @Schema(description = "Mensaje de error", example = "Credenciales inválidas")
        private String error;

        @Schema(description = "Timestamp del error", example = "2024-01-15T10:30:00")
        private String timestamp;

        @Schema(description = "Path del request", example = "/api/auth/login")
        private String path;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información del usuario actual")
    public static class CurrentUserResponse {
        @Schema(description = "ID del usuario", example = "STUDENT_123")
        private String userId;

        @Schema(description = "Email del usuario", example = "usuario@ejemplo.com")
        private String email;

        @Schema(description = "Roles del usuario", example = "[\"ROLE_STUDENT\"]")
        private List<String> roles;
    }
}
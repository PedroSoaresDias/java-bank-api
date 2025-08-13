package br.com.bank.java_bank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.bank.java_bank.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank.domain.DTO.UserResponse;
import br.com.bank.java_bank.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@Tag(name = "Módulo de Usuários", description = "Cuida do gerenciamento de usuários da aplicação")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Buscar os dados de todos os usuários")
    @ApiResponse(responseCode = "200", description = "Todos os usuários")
    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Buscar os dados de um usuário pelo Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public Mono<UserResponse> getUserById(@Valid @PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Criar um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao criar um usuário")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Atualizar um usuário definido pelo Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao atualizar um usuário"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")    
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateUser(@PathVariable("id") Long id, @Valid @RequestBody CreateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @Operation(summary = "Excluir um usuário definido pelo Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")    
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@Valid @PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }
}

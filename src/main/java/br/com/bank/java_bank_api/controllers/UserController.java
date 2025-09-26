package br.com.bank.java_bank_api.controllers;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bank.java_bank_api.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank_api.domain.DTO.UserResponse;
import br.com.bank.java_bank_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Buscar os dados de um usuário pelo Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Criar um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao criar um usuário")
    })
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualizar um usuário definido pelo Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao atualizar um usuário"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")    
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @Valid @RequestBody CreateUserRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Excluir um usuário definido pelo Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")    
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

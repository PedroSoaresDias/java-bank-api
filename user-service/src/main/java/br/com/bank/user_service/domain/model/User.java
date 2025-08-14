package br.com.bank.user_service.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tb_user")
@Getter
@Setter
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
}

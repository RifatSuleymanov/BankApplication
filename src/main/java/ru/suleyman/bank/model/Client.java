package ru.suleyman.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Validated
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "birthDate")
    private LocalDate birthDate;
    @Column(name = "phone", unique = true)
    private String phone;
    @Email(message = "Email должые быть валидным: *****@***.ru")
    @Column(name = "email", unique = true)
    private String email;
    @PositiveOrZero
    @Column(name = "balance")
    private BigDecimal balance;
    @PositiveOrZero
    @Column(name = "initialBalance")
    private BigDecimal initialBalance;


}

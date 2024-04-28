package ru.praktikum.diplom.dto;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String email;
    private String password;
    private String name;
}

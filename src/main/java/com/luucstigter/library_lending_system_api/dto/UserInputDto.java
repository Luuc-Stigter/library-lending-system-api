package com.luucstigter.library_lending_system_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserInputDto {

    @NotBlank(message = "Gebruikersnaam is verplicht")
    public String username;

    @NotBlank(message = "Wachtwoord is verplicht")
    @Size(min = 8, message = "Wachtwoord moet minimaal 8 karakters lang zijn")
    public String password;

    @NotBlank(message = "E-mail is verplicht")
    @Email(message = "Ongeldig e-mailformaat")
    public String email;

    @NotBlank(message = "Rol is verplicht")
    public String role;
}
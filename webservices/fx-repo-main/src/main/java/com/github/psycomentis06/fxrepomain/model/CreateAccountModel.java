package com.github.psycomentis06.fxrepomain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateAccountModel {
    private String username;
    private String password;
    private String email;
}

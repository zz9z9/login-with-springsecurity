package com.springstudy.project.myweddingplanner.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {
    private String email;
    private String name;
    private String password;

    public MemberDTO() {
    }

    public MemberDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberDTO(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}

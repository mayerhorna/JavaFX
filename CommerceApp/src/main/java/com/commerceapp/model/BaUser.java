package com.commerceapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ba_user")
public class BaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ba_user_id;
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String login_user;
    @Column
    private String password_user;

    public int getBa_user_id() {
        return ba_user_id;
    }

    public void setBa_user_id(int ba_user_id) {
        this.ba_user_id = ba_user_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin_user() {
        return login_user;
    }

    public void setLogin_user(String login_user) {
        this.login_user = login_user;
    }

    public String getPassword_user() {
        return password_user;
    }

    public void setPassword_user(String password_user) {
        this.password_user = password_user;
    }

    @Override
    public String toString() {
        return "ba_user [ba_user_id=" + ba_user_id + ", code=" + code + ", name=" + name + ", login_user=" + login_user
                + ", password_user=" + password_user + "]";
    }

}

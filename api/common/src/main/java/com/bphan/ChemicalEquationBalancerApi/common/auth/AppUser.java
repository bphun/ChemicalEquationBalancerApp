package com.bphan.ChemicalEquationBalancerApi.common.auth;

import java.util.UUID;

public class AppUser {
  private String id;
  private String username, password, role;

  public AppUser() {
    this.id = "";
    this.username = "";
    this.password = "";
    this.role = "";
  }

  public AppUser(String id, String username, String password, String role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public AppUser(String username, String password) {
    this.id = UUID.randomUUID().toString();
    this.username = username;
    this.password = password;
    this.role = "USER";
  }

  public AppUser(UserCredentials credentials) {
    this.id = UUID.randomUUID().toString();
    this.username = credentials.getUsername();
    this.password = credentials.getPassword();
    this.role = "USER";
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setRandomUid() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public String getRole() {
    return this.role;
  }
}

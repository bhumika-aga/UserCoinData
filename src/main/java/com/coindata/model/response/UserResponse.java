package com.coindata.model.response;

public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String username;
    private String userPan;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPan() {
        return userPan;
    }

    public void setUserPan(String userPan) {
        this.userPan = userPan;
    }

    public UserResponse(String firstName, String lastName, String email, String mobile, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.username = username;
    }

    public UserResponse(String firstName, String lastName, String email, String mobile, String username,
            String userPan) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.username = username;
        this.userPan = userPan;
    }
}
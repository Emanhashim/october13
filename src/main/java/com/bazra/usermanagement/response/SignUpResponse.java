package com.bazra.usermanagement.response;

import com.bazra.usermanagement.model.Levels;
import com.bazra.usermanagement.model.Role;

/**
 * SignUp Response
 * 
 * @author MOSS
 * @version 4/2022
 */
public class SignUpResponse {
    private String message;
    private String firstName;
    private String lastName;
    private Levels level;
    private String username;
    private Role roles;
 
    
 

    public SignUpResponse(String message) {
        this.message = message;
    }

    public SignUpResponse(String username, Role string, String message,String firstname,String lastname,Levels levels) {
        this.username = username;
        this.roles = string;
        this.message = message;
        this.firstName =firstname;
        this.lastName = lastname;
        this.level=levels;

    }
    
    
    
    public SignUpResponse(String username, Role role, String string, String firstName, String lastName) {
		this.username=username;
		this.roles=role;
		this.message=string;
		this.firstName=firstName;
		this.lastName=lastName;
		
	}
    
    
    
	public Levels getLevel() {
		return level;
	}

	public void setLevel(Levels level) {
		this.level = level;
	}

	public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

  

   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }
}

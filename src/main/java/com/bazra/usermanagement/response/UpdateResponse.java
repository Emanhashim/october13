package com.bazra.usermanagement.response;

public class UpdateResponse {
    private String message;

    public UpdateResponse(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

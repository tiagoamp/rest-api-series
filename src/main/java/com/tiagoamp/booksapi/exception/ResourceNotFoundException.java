package com.tiagoamp.booksapi.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor  @AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String resourceId;

    public String getMessage() {
        if (resourceName == null || resourceId == null)
            return null;
        return String.format("Resource '%s' not found with id '%s'", resourceName, resourceId);
    }

}

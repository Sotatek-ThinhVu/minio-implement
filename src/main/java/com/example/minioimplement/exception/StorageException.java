package com.example.minioimplement.exception;

public class StorageException extends RuntimeException{
    public StorageException(Exception ex){
        super(ex);
    }
}

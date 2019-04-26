package com.conch.validation.http.basis;


public class BaseRepo<T> {

    protected T remoteDataSource;

    public BaseRepo(T remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

}
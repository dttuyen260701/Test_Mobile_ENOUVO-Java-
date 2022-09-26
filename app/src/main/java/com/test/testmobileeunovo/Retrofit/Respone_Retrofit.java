package com.test.testmobileeunovo.Retrofit;

import com.test.testmobileeunovo.Models.Feature_Approval;

import java.util.ArrayList;

public class Respone_Retrofit <T> {
    private T data;

    public Respone_Retrofit(T list_data) {
        this.data = list_data;
    }

    public T getList_data() {
        return data;
    }

    public void setList_data(T list_data) {
        this.data = list_data;
    }
}

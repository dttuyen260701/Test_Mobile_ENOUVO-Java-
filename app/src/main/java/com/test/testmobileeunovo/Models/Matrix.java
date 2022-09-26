package com.test.testmobileeunovo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Matrix {
    private int id;
    private String alias;
    private int min_Range;
    private int max_Range;
    private int feature_id;
    @SerializedName("approvals")
    private ArrayList<Feature_Approval> list_approval;

    public Matrix(int id, String alias, int min_Range, int max_Range, int feature_id, ArrayList<Feature_Approval> list_approval) {
        this.id = id;
        this.alias = alias;
        this.min_Range = min_Range;
        this.max_Range = max_Range;
        this.feature_id = feature_id;
        this.list_approval = list_approval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getMin_Range() {
        return min_Range;
    }

    public void setMin_Range(int min_Range) {
        this.min_Range = min_Range;
    }

    public int getMax_Range() {
        return max_Range;
    }

    public void setMax_Range(int max_Range) {
        this.max_Range = max_Range;
    }

    public int getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(int feature_id) {
        this.feature_id = feature_id;
    }

    public ArrayList<Feature_Approval> getList_approval() {
        return list_approval;
    }

    public void setList_approval(ArrayList<Feature_Approval> list_approval) {
        this.list_approval = list_approval;
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", min_Range=" + min_Range +
                ", max_Range=" + max_Range +
                ", feature_id=" + feature_id +
                ", list_approval=" + list_approval +
                '}';
    }
}

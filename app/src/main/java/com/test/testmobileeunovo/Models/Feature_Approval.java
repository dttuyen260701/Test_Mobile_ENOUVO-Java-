package com.test.testmobileeunovo.Models;

public class Feature_Approval {
    private int id;
    private String name;
    private boolean check;

    public Feature_Approval(int id, String name) {
        this.id = id;
        this.name = name;
        this.check = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "Feature_Approval{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", check=" + check +
                '}';
    }
}

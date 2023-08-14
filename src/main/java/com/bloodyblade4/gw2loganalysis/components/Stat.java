package com.bloodyblade4.gw2loganalysis.components;


public class Stat {
    Boolean isActive = false;
    String objName;
    String desc = "";

    public Stat() {
    }

    public Stat(String ObjName) {
        this.objName = ObjName;
    }

    public Stat(Boolean IsActive, String ObjName) {
        this.isActive = IsActive;
        this.objName = ObjName;
    }

    public Stat(String ObjName, String Desc) {
        this.objName = ObjName;
        this.desc = Desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String Desc) {
        this.desc = Desc;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getObjName() {
        return this.objName;
    }

    public void setObjName(String name) {
        this.objName = name;
    }
}

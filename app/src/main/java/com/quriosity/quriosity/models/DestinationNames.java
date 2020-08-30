package com.quriosity.quriosity.models;

public class DestinationNames {
    private String destname;
    private String destId;

    @Override
    public String toString() {
        return destname;
    }

    public String getDestId() {
        return destId;
    }

    public String getDestname() {
        return destname;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public void setDestname(String destname) {
        this.destname = destname;
    }
}

package at.technikum.apps.mtcg.entity;

import java.util.ArrayList;
import java.util.List;

public class Package {
    private String package_id;
    private Boolean bought;

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }


    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }
}

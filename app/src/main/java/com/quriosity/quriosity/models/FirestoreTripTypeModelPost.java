package com.quriosity.quriosity.models;

import java.util.List;
import java.util.Map;

public class FirestoreTripTypeModelPost {
    private String id;
    private String uniqueid;
    private String name;
    private String description;
    private Map parent;
    private List<String> tags;
    private String createat;
    private String lastmodified;
    private String icon;
    private String cover;

    public FirestoreTripTypeModelPost(String id, String uniqueid, String name, String description, Map parent,
                                      List<String> tags, String createat, String lastmodified, String icon, String cover) {
        this.id = id;
        this.uniqueid = uniqueid;
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.tags = tags;
        this.createat = createat;
        this.lastmodified = lastmodified;
        this.icon = icon;
        this.cover = cover;
    }

    public Map getParent() {
        return parent;
    }

    public void setParent(Map parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat;
    }

    public String getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
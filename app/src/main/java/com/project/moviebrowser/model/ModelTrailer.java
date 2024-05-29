package com.project.moviebrowser.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

import io.realm.RealmModel;
import io.realm.RealmObject;

public class ModelTrailer extends RealmObject implements Serializable {
    private String Id;
    private String Name;
    private String Key;
    private String Url;
    private String Type;
    private Boolean Official;
    private int Size;
    private String PublishedAt;

    public static ModelTrailer createModel(JSONObject jsonObject) throws JSONException {
        ModelTrailer model = new ModelTrailer();
        return updateModel(model, jsonObject);
    }

    public  static ModelTrailer updateModel(ModelTrailer model, JSONObject json)  throws JSONException {
        model.setId(json.getString("id"));
        model.setName(json.getString("name"));
        model.setKey(json.getString("key"));
        model.setUrl(json.getString("url"));
        model.setType(json.getString("type"));
        model.setOfficial(json.getBoolean("official"));
        model.setSize(json.getInt("size"));
        model.setPublishedAt(json.getString("publishedAt"));

        return model;
    }

    public JSONObject toJSONObject() throws JSONException{
        JSONObject object = new JSONObject();
        object.put("id", Id);
        object.put("name", Name);
        object.put("key", Key);
        object.put("url", Url);
        object.put("type", Type);
        object.put("official", Official);
        object.put("size", Size);
        object.put("publishedAt", PublishedAt);

        return object;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Boolean getOfficial() {
        return Official;
    }

    public void setOfficial(Boolean official) {
        Official = official;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        PublishedAt = publishedAt;
    }


}

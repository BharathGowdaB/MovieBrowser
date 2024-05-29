package com.project.moviebrowser.model;

import java.io.Serializable;

import io.realm.RealmObject;

public class Genre extends RealmObject implements Serializable {
    private int Id;
    private String Name;
    public Genre(){}

    public static Genre createModel(int id, String name){
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
    public int getId(){
        return this.Id;
    }

    public String getName(){
        return this.Name;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }
}

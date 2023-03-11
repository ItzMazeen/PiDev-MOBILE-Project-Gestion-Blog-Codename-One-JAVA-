/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

/**
 *
 * @author bhk
 */
public class Article {

    private int id, views;
    private String sujet, contenu, image, createdAt;
    private String userId;
    private String comments;

    public Article(int views, String sujet, String contenu, String image, String createdAt, String userId, String comments) {
        this.views = views;
        this.sujet = sujet;
        this.image = image;
        this.createdAt = createdAt;
        this.contenu = contenu;
        this.userId = userId;
        this.comments = comments;
    }

    public Article(int id, int views, String sujet, String contenu, String image, String createdAt, String userId, String comments) {
        this.id = id;
        this.views = views;
        this.sujet = sujet;
        this.image = image;
        this.createdAt = createdAt;
        this.contenu = contenu;
        this.userId = userId;
        this.comments = comments;
    }

    public Article(String sujet, String contenu, String image, String userId) {
        this.sujet = sujet;
        this.contenu = contenu;
        this.image = image;
        this.userId = userId;
    }

    public Article() {

    }

    public Article(String sujet, String contenu, String image) {
        this.sujet = sujet;
        this.contenu = contenu;
        this.image = image;

    }

    public Article(String contenu, String userId) {

        this.contenu = contenu;

        this.userId = userId;
    }

    public Article(String contenu) {
        this.contenu = contenu;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;

    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public String toString() {
        return "Article{" + "id=" + id + ", sujet=" + sujet + "image=" + image + ", views=" + views + "\n";
    }

}

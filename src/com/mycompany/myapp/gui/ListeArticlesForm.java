/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.ImageViewer;
import com.codename1.io.Storage;
import com.codename1.ui.Button;

import com.codename1.ui.Dialog;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Article;
import com.mycompany.myapp.services.ServiceArticles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author bhk
 */
public class ListeArticlesForm extends Form {

    EncodedImage ec;
    int scaledWidth = 100;
    int scaledHeight = 100;

    public ListeArticlesForm() {
        Storage.getInstance().clearCache();

        setTitle("List Articles");
        setLayout(BoxLayout.y());

// Adding article button :
        Button btnAddArticle = new Button("Add Article");
        btnAddArticle.addActionListener(e -> new AddArticleForm(this).show());
        add(btnAddArticle);

// Articles stats button :
        Button btnStatArticles = new Button("Articles stats");
        btnStatArticles.addActionListener(e -> new ArticlesStatsForm(this).show());
        add(btnStatArticles);

// Comment stats button :
        Button btnStat = new Button("Comments stats");
        btnStat.addActionListener(e -> new CommentsStatsForm(this).show());
        add(btnStat);

        Label top3 = new Label("Top 3 Articles : ------------------------------------------------------------------------------------");
        add(top3);

        ArrayList<Article> articles = ServiceArticles.getInstance().getAllArticles();
        ArrayList<Article> bestarticles = ServiceArticles.getInstance().getAllArticles();

// Get the top 3 articles :
        Collections.sort(bestarticles, (a1, a2) -> Integer.compare(a2.getViews(), a1.getViews()));
        ArrayList<Article> top3Articles = new ArrayList<Article>();
        for (int i = 0; i < Math.min(3, articles.size()); i++) {
            top3Articles.add(bestarticles.get(i));
        }

        for (Article t : top3Articles) {
            addElement(t);
        }

        Label liste = new Label("Liste Articles : --------------------------------------------------------------------------------------------");
        add(liste);

// Liste articles :
        for (Article t : articles) {
            addElement(t);
            Label fasel = new Label("--------------------------------------------------------------------------------------------------------------");
            add(fasel);
        }

    }

    public void addElement(Article article) {

        Label sujet = new Label("Sujet : " + article.getSujet());
        String JSON = article.getUserId();

        int firstNameStart = JSON.indexOf("firstName=") + 10; // 10 is the length of "firstName="
        int firstNameEnd = JSON.indexOf(",", firstNameStart);
        String firstName = JSON.substring(firstNameStart, firstNameEnd);

        int lastNameStart = JSON.indexOf("lastName=") + 9; // 9 is the length of "lastName="
        String lastName = JSON.substring(lastNameStart, JSON.length() - 1);

        Label userIdLabel = new Label("Publié Par : " + firstName + " " + lastName);
        String input = article.getCreatedAt();
        String date2 = input.substring(0, 10); // extract date
        String time = input.substring(11, 19); // extract time

        Label date = new Label("Date : le " + date2 + " à " + time);
        Label views = new Label("Vues : " + Integer.toString(article.getViews()));
        Label id = new Label("ID : " + Integer.toString(article.getId()));

// the image viewer : 
        ImageViewer iv = new ImageViewer();
        Image img;
        try {
            String URL = "http://127.0.0.1:8000/uploads/images/" + article.getImage();
            ec = EncodedImage.create("/load.png");
            img = URLImage.createToStorage(ec, URL, URL, URLImage.RESIZE_FAIL).scaled(scaledWidth, scaledHeight);
            iv.setImage(img);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

// Détails artical button :
        Button myButton = new Button("see more details");
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int articleId = article.getId();
                // Redirect to another form
                myButton.addActionListener(e -> new ArticleDetailForm(articleId).show());
            }
        });

// Delete artical button :
        Button deleteButton = new Button("Delete");
        deleteButton.addActionListener(evt -> {
            int articleId = article.getId();
            // show a confirmation dialog :
            boolean confirmed = Dialog.show("Confirm", "Are you sure you want to delete?", "Yes", "No");
            if (confirmed) {
                boolean success = ServiceArticles.getInstance().deleteArticle(articleId);
                if (success) {
                    Dialog.show("Success", "Article deleted", "OK", null);
                    new ListeArticlesForm().show();
                } else {
                    Dialog.show("Error", "Failed to delete article", "OK", null);
                }
            }
        });

// adding the buttons to the form :
        add(userIdLabel);
        add(date);
        add(id);
        add(sujet);
        add(views);
        add(iv);
        add(myButton);
        add(deleteButton);

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.*;
import com.codename1.ui.URLImage;
import com.mycompany.myapp.services.ServiceArticles;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Article;
import com.mycompany.myapp.services.ServiceArticles.ArticleCallback;
import java.io.IOException;

/**
 *
 * @author mazee
 */
public class ArticleDetailForm extends Form {

    EncodedImage ec;
    int scaledWidth = 100;
    int scaledHeight = 100;

    public ArticleDetailForm(int articleId) {

        setLayout(BoxLayout.y());
        Toolbar toolbar = new Toolbar();
        setToolbar(toolbar);

        Button backButton = new Button("Back");
        backButton.addActionListener(e -> {
            new ListeArticlesForm().show();
        });
        add(backButton);

        Button update = new Button("Update Article");

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Redirect to another form
                new UpdateArticleForm(articleId).show();
            }
        });
        add(update);

        String URL = "https://www.facebook.com/share.php?u=http://127.0.0.1:8000/articles/" + articleId;
        System.out.println(URL);
        Button shareButton = new Button("Share on Facebook");
        shareButton.addActionListener(e -> {
            BrowserComponent browser = new BrowserComponent();
            browser.setURL(URL);
            Form browserForm = new Form();
            browserForm.setLayout(new BorderLayout());
            browserForm.addComponent(BorderLayout.CENTER, browser);
            browserForm.show();
        });
        add(shareButton);

        String URLWhatsapp = "https://api.whatsapp.com/send?text=Hey, I found this article : http://127.0.0.1:8000/articles/" + articleId;
        System.out.println(URLWhatsapp);
        Button shareButtonWA = new Button("Share on Whatsapp");
        shareButtonWA.addActionListener(e -> {
            BrowserComponent browser = new BrowserComponent();
            browser.setURL(URLWhatsapp);
            Form browserForm1 = new Form();
            browserForm1.setLayout(new BorderLayout());
            browserForm1.addComponent(BorderLayout.CENTER, browser);
            browserForm1.show();
        });
        add(shareButtonWA);
        ServiceArticles.getInstance().getArticalDetails(articleId, new ArticleCallback() {
            @Override
            public void onSuccess(Article article) {
                if (article != null) {
                    setTitle(article.getSujet());

                    String JSON = article.getUserId();

                    int firstNameStart = JSON.indexOf("firstName=") + 10; // 10 is the length of "firstName="
                    int firstNameEnd = JSON.indexOf(",", firstNameStart);
                    String firstName = JSON.substring(firstNameStart, firstNameEnd);

                    int lastNameStart = JSON.indexOf("lastName=") + 9; // 9 is the length of "lastName="
                    String lastName = JSON.substring(lastNameStart, JSON.length() - 1);

                    add(new SpanLabel("Publié Par : " + firstName + " " + lastName));

                    add(new SpanLabel("Vues : " + Integer.toString(article.getViews())));

                    ImageViewer iv = new ImageViewer();
                    add(iv);

                    add(new SpanLabel(article.getContenu()));
                    add(new SpanLabel("Liste Commentaires : ------------------------------------------------------------------------ "));
                    String commentJSON = article.getComments();
                    int idIndex = commentJSON.indexOf("id=");
                    int startIndex = commentJSON.indexOf(", contenu=");
                    int firstNameIndex = commentJSON.indexOf("firstName=");
                    int lastNameIndex = commentJSON.indexOf("lastName=");
                    int dateIndex = commentJSON.indexOf("createdAt=");

                    while (startIndex != -1 && firstNameIndex != -1 && lastNameIndex != -1 && dateIndex != -1) {
                        int endId = commentJSON.indexOf(".0", idIndex);
                        int endIndex = commentJSON.indexOf(", cr", startIndex);
                        int endIndexDate = commentJSON.indexOf(", u", dateIndex + 1);
                        int endIndefirst = commentJSON.indexOf(", l", firstNameIndex + 1);
                        int endIndexlast = commentJSON.indexOf("}", lastNameIndex + 1);

                        String id = commentJSON.substring(idIndex + 3, endId);
                        String contenu = commentJSON.substring(startIndex + 10, endIndex);
                        String dateComment = commentJSON.substring(dateIndex + 10, endIndexDate);
                        String firstNameComment = commentJSON.substring(firstNameIndex + 10, endIndefirst);
                        String lastNameComment = commentJSON.substring(lastNameIndex + 9, endIndexlast);
                        String input = dateComment;

                        String date = input.substring(0, 10); // extract date
                        String time = input.substring(11, 19); // extract time

                        add(new SpanLabel(id + "\n" + firstNameComment + " " + lastNameComment + " a commenté le " + date + " à " + time + " : \n" + contenu));

                        idIndex = commentJSON.indexOf("id=", endId);
                        startIndex = commentJSON.indexOf(", contenu=", endIndex);
                        firstNameIndex = commentJSON.indexOf("firstName=", endIndefirst);
                        lastNameIndex = commentJSON.indexOf("lastName=", endIndexlast);
                        dateIndex = commentJSON.indexOf("createdAt=", endIndexDate);
                        Button update = new Button("Update Comment");

                        update.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                // Redirect to another form
                                new UpdateCommentForm(id, articleId).show();
                            }
                        });
                        add(update);
                        Button delete = new Button("delete");

                        delete.addActionListener(evt -> {
                            int articleId = article.getId();

                            // perform deletion logic here
                            // ...
                            // show a confirmation dialog if necessary
                            boolean confirmed = Dialog.show("Confirm", "Are you sure you want to delete?", "Yes", "No");
                            if (confirmed) {
                                boolean success = ServiceArticles.getInstance().deleteComment(id);
                                if (success) {
                                    Dialog.show("Success", "Comment deleted", "OK", null);
                                    new ArticleDetailForm(articleId).show();
                                } else {
                                    Dialog.show("Error", "Failed to delete article", "OK", null);
                                }

                            }
                        });
                        add(delete);
                        add(new SpanLabel("--------------------------------------------------------------------------------------------------------- "));

                    }

                    Button comment = new Button("Add comment");

                    comment.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            int articleId = article.getId();
                            // Redirect to another form
                            new AddCommentForm(articleId).show();
                        }
                    });
                    add(comment);

                    Image img;
                    try {
                        String URL = "http://127.0.0.1:8000/uploads/images/" + article.getImage();
                        ec = EncodedImage.create("/load.png");
                        img = URLImage.createToStorage(ec, URL, URL, URLImage.RESIZE_FAIL).scaled(scaledWidth, scaledHeight);
                        iv.setImage(img);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    setTitle("Article not found");
                    add(new SpanLabel("Sorry, the requested article could not be found."));
                }
            }

            @Override
            public void onError(Throwable t) {
                Dialog.show("Error", "An error occurred: " + t.getMessage(), "OK", null);
            }

        });
    }

}

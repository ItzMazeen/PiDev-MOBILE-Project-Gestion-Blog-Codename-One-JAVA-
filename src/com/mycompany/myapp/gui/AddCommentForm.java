/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Comment;
import com.mycompany.myapp.services.ServiceArticles;

/**
 *
 * @author bhk
 */
public class AddCommentForm extends Form {

    public AddCommentForm(int articleId) {
        setTitle("Add a new comment");
        setLayout(BoxLayout.y());
        TextField tfContenu = new TextField("", "Contenu Commentaire");
        TextField tfUser = new TextField("", "User ID");

        Button btnValider = new Button("Add Comment");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((tfContenu.getText().length() == 0) || (tfUser.getText().length() == 0)) {
                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
                } else {
                    try {

                        Comment c = new Comment(tfContenu.getText().toString(), tfUser.getText().toString());
                        if (ServiceArticles.getInstance().addComment(c, articleId)) {
                            Dialog.show("Success", "Connection accepted", new Command("OK"));
                            new ArticleDetailForm(articleId).show();
                        } else {
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                        }
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }

                }

            }
        });

        addAll(tfContenu, tfUser, btnValider);
        Button backButton = new Button("Cancel");
        backButton.addActionListener(e -> {
            new ArticleDetailForm(articleId).show();
        });
        add(backButton);
    }

}

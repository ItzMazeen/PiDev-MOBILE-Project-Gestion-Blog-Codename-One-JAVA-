/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Article;
import com.mycompany.myapp.services.ServiceArticles;

/**
 *
 * @author bhk
 */
public class AddArticleForm extends Form {

    public AddArticleForm(Form previous) {
        setTitle("Add a new Article");
        setLayout(BoxLayout.y());
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        TextField tfSujet = new TextField("", "Sujet Article");
        TextField tfContenu = new TextField("", "Contenu Article");
        TextField tfImage = new TextField("", "Image Article");
        TextField tfUser = new TextField("", "User ID");

        Button btnValider = new Button("Add Article");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((tfSujet.getText().length() == 0) || (tfContenu.getText().length() == 0) || (tfImage.getText().length() == 0) || (tfUser.getText().length() == 0)) {
                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
                } else {
                    try {

                        Article t = new Article(tfSujet.getText().toString(), tfContenu.getText().toString(), tfImage.getText().toString(), tfUser.getText().toString());
                        if (ServiceArticles.getInstance().addArticle(t)) {
                            Dialog.show("Success", "Connection accepted", new Command("OK"));
                            new ListeArticlesForm().show();
                        } else {
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                        }
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }
                }
            }
        });

        addAll(tfSujet, tfContenu, tfImage, tfUser, btnValider);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }

}

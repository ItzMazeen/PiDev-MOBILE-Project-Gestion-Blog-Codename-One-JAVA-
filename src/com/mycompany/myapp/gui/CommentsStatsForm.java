/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Form;
import com.codename1.ui.*;
import com.mycompany.myapp.services.ServiceArticles;

import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.stat;
import com.mycompany.myapp.services.ServiceArticles.StatCallback;

/**
 *
 * @author mazee
 */
public class CommentsStatsForm extends Form {

    public CommentsStatsForm(Form previous) {
        setTitle("Comments stat");
        setLayout(BoxLayout.y());
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

        // Add previous button to the toolbar
        ServiceArticles.getInstance().getStats(new StatCallback() {
            @Override
            public void onSuccess(stat stat) {
                if (stat != null) {

                    add(new SpanLabel("Nombre commentaires : " + Integer.toString(stat.getNumComments())));
                    add(new SpanLabel("Unique utilisateur commenté : " + Integer.toString(stat.getCountUsers())));
                    add(new SpanLabel("This week comments : " + Integer.toString(stat.getWeekCount())));
                    add(new SpanLabel("This day comments : " + Integer.toString(stat.getTodayCount())));

                }
            }

            @Override
            public void onError(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
    }
}

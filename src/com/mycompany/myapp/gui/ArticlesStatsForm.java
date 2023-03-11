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

/**
 *
 * @author mazee
 */
public class ArticlesStatsForm extends Form {

    public ArticlesStatsForm(Form previous) {
        setTitle("Articles stat");
        setLayout(BoxLayout.y());
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

        ServiceArticles.getInstance().getArticalsStats(new ServiceArticles.StatArticalCallback() {
            @Override
            public void onSuccess(stat stat) {
                if (stat != null) {

                    add(new SpanLabel("Nombre Article : " + Integer.toString(stat.getNumComments())));
                    add(new SpanLabel("Unique utilisateur publié : " + Integer.toString(stat.getCountUsers())));
                    add(new SpanLabel("This week articals : " + Integer.toString(stat.getWeekCount())));
                    add(new SpanLabel("This day articals : " + Integer.toString(stat.getTodayCount())));

                }
            }

            @Override
            public void onError(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
    }
}

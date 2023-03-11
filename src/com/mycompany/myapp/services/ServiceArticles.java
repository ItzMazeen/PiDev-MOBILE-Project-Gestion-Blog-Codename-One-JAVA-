/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Article;
import com.mycompany.myapp.entities.Comment;
import com.mycompany.myapp.entities.stat;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bhk
 */
public class ServiceArticles {

    public ArrayList<Article> articles;

    public static ServiceArticles instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    private ServiceArticles() {
        req = new ConnectionRequest();
    }

    public static ServiceArticles getInstance() {
        if (instance == null) {
            instance = new ServiceArticles();
        }
        return instance;
    }

// add Artical :
    public boolean addArticle(Article t) {
        String sujet = t.getSujet();
        String contenu = t.getContenu();
        String image = t.getImage();
        String user = t.getUserId();

        String url = Statics.BASE_URL + "addArticleJSON/new?sujet=" + sujet + "&contenu=" + contenu + "&image=" + image + "&user=" + user;

        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

// parse all articals :
    public ArrayList<Article> parseArticles(String jsonText) {
        try {
            articles = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> articlesListJson
                    = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) articlesListJson.get("root");
            for (Map<String, Object> obj : list) {
                Article t = new Article();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
                t.setViews(((int) Float.parseFloat(obj.get("views").toString())));
                t.setSujet(obj.get("sujet").toString());
                t.setContenu(obj.get("contenu").toString());
                t.setUserId(obj.get("userId").toString());
                t.setComments(obj.get("comments").toString());
                t.setImage(obj.get("image").toString());
                t.setCreatedAt(obj.get("createdAt").toString());

                if (obj.get("createdAt") == null) {
                    t.setCreatedAt("createdAt");
                } else {
                    t.setCreatedAt(obj.get("createdAt").toString());
                }
                if (obj.get("sujet") == null) {
                    t.setSujet("null");
                } else {
                    t.setSujet(obj.get("sujet").toString());
                }
                if (obj.get("image") == null) {
                    t.setImage("null");
                } else {
                    t.setImage(obj.get("image").toString());
                }

                articles.add(t);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return articles;
    }

// parse article by id :
    public Article parseArticle(String jsonText) {
        Article t = new Article();
        try {
            JSONParser j = new JSONParser();
            Map<String, Object> articleJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            float id = Float.parseFloat(articleJson.get("id").toString());
            t.setId((int) id);
            t.setViews(((int) Float.parseFloat(articleJson.get("views").toString())));
            t.setSujet(articleJson.get("sujet").toString());
            t.setContenu(articleJson.get("contenu").toString());
            t.setComments(articleJson.get("comments").toString());
            t.setImage(articleJson.get("image").toString());
            t.setCreatedAt(articleJson.get("createdAt").toString());
            t.setUserId(articleJson.get("userId").toString());
            t.setCreatedAt(articleJson.get("createdAt") == null ? null : articleJson.get("createdAt").toString());
            t.setSujet(articleJson.get("sujet") == null ? null : articleJson.get("sujet").toString());
            t.setImage(articleJson.get("image") == null ? null : articleJson.get("image").toString());
            t.setSujet(articleJson.get("sujet") == null ? null : articleJson.get("sujet").toString());
            t.setImage(articleJson.get("image") == null ? null : articleJson.get("image").toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return t;
    }

// parse Comments stats : 
    public stat parseStats(String jsonText) {
        stat s = new stat();
        try {
            JSONParser j = new JSONParser();
            Map<String, Object> articleJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            s.setNumComments(((int) Float.parseFloat(articleJson.get("numComments").toString())));
            s.setCountUsers(((int) Float.parseFloat(articleJson.get("countUsers").toString())));
            s.setWeekCount(((int) Float.parseFloat(articleJson.get("weekCount").toString())));
            s.setTodayCount(((int) Float.parseFloat(articleJson.get("todayCount").toString())));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return s;
    }

// parse Articles stats :
    public stat parseArticleStats(String jsonText) {
        stat s = new stat();
        try {
            JSONParser j = new JSONParser();
            Map<String, Object> articleJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            s.setNumComments(((int) Float.parseFloat(articleJson.get("numArticles").toString())));
            s.setCountUsers(((int) Float.parseFloat(articleJson.get("countUsers").toString())));
            s.setWeekCount(((int) Float.parseFloat(articleJson.get("weekCount").toString())));
            s.setTodayCount(((int) Float.parseFloat(articleJson.get("todayCount").toString())));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return s;
    }

// get all articlas :
    public ArrayList<Article> getAllArticles() {
        String url = Statics.BASE_URL + "getAllArticals/";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                articles = parseArticles(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return articles;
    }

    public interface ArticleCallback {

        void onSuccess(Article article);

        void onError(Throwable throwable);
    }

// get Artical by ID :
    public void getArticalDetails(int articleId, ArticleCallback callback) {
        String url = Statics.BASE_URL + "getArtical/" + articleId;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Article article = parseArticle(new String(req.getResponseData()));
                    callback.onSuccess(article);
                } catch (Throwable t) {
                    callback.onError(t);
                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }

// update article : 
    public boolean updateArticle(Article t, int articleId) {

        String sujet = t.getSujet();
        String contenu = t.getContenu();
        String image = t.getImage();

        String url = Statics.BASE_URL + "updateArticleAPI/" + articleId + "?sujet=" + sujet + "&contenu=" + contenu + "&image=" + image;

        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

// update Comment :
    public boolean updateComment(Article t, String id) {

        String contenu = t.getContenu();

        String url = "http://127.0.0.1:8000/adminCommentaires/updateCommentAPI/" + id + "?contenu=" + contenu;

        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

// add Comment :
    public boolean addComment(Comment c, int articleId) {
        String user = c.getUserID();
        String contenu = c.getContenu();

        String url = Statics.BASE_URL + articleId + "/commentaires/addCommentJSON/new?user=" + user + "&contenu=" + contenu;

        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

// delete Artical :
    public boolean deleteArticle(int articleId) {

        String url = Statics.BASE_URL + "removeArticleAPI/" + articleId;

        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

// delete Comment :
    public boolean deleteComment(String id) {

        String url = "http://127.0.0.1:8000/adminCommentaires/removeCommentAPI/" + id;

        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public interface StatCallback {

        void onSuccess(stat stat);

        void onError(Throwable throwable);
    }

// get Comments stats :
    public void getStats(StatCallback callback) {
        String url = "http://127.0.0.1:8000/adminCommentaires/adminAPI";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    stat stat = parseStats(new String(req.getResponseData()));
                    callback.onSuccess(stat);
                } catch (Throwable t) {
                    callback.onError(t);
                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    public interface StatArticalCallback {

        void onSuccess(stat stat);

        void onError(Throwable throwable);
    }

// get Artical Stat :
    public void getArticalsStats(StatArticalCallback callback) {
        String url = "http://127.0.0.1:8000/adminArticles/adminAPI";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    stat stat = parseArticleStats(new String(req.getResponseData()));
                    callback.onSuccess(stat);
                } catch (Throwable t) {
                    callback.onError(t);
                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
}

package com.accordo.data;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppModel {

    private final String TAG = "MYTAG_AppModel";
    private final String PVERSION = "pversion";
    private final String PICTURE = "img";

    private static AppModel instance;
    private List<Channel> channels;
    private HashMap<String, ArrayList<Post>> posts;
    private HashMap<String, HashMap<String,Object>> profilePictures;

    public AppModel(){
        channels = new ArrayList<>();
        posts = new HashMap<>();
        profilePictures = new HashMap<>();
    }

    public static synchronized AppModel getInstance() {
        if(instance == null) instance = new AppModel();
        return instance;
    }

    public void addChannel(Channel channel) { channels.add(channel); }

    public void addPost(Post post, String cTitle) {

        ArrayList<Post> temp = posts.getOrDefault(cTitle, new ArrayList<>());
        if (temp != null) temp.add(post);

        posts.put(cTitle,temp);
    }

    public void addPost(JSONObject post, String cTitle) {
        Post p = null;
        try {
            String author = post.get("name").toString().length() > 0 ? post.get("name").toString() : "user"+post.get("uid").toString();
            switch (post.get("type").toString()) {
                case "t": {
                    p = new TextPost(post.get("pid").toString(), author, cTitle, null);
                    p.setContent(post.get("content").toString());
                    break;
                }
                case "i": {
                    p = new ImagePost(post.get("pid").toString(), author, cTitle, null);
                    break;
                }
                case "l": {
                    p = new LocationPost(post.get("pid").toString(), author, cTitle, null);
                    p.setContent(post.get("lat").toString() + "," + post.get("lon").toString());
                    break;
                }
                default:
                    break;
            }
            ArrayList<Post> temp = posts.getOrDefault(cTitle, new ArrayList<>());
            if (temp != null && p != null) temp.add(p);
            posts.put(cTitle,temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addProfilePicture(String uid, Bitmap img, String pversion) {
        HashMap<String, Object> profilePic = new HashMap<>();
        profilePic.put(PVERSION, pversion);
        profilePic.put(PICTURE, img);
        profilePictures.put(uid,profilePic);
    }

    public int channelSize(String cTitle) {
        if(posts.get(cTitle) != null) return posts.get(cTitle).size();
        else return -1;
    }

    public int channelsSize() {return channels.size();}

    public void emptyWall() {
        channels.clear();
    }

    public boolean hasFullChannel(String cTitle) { return posts.containsKey(cTitle); }

    public boolean hasProfilePic(String uid) { return profilePictures.containsKey(uid); }

    public Channel getChannel(int position) { return channels.get(position); }

    public ArrayList<Post> getChannelPosts(String cTitle) { return posts.get(cTitle); }

    public Post getPost(String cTitle, int position) { return posts.get(cTitle).get(position); }

    public Post getPost(String cTitle, String pid) {
        for(Post p : posts.get(cTitle)) if(p.getPid().equals(pid)) return p;
        return null;
    }

    public int getPostPosition(String cTitle, Post p){
        int pos = -1;
        for(int i = 0; i < posts.get(cTitle).size()-1; i++)
            if(posts.get(cTitle).get(i).getPid().equals(p.getPid())) {
                pos = i;
            }
        return pos;
    }

    public Bitmap getProfilePicture(String uid) {
        return profilePictures.getOrDefault(uid,null) != null ? (Bitmap) profilePictures.getOrDefault(uid,null).getOrDefault(PICTURE,null) : null;
    }

    public int getProfilePictureVersion(String uid) {
        return profilePictures.getOrDefault(uid,null) != null ?  Integer.parseInt(profilePictures.getOrDefault(uid,null).getOrDefault(PVERSION,null).toString()) : -1;
    }

    public void updatePost(String cTitle, Post p) {
        for(int i = 0; i < posts.get(cTitle).size()-1; i++)
            if(posts.get(cTitle).get(i).getPid().equals(p.getPid())) {
                posts.get(cTitle).set(i, p);
            }
    }

}

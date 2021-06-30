package com.accordo.data;

import android.graphics.Bitmap;
import android.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.accordo.data.AccordoValues.PICTURE;
import static com.accordo.data.AccordoValues.PVERSION;

public class AppModel {

    private final String TAG = "MYTAG_AppModel";

    private static AppModel instance;
    private final List<Channel> channels;
    private final ArrayMap<String, ArrayList<Post>> posts;
    private final ArrayMap<String, ArrayMap<String,Object>> profilePictures;

    public AppModel(){
        channels = new ArrayList<>();
        posts = new ArrayMap<>();
        profilePictures = new ArrayMap<>();
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
                    p = new TextPost(post.get("pid").toString(), post.get("uid").toString(), author, cTitle);
                    p.setContent(post.get("content").toString());
                    break;
                }
                case "i": {
                    p = new ImagePost(post.get("pid").toString(), post.get("uid").toString(), author, cTitle);
                    break;
                }
                case "l": {
                    p = new LocationPost(post.get("pid").toString(), post.get("uid").toString(), author, cTitle);
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
        ArrayMap<String, Object> profilePic = new ArrayMap<>();
        profilePic.put(PVERSION, pversion);
        profilePic.put(PICTURE, img);
        profilePictures.put(uid,profilePic);
    }

    public int channelSize(String cTitle) { return posts.get(cTitle) != null ? Objects.requireNonNull(posts.get(cTitle)).size() : -1; }

    public int channelsSize() {return channels.size();}

    public void emptyWall() { channels.clear(); }

    public boolean hasFullChannel(String cTitle) { return posts.containsKey(cTitle); }

    public boolean hasProfilePic(String uid) { return profilePictures.containsKey(uid); }

    public Channel getChannel(int position) { return channels.get(position); }

    public ArrayList<Post> getChannelPosts(String cTitle) { return posts.get(cTitle); }

    public Post getPost(String cTitle, int position) { return Objects.requireNonNull(posts.get(cTitle)).get(position); }

    public Post getPost(String cTitle, String pid) {
        for(Post p : Objects.requireNonNull(posts.get(cTitle))) if(p.getPid().equals(pid)) return p;
        return null;
    }

    public int getPostPosition(String cTitle, Post p){
        for(int i = 0; i < Objects.requireNonNull(posts.get(cTitle)).size()-1; i++) if(Objects.requireNonNull(posts.get(cTitle)).get(i).getPid().equals(p.getPid())) return i;
        return -1;
    }

    public Bitmap getProfilePicture(String uid) { return profilePictures.getOrDefault(uid,null) != null ? (Bitmap) Objects.requireNonNull(profilePictures.getOrDefault(uid, null)).getOrDefault(PICTURE,null) : null; }

    public int getProfilePictureVersion(String uid) { return profilePictures.getOrDefault(uid,null) != null ?  Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(profilePictures.getOrDefault(uid, null)).getOrDefault(PVERSION, null)).toString()) : -1; }

    public void updatePost(String cTitle, Post p) { for(int i = 0; i < Objects.requireNonNull(posts.get(cTitle)).size()-1; i++) if(Objects.requireNonNull(posts.get(cTitle)).get(i).getPid().equals(p.getPid())) Objects.requireNonNull(posts.get(cTitle)).set(i, p); }
}

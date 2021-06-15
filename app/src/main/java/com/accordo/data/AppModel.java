package com.accordo.data;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppModel {

    private static AppModel instance;
    private List<User> users;
    private List<Channel> channels;
    private HashMap<String, ArrayList<Post>> posts;
    private final String TAG = "MYTAG_AppModel";

    public AppModel(){
        users = new ArrayList<>();
        channels = new ArrayList<>();
        posts = new HashMap<>();
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

    public int channelSize(String cTitle) {
        if(posts.get(cTitle) != null) return posts.get(cTitle).size();
        else return -1;
    }

    public int channelsSize() {return channels.size();}

    public boolean hasChannel(String cTitle) { return posts.containsKey(cTitle); }

    public Channel getChannel(int position) { return channels.get(position); }

    public Post getPost(int position, String cTitle) { return posts.get(cTitle).get(position); }

    public ArrayList<Post> getChannel(String cTitle) { return posts.get(cTitle); }

}

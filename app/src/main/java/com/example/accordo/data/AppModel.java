package com.example.accordo.data;

import java.util.ArrayList;
import java.util.List;

public class AppModel {

    private static AppModel instance;
    private List<User> users;
    private List<Channel> channels;
    private List<Post> posts;

    public AppModel(){
        users = new ArrayList<User>();
        channels = new ArrayList<Channel>();
        posts = new ArrayList<Post>();
    }

    public static synchronized AppModel getInstance() {
        if(instance == null) instance = new AppModel();
        return instance;
    }

    public List<Channel> getAllChannels() { return channels; }

    public List<Post> getAllPosts() { return posts; }

    public List<User> getAllUsers() { return users; }

    public void addChannel(Channel channel) { channels.add(channel); }

    public void addPost(Post post) { posts.add(post); }

    public void addUser(User user) { users.add(user); }
}

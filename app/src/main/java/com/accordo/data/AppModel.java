package com.accordo.data;

import java.util.ArrayList;
import java.util.List;

public class AppModel {

    private static AppModel instance;
    private List<User> users;
    private List<Channel> channels;
    private List<Post> posts;
    private User currentUser;

    public AppModel(){
        users = new ArrayList<>();
        channels = new ArrayList<>();
        posts = new ArrayList<>();
    }

    public static synchronized AppModel getInstance() {
        if(instance == null) instance = new AppModel();
        return instance;
    }

    public void addChannel(Channel channel) { channels.add(channel); }

    public void addPost(Post post) { posts.add(post); }

    public void addUser(User user) { users.add(user); }

    public List<Channel> getAllChannels() { return channels; }

    public List<Post> getAllPosts() { return posts; }

    public List<User> getAllUsers() { return users; }

    public Channel getChannel(int position) { return channels.get(position); }

    public int getChannelsSize() {return channels.size();}











}

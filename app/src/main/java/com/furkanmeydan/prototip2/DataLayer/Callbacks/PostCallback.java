package com.furkanmeydan.prototip2.DataLayer.Callbacks;

import com.furkanmeydan.prototip2.Models.Post;

import java.util.List;

public abstract class PostCallback {

    public void onPostAdded(Post post){
        
    }

    public void getPost(Post post){

    }

    public void getPosts(List<Post> list){

    }

    public void onWishNOTUpdated(){

    }

    public void onWishUpdated(){

    }

    public void OnWishListRetrieved(List<Post> list){

    }

    public void getMyPosts(List<Post> list){

    }

    public void onPostDeleted(){

    }

    public void onPostUpdated(){

    }

    public void deleteWishOnBlock(){

    }

}

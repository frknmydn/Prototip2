package com.furkanmeydan.prototip2.Views.PostActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furkanmeydan.prototip2.DataLayer.BlockDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Block;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Adapters.SearchResultRecyclerAdapter;
import com.furkanmeydan.prototip2.R;

import java.util.ArrayList;
import java.util.List;

public class PostSearchResultFragment extends Fragment {
    PostDAL postDAL;
    BlockDAL blockDAL;
    PostActivity postActivity;
    private String city;
    private String gender;
    private int direction;
    private long timestamp1;
    private long timestamp2;
    Double userlat1,userlat2,userlng1,userlng2;


    //RCL işlemleri için
    SearchResultRecyclerAdapter resultAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;
    ArrayList<Block> blockList;



    public PostSearchResultFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<>();
        blockDAL = new BlockDAL();
        blockList = new ArrayList<>();

        postDAL = new PostDAL();
        postActivity = (PostActivity) getActivity();

        if (getArguments() != null) {

            city = getArguments().getString("city");
            gender = getArguments().getString("gender");
            timestamp1 = getArguments().getLong("timestamp1");
            timestamp2 = getArguments().getLong("timestamp2");
            userlat1 = getArguments().getDouble("userlat1");
            userlat2 = getArguments().getDouble("userlat2");
            userlng1 = getArguments().getDouble("userlng1");
            userlng2 = getArguments().getDouble("userlng2");
            direction = getArguments().getInt("direction");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.postSearchResultRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        resultAdapter = new SearchResultRecyclerAdapter(posts);
        recyclerView.setAdapter(resultAdapter);

        if(gender.equals("Fark Etmez")){
            postDAL.getPostsWithOUTGender(userlat1, userlat2, userlng1, userlng2, timestamp1, timestamp2, city, postActivity,direction, new PostCallback() {
                @Override
                public void getPosts(List<Post> list) {
                    super.getPosts(list);
                    final List<Post> filteredList = postDAL.filterWithLTGLNG(list,userlat2,userlng2,userlat1,userlng1);

                    blockDAL.getBlockedListForPosts(new BlockCallback() {
                        @Override
                        public void onListRetrieved(List<Block> list) {
                            super.onListRetrieved(list);
                            for(Post postvar : filteredList){
                                for(Block blockvar : list){
                                    if(!blockvar.getUserBlockerID().equals(postvar.getOwnerID())){
                                        posts.add(postvar);
                                    }
                                }
                            }
                            resultAdapter.notifyDataSetChanged();

                        }
                    });




                    //Log.d("TagPostGetFragment", list.get(0).getDestination());

                }
            });
        }
        else {

            postDAL.getPostsWithGender(userlat1, userlat2, userlng1, userlng2,timestamp1, timestamp2, gender, city, postActivity,direction, new PostCallback() {
                @Override
                public void getPosts(List<Post> list) {
                    super.getPosts(list);
                    final List<Post> filteredList = postDAL.filterWithLTGLNG(list,userlat2,userlng2,userlat1,userlng1);
                    Log.d("Tag","FilteredListSize"+ filteredList.size());

                    blockDAL.getBlockedListForPosts(new BlockCallback() {
                        @Override
                        public void onListRetrieved(List<Block> list) {
                            super.onListRetrieved(list);
                            Log.d("Tag","onListRetrieved Block içi ");

                            if(list.size() > 0 ){
                                Log.d("Tag","onListRetrievedBlock if içi ");
                                for(Post postvar : filteredList){
                                    Log.d("Tag","onListRetrievedBlock ilk for içi ");
                                    Log.d("Tag","PostOwner "+postvar.getOwnerID());
                                    for(Block blockvar : list){
                                        Log.d("Tag","onListRetrievedBlock ikinci for içi ");
                                        Log.d("Tag","Blocklist size "+list.size());
                                        Log.d("Tag","BlockerID "+blockvar.getUserBlockerID());
                                        if(!blockvar.getUserBlockedID().equals(postvar.getOwnerID())){
                                            Log.d("Tag","onListRetrievedBlock en son if içi ");
                                            posts.add(postvar);
                                            Log.d("Tag","postsSize"+ posts.size());
                                        }
                                    }
                                }
                            }
                            else{
                                Log.d("Tag","onListRetrievedBlock else AddAll (Block Yok) ");
                                posts.addAll(filteredList);
                            }

                            resultAdapter.notifyDataSetChanged();

                        }
                    });

                    //Log.d("TagPostGetFragment", list.get(0).getDestination());

                }
            });
        }

        Log.d("TagSearchResult",city);
        Log.d("TagSearchResult",gender);
        Log.d("TagSearchResult", String.valueOf(timestamp1));Log.d("TagSearchResult", String.valueOf(timestamp2));
    }
}
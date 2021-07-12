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
import com.furkanmeydan.prototip2.Views.PostActivity.PostActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    Set singleBlockAndPostIDs;

    ArrayList<String> newArrayList;

    //RCL işlemleri için
    SearchResultRecyclerAdapter resultAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;
    ArrayList<Block> blockList;
    ArrayList<String> blockedAndPosted;



    public PostSearchResultFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<>();
        blockDAL = new BlockDAL();
        blockList = new ArrayList<>();
        blockedAndPosted = new ArrayList<>();
        newArrayList = new ArrayList<>();

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

        resultAdapter = new SearchResultRecyclerAdapter(posts, postActivity);
        recyclerView.setAdapter(resultAdapter);

        if(gender.equals("Fark Etmez")){
            postDAL.getPostsWithOUTGender(timestamp1, timestamp2, city, postActivity,direction, new PostCallback() {
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

            postDAL.getPostsWithGender(timestamp1, timestamp2, gender, city, postActivity,direction, new PostCallback() {
                @Override
                public void getPosts(List<Post> list) {
                    super.getPosts(list);
                    final List<Post> filteredList = postDAL.filterWithLTGLNG(list,userlat2,userlng2,userlat1,userlng1);
                    Log.d("Tag","FilteredListSize"+ filteredList.size());

                    blockDAL.getBlockedListForPosts(new BlockCallback() {
                        @Override
                        public void onListRetrieved(List<Block> list) {
                            super.onListRetrieved(list);
                            /*
                            ArrayList<String> blockedIDs = new ArrayList<>();
                            for(Block blockvar : list){
                                blockedIDs.add(blockvar.getUserBlockedID());
                                Log.d("Tag","BlockedIDs size "+ blockedIDs.size());
                            }
                            ArrayList<String> postOwnerIDs = new ArrayList<>();
                            for(Post postvar: filteredList){
                                postOwnerIDs.add(postvar.getOwnerID());
                                Log.d("Tag","PostOwnerIDs size "+ postOwnerIDs.size());
                            }

                            newArrayList.addAll(blockedIDs);
                            newArrayList.addAll(postOwnerIDs);
                            Log.d("Tag","newArrayList size "+ newArrayList.size());
                            singleBlockAndPostIDs = new HashSet<>();
                            for(String str : newArrayList){
                                if(!singleBlockAndPostIDs.add(str)){
                                    blockedAndPosted.add(str);
                                    Log.d("Tag","BlockedandPosted size "+ blockedAndPosted.size());
                                }
                            }

                            for(Post postvar : filteredList){
                                for(String stringvar: blockedAndPosted){
                                    if(!postvar.getOwnerID().equals(stringvar)){
                                        if(!posts.contains(postvar)){
                                            posts.add(postvar);
                                            Log.d("Tag","Eklenen ama blocklanan ID "+ stringvar);
                                            Log.d("Tag","Eklenen Post Owner ID "+ postvar.getOwnerID());
                                            Log.d("Tag","posts size "+ posts.size());
                                        }
                                    }else{
                                        Log.d("Tag","Eklenmeyen ID "+ stringvar);
                                        Log.d("Tag","PostOwner ID "+ postvar.getOwnerID());
                                    }
                                }
                            }
                            */

                            Log.d("Tag","onListRetrieved Block içi ");

                            if(list.size() > 0 ){
                                Log.d("Tag","onListRetrievedBlock if içi ");
                                for(Post postvar : filteredList){
                                    boolean isBlocked = false;
                                    Log.d("Tag","onListRetrievedBlock ilk for içi ");
                                    Log.d("Tag","PostOwner "+postvar.getOwnerID());
                                    Log.d("Tag","PostListSize "+filteredList.size());
                                    for(Block blockvar : list){
                                        Log.d("Tag","onListRetrievedBlock ikinci for içi ");
                                        Log.d("Tag","Blocklist size "+list.size());
                                        Log.d("Tag","BlockerID "+blockvar.getUserBlockerID());
                                        if((blockvar.getUserBlockedID().equals(postvar.getOwnerID()))){
                                            isBlocked = true;
                                            Log.d("Tag","UserBlockedID equals OwnerID if içi ");
                                            Log.d("Tag","BlockedID "+blockvar.getUserBlockedID());
                                        }
                                    }
                                    if(!isBlocked){
                                        posts.add(postvar);
                                        Log.d("Tag","postsSize "+ posts.size());
                                        Log.d("Tag","postOwnerID "+ postvar.getOwnerID());
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
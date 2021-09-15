package com.furkanmeydan.prototip2.Views.PostActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.furkanmeydan.prototip2.DataLayer.BlockDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.Block;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Adapters.SearchResultRecyclerAdapter;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
    ConstraintLayout layoutProgress, layoutContent, layoutInfo;

    ArrayList<String> newArrayList;

    //RCL işlemleri için
    SearchResultRecyclerAdapter resultAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;
    ArrayList<Block> blockList;
    ArrayList<String> blockedAndPosted;

    boolean isScrolling = false;
    boolean isLastItemReached = false;

    DocumentSnapshot lastVisible;


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

            Log.d("Tag", "timestamp1: "+timestamp1);
            Log.d("Tag", "timestamp2: "+timestamp2);
            Log.d("Tag", "city: "+city);
            Log.d("Tag", "gender: "+gender);
            Log.d("Tag", "direction: "+ direction);

        }
        else{
            Log.d("TAG", "GETARGS NULL GELDİ ");
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

        layoutInfo = view.findViewById(R.id.resultLayoutInfo);
        layoutProgress = view.findViewById(R.id.resultLayoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.postSearchResultRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        resultAdapter = new SearchResultRecyclerAdapter(posts, postActivity);
        recyclerView.setAdapter(resultAdapter);


        if(gender.equals("Fark Etmez")){
            postDAL.getPostsWithOUTGender(timestamp1, timestamp2, city, postActivity,direction, new PostCallback() {
                @Override
                public void getPosts(List<Post> list, Task<QuerySnapshot> task) {
                    super.getPosts(list, task);
                    if(list.size() > 0){
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
                                    if(posts.size()>0){
                                        layoutProgress.setVisibility(View.GONE);
                                        layoutInfo.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);

                                        resultAdapter.notifyDataSetChanged();
                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                super.onScrollStateChanged(recyclerView, newState);
                                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                                    isScrolling = true;
                                                    Log.d("TAG", "onStateChanged: ");
                                                }
                                            }

                                            @Override
                                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                super.onScrolled(recyclerView, dx, dy);
                                                Log.d("TAG", "onScrolled: ");

                                                LinearLayoutManager tempLinear = (LinearLayoutManager) recyclerView.getLayoutManager();
                                                int firstVisibleItemPosition = tempLinear.findFirstVisibleItemPosition();
                                                int visibleItemCount = tempLinear.getChildCount();
                                                int totalItemCount = tempLinear.getItemCount();


                                                if(isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached){
                                                    isScrolling = false;

                                                    Query query = postDAL.getFirestore().collectionGroup(CollectionHelper.POST_COLLECTION)
                                                            .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                                                            .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, city)
                                                            .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER, gender)
                                                            .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                                                            .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                                                            .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                                                            .orderBy(CollectionHelper.POST_TIMESTAMP)
                                                            .startAfter(lastVisible)
                                                            .limit(postDAL.getLimit());

                                                    postDAL.executeQuery(query, new PostCallback() {
                                                        @Override
                                                        public void onQueryExecuted(List<Post> list, Task<QuerySnapshot> task) {
                                                            super.onQueryExecuted(list, task);
                                                            Log.d("TAG", "onQueryExecuted: "+ list.size());
                                                            posts.addAll(list);

                                                            resultAdapter.notifyDataSetChanged();

                                                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                            if(task.getResult().size() < postDAL.getLimit()){
                                                                isLastItemReached = true;
                                                                Log.d("TAG", "isLastItemReached: "+ isLastItemReached);

                                                            }
                                                        }
                                                    });
                                                }




                                            }
                                        };
                                        recyclerView.addOnScrollListener(onScrollListener);
                                        resultAdapter.notifyDataSetChanged();


                                    }
                                    else {
                                        layoutProgress.setVisibility(View.GONE);
                                        layoutInfo.setVisibility(View.VISIBLE);
                                    }
                                }
                                else{
                                    Log.d("Tag","onListRetrievedBlock else AddAll (Block Yok) ");
                                    posts.addAll(filteredList);
                                    layoutProgress.setVisibility(View.GONE);
                                    layoutInfo.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    resultAdapter.notifyDataSetChanged();
                                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                                isScrolling = true;
                                                Log.d("TAG", "onStateChanged: ");
                                            }
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            Log.d("TAG", "onScrolled: ");

                                            LinearLayoutManager tempLinear = (LinearLayoutManager) recyclerView.getLayoutManager();
                                            int firstVisibleItemPosition = tempLinear.findFirstVisibleItemPosition();
                                            int visibleItemCount = tempLinear.getChildCount();
                                            int totalItemCount = tempLinear.getItemCount();


                                            if(isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached){
                                                isScrolling = false;

                                                Query query = postDAL.getFirestore().collectionGroup(CollectionHelper.POST_COLLECTION)
                                                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                                                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, city)
                                                        .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                                                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                                                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                                                        .orderBy(CollectionHelper.POST_TIMESTAMP)
                                                        .startAfter(lastVisible)
                                                        .limit(postDAL.getLimit());

                                                postDAL.executeQuery(query, new PostCallback() {
                                                    @Override
                                                    public void onQueryExecuted(List<Post> list, Task<QuerySnapshot> task) {
                                                        super.onQueryExecuted(list, task);
                                                        Log.d("TAG", "onQueryExecuted: "+ list.size());
                                                        posts.addAll(list);

                                                        resultAdapter.notifyDataSetChanged();

                                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                        if(task.getResult().size() < postDAL.getLimit()){
                                                            isLastItemReached = true;
                                                            Log.d("TAG", "isLastItemReached: "+ isLastItemReached);

                                                        }
                                                    }
                                                });
                                            }




                                        }
                                    };
                                    recyclerView.addOnScrollListener(onScrollListener);
                                    resultAdapter.notifyDataSetChanged();
                                }




                            }
                        });

                        //Log.d("TagPostGetFragment", list.get(0).getDestination());
                    }
                    else{
                        layoutProgress.setVisibility(View.GONE);
                        layoutInfo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });
        }


        else {

            postDAL.getPostsWithGender(timestamp1, timestamp2, gender, city, direction, new PostCallback() {
                @Override
                public void getPosts(List<Post> list, Task<QuerySnapshot> task) {
                    super.getPosts(list, task);
                    if(list.size() > 0){
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
                                    Log.d("Tag","Blocklu User Var ");
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
                                    if(posts.size()>0){
                                        layoutProgress.setVisibility(View.GONE);
                                        layoutInfo.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                    if(posts.size() <= postDAL.getLimit()){
                                        Log.d("TAG", "POSTSIZE 4'TEN KÜÇÜK: ");
                                        Log.d("TAG", "POSTS SIZE: " + posts.size());
                                        getMoreData();
                                        }

                                        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                super.onScrollStateChanged(recyclerView, newState);
                                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                                    isScrolling = true;
                                                    Log.d("TAG", "onStateChanged: ");
                                                }
                                            }

                                            @Override
                                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                super.onScrolled(recyclerView, dx, dy);
                                                Log.d("TAG", "onScrolled: ");

                                                LinearLayoutManager tempLinear = (LinearLayoutManager) recyclerView.getLayoutManager();
                                                int firstVisibleItemPosition = tempLinear.findFirstVisibleItemPosition();
                                                int visibleItemCount = tempLinear.getChildCount();
                                                int totalItemCount = tempLinear.getItemCount();


                                                if(isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached){
                                                    isScrolling = false;

                                                    Query query = postDAL.getFirestore().collectionGroup(CollectionHelper.POST_COLLECTION)
                                                            .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                                                            .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, city)
                                                            .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER, gender)
                                                            .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                                                            .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                                                            .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                                                            .orderBy(CollectionHelper.POST_TIMESTAMP)
                                                            .startAfter(lastVisible)
                                                            .limit(postDAL.getLimit());

                                                    postDAL.executeQuery(query, new PostCallback() {
                                                        @Override
                                                        public void onQueryExecuted(List<Post> list, Task<QuerySnapshot> task) {
                                                            super.onQueryExecuted(list, task);
                                                            Log.d("TAG", "onQueryExecuted: "+ list.size());
                                                            posts.addAll(list);

                                                            resultAdapter.notifyDataSetChanged();

                                                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                            if(task.getResult().size() < postDAL.getLimit()){
                                                                isLastItemReached = true;
                                                                Log.d("TAG", "isLastItemReached: "+ isLastItemReached);

                                                            }
                                                        }
                                                    });
                                                }




                                            }
                                        };
                                        recyclerView.addOnScrollListener(onScrollListener);
                                        resultAdapter.notifyDataSetChanged();


                                    }
                                    else {
                                        layoutProgress.setVisibility(View.GONE);
                                        layoutInfo.setVisibility(View.VISIBLE);
                                    }
                                }
                                else{
                                    Log.d("Tag","onListRetrievedBlock else AddAll (Block Yok) ");
                                    posts.addAll(filteredList);
                                    layoutProgress.setVisibility(View.GONE);
                                    layoutInfo.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    resultAdapter.notifyDataSetChanged();
                                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                                isScrolling = true;
                                                Log.d("TAG", "onStateChanged: ");
                                            }
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            Log.d("TAG", "onScrolled: ");

                                            LinearLayoutManager tempLinear = (LinearLayoutManager) recyclerView.getLayoutManager();
                                            int firstVisibleItemPosition = tempLinear.findFirstVisibleItemPosition();
                                            int visibleItemCount = tempLinear.getChildCount();
                                            int totalItemCount = tempLinear.getItemCount();


                                            if(isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached){
                                                isScrolling = false;

                                                Query query = postDAL.getFirestore().collectionGroup(CollectionHelper.POST_COLLECTION)
                                                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                                                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, city)
                                                        .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER, gender)
                                                        .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                                                        .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                                                        .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                                                        .orderBy(CollectionHelper.POST_TIMESTAMP)
                                                        .startAfter(lastVisible)
                                                        .limit(postDAL.getLimit());

                                                postDAL.executeQuery(query, new PostCallback() {
                                                    @Override
                                                    public void onQueryExecuted(List<Post> list, Task<QuerySnapshot> task) {
                                                        super.onQueryExecuted(list, task);
                                                        Log.d("TAG", "onQueryExecuted: "+ list.size());
                                                        posts.addAll(list);

                                                        resultAdapter.notifyDataSetChanged();

                                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                        if(task.getResult().size() < postDAL.getLimit()){
                                                            isLastItemReached = true;
                                                            Log.d("TAG", "isLastItemReached: "+ isLastItemReached);

                                                        }
                                                    }
                                                });
                                            }




                                        }
                                    };
                                    recyclerView.addOnScrollListener(onScrollListener);
                                }




                            }
                        });

                        //Log.d("TagPostGetFragment", list.get(0).getDestination());
                    }
                    else{
                        layoutProgress.setVisibility(View.GONE);
                        layoutInfo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                }
            });
        }

        Log.d("TagSearchResult",city);
        Log.d("TagSearchResult",gender);
        Log.d("TagSearchResult", String.valueOf(timestamp1));Log.d("TagSearchResult", String.valueOf(timestamp2));
    }

    public void getMoreData(){
        Log.d("TAG", "GETMOREDATA: ");
        Log.d("TAG", "POSTS SIZE: " + posts.size());

        Query query = postDAL.getFirestore().collectionGroup(CollectionHelper.POST_COLLECTION)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_STATUS, 1)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_CITY, city)
                .whereEqualTo(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_GENDER, gender)
                .whereEqualTo(CollectionHelper.POST_DIRECTION, direction)
                .whereGreaterThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp1)
                .whereLessThan(CollectionHelper.POSTSEARCH_COLLECTIONGROUP_TIMESTAMP, timestamp2)
                .orderBy(CollectionHelper.POST_TIMESTAMP)
                .startAfter(lastVisible)
                .limit(postDAL.getLimit());

        postDAL.executeQuery(query, new PostCallback() {
            @Override
            public void onQueryExecuted(List<Post> list, Task<QuerySnapshot> task) {
                super.onQueryExecuted(list, task);
                ArrayList<Post> postsTemp = new ArrayList<>(list);

                blockDAL.getBlockedListForPosts(new BlockCallback() {
                    @Override
                    public void onListRetrieved(List<Block> list) {
                        super.onListRetrieved(list);
                        if(list.size() > 0){
                            Log.d("Tag","Blocklu User Var ");
                            for(Post postvar : postsTemp){
                                boolean isBlocked = false;
                                Log.d("Tag","onListRetrievedBlock ilk for içi 2 ");
                                Log.d("Tag","PostOwner "+postvar.getOwnerID());
                                Log.d("Tag","PostListSize "+postsTemp.size());
                                for(Block blockvar : list){
                                    Log.d("Tag","onListRetrievedBlock ikinci for içi 2 ");
                                    Log.d("Tag","Blocklist size 2 "+list.size());
                                    Log.d("Tag","BlockerID 2 "+blockvar.getUserBlockerID());
                                    if((blockvar.getUserBlockedID().equals(postvar.getOwnerID()))){
                                        isBlocked = true;
                                        Log.d("Tag","UserBlockedID equals OwnerID if içi 2 ");
                                        Log.d("Tag","BlockedID 2 "+blockvar.getUserBlockedID());
                                    }
                                }
                                if(!isBlocked){
                                    posts.add(postvar);
                                    Log.d("Tag","postsSize 2 "+ posts.size());
                                    Log.d("Tag","postOwnerID 2 "+ postvar.getOwnerID());

                                }
                            }
                        }else{
                            Log.d("Tag", "blocklu user yok 2: ");
                            posts.addAll(postsTemp);
                        }
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        if(posts.size() < postDAL.getLimit()){
                            getMoreData();
                        }else{
                            resultAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}
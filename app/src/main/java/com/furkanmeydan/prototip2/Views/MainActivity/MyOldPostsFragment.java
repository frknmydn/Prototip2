package com.furkanmeydan.prototip2.Views.MainActivity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import com.furkanmeydan.prototip2.Adapters.myPostRecyclerAdapter;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.Models.CollectionHelper;
import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyOldPostsFragment extends Fragment {

    myPostRecyclerAdapter postRCLAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;
    MainActivity activity;
    ConstraintLayout layoutContent, layoutinfo, layoutProgress;

    PostDAL postDAL;
    DocumentSnapshot lastVisible;
    boolean isScrolling = false;
    boolean isLastItemReached = false;


    public MyOldPostsFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postDAL = new PostDAL();
        activity = (MainActivity) getActivity();
        posts = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_old_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.myOldPostsRCL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        layoutContent = view.findViewById(R.id.layoutOldPostsRCL);

        layoutProgress = view.findViewById(R.id.layoutOldPostProgress);
        layoutProgress.setVisibility(View.VISIBLE);
        layoutinfo = view.findViewById(R.id.consLayoutMyPosts);

        postRCLAdapter = new myPostRecyclerAdapter(activity,posts);
        recyclerView.setAdapter(postRCLAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMyPosts();
    }

    public void getMyPosts(){
        posts.clear();
        postDAL.getMyOlderPosts(new PostCallback() {
            @Override
            public void getMyPosts(List<Post> list, Task<QuerySnapshot> task) {
                super.getMyPosts(list, task);

                Collections.sort(posts);
                if(list.size()>0){
                    posts.addAll(list);
                    Log.d("TAG", "getMyPosts: " + posts.size());
                    layoutProgress.setVisibility(View.INVISIBLE);
                    layoutContent.setVisibility(View.VISIBLE);
                    postRCLAdapter.notifyDataSetChanged();

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

                            LinearLayoutManager tempLinear = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int firstVisibleItemPosition = tempLinear.findFirstVisibleItemPosition();
                            int visibleItemCount = tempLinear.getChildCount();
                            int totalItemCount = tempLinear.getItemCount();


                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;
                                long currentTimestamp = Timestamp.now().getSeconds();

                                Query query = postDAL.getFirestore().collection(CollectionHelper.USER_COLLECTION)
                                        .document(postDAL.getUserId())
                                        .collection(CollectionHelper.POST_COLLECTION)
                                        .whereLessThan(CollectionHelper.POST_TIMESTAMP, currentTimestamp)
                                        .orderBy(CollectionHelper.POST_TIMESTAMP)
                                        .limit(postDAL.getLimit());

                                postDAL.executeQuery(query, new PostCallback() {
                                    @Override
                                    public void onQueryExecuted(List<Post> list, Task<QuerySnapshot> task) {
                                        super.onQueryExecuted(list, task);
                                        Log.d("TAG", "onQueryExecuted: " + list.size());
                                        posts.addAll(list);

                                        postRCLAdapter.notifyDataSetChanged();

                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                        if (task.getResult().size() < postDAL.getLimit()) {
                                            isLastItemReached = true;
                                            Log.d("TAG", "isLastItemReached: " + isLastItemReached);

                                        }
                                    }
                                });
                            }
                        }


                    };
                    recyclerView.addOnScrollListener(onScrollListener);
                    postRCLAdapter.notifyDataSetChanged();

                }
                else{
                    Log.d("TAG", "getMyPosts: "+ posts.size());
                    layoutProgress.setVisibility(View.INVISIBLE);
                    layoutinfo.setVisibility(View.VISIBLE);
                }

                postRCLAdapter.notifyDataSetChanged();
            }
        });


    }


}
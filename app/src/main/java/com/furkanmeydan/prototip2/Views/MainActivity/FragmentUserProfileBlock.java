package com.furkanmeydan.prototip2.Views.MainActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.furkanmeydan.prototip2.DataLayer.BlockDAL;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.BlockCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.PostCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.ProfileCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.QuestionCallback;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.RequestCallback;
import com.furkanmeydan.prototip2.DataLayer.PostDAL;
import com.furkanmeydan.prototip2.DataLayer.ProfileDAL;
import com.furkanmeydan.prototip2.DataLayer.QuestionDAL;
import com.furkanmeydan.prototip2.DataLayer.RequestDAL;
import com.furkanmeydan.prototip2.Models.Question;
import com.furkanmeydan.prototip2.Models.Request;
import com.furkanmeydan.prototip2.Models.User;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.PostSearchResultDetailActivity.PostSearchResultDetailActivity;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentUserProfileBlock extends Fragment {
    FirebaseAuth firebaseAuth;
    RequestDAL requestDAL;
    PostDAL postDAL;
    ProfileDAL profileDAL;
    BlockDAL blockDAL;
    QuestionDAL questionDAL;
    Question question;
    Dialog dialog;
    MainActivity activity;
    Bundle bundle;
    private TextView txtNameSurname, txtGender, txtBirthday;
    private ImageView imageView;
    ConstraintLayout layoutDetail, layoutProgress;
    String userID, userIDSender;
    User userProfile;
    Button btnBlock;


    public FragmentUserProfileBlock() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        activity = (MainActivity) getActivity();
        if(getArguments() !=null){
            bundle = getArguments();
            question = (Question) bundle.getSerializable("question");
            userID = firebaseAuth.getCurrentUser().getUid();
            userIDSender = question.getSenderID();
        }

        blockDAL = new BlockDAL();
        questionDAL = new QuestionDAL();
        requestDAL = new RequestDAL();
        postDAL = new PostDAL();
        profileDAL = new ProfileDAL();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_search_post_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNameSurname = view.findViewById(R.id.postSearchPostOwnerName);
        txtGender = view.findViewById(R.id.postSearchPostOwnerGender);
        txtBirthday = view.findViewById(R.id.postSearchPostOwnerBD);
        imageView = view.findViewById(R.id.postSearchPostOwnerImageView);
        layoutDetail = view.findViewById(R.id.constraintLayout2);
        layoutProgress = view.findViewById(R.id.searchProfileConsLayout);
        btnBlock = view.findViewById(R.id.btnBlockUserProfile);
        getProfileData();

        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(activity);
                dialog.setContentView(R.layout.popup_blockuser_notif);
                dialog.show();
                Button btnBlockYes = dialog.findViewById(R.id.btnPopupBlockYes);
                Button btnBlockNo = dialog.findViewById(R.id.btnPopupBlockNo);
                final EditText edtBlockReason = dialog.findViewById(R.id.edtPopupBlockReason);


                btnBlockYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String userid = activity.userId;

                        final String blockReason = edtBlockReason.getText().toString();
                        requestDAL.getRequestStatusToBlock(userid, question.getSenderID(), new RequestCallback() {
                            @Override
                            public void onAcceptedRequestSearchResult(boolean flag) {
                                super.onAcceptedRequestSearchResult(flag);
                                if(!flag){

                                    requestDAL.getRequestStatusToBlock(question.getSenderID(), userid, new RequestCallback() {
                                        @Override
                                        public void onAcceptedRequestSearchResult(boolean flag) {
                                            super.onAcceptedRequestSearchResult(flag);
                                            if(!flag){
                                                if(blockDAL.checkReason(blockReason,activity)){
                                                    blockDAL.blockUser(userid, question.getSenderID(), blockReason, new BlockCallback() {
                                                        @Override
                                                        public void onUserBlocked() {
                                                            super.onUserBlocked();
                                                            blockDAL.blockUser(question.getSenderID(), userid, null, new BlockCallback() {
                                                                @Override
                                                                public void onUserBlocked() {
                                                                    super.onUserBlocked();
                                                                    requestDAL.deleteRequestsOnBlock(userid, question.getSenderID(), new RequestCallback() {
                                                                        @Override
                                                                        public void onRequestsDeletedOnBlock() {
                                                                            super.onRequestsDeletedOnBlock();
                                                                            requestDAL.deleteRequestsOnBlock(question.getSenderID(), userid, new RequestCallback() {
                                                                                @Override
                                                                                public void onRequestsDeletedOnBlock() {
                                                                                    super.onRequestsDeletedOnBlock();
                                                                                    postDAL.removeFromWishListForBlock(userid, question.getSenderID(), new PostCallback() {
                                                                                        @Override
                                                                                        public void deleteWishOnBlock() {
                                                                                            super.deleteWishOnBlock();
                                                                                            postDAL.removeFromWishListForBlock(question.getSenderID(), userid, new PostCallback() {
                                                                                                @Override
                                                                                                public void deleteWishOnBlock() {
                                                                                                    super.deleteWishOnBlock();
                                                                                                    questionDAL.removeQuestionforBlock(userid, question.getSenderID(), new QuestionCallback() {
                                                                                                        @Override
                                                                                                        public void onQuestionRemovedForBlock() {
                                                                                                            super.onQuestionRemovedForBlock();
                                                                                                            questionDAL.removeQuestionforBlock(question.getSenderID(), userid, new QuestionCallback() {
                                                                                                                @Override
                                                                                                                public void onQuestionRemovedForBlock() {
                                                                                                                    super.onQuestionRemovedForBlock();
                                                                                                                    dialog.dismiss();
                                                                                                                    Intent i = new Intent(activity,MainActivity.class);
                                                                                                                    startActivity(i);
                                                                                                                    activity.finish();
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    });

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });

                                                                                }
                                                                            });
                                                                        }
                                                                    });

                                                                }
                                                            });
                                                        }
                                                    });
                                                }

                                            }else{
                                                Toast.makeText(activity,"Bu kullanıcadan size gelen onaylı bir istek bulunmaktadır",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(activity,"Bu kullanıcya yolladığınız onaylı bir isteğiniz bulunmaktadır",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });
                btnBlockNo.setOnClickListener(view1 -> dialog.dismiss());
            }
        });




    }

    public void getProfileData(){
        Log.d("Tag","PostSearchactivity getProfiledata içerisi");

        profileDAL.getProfile(userIDSender, new ProfileCallback() {
            @Override
            public void getUser(User user) {
                super.getUser(user);
                Log.d("Tag","PostSearchactivity getUser içerisi");
                userProfile = user;
                txtNameSurname.setText(userProfile.getNameSurname());
                txtBirthday.setText(userProfile.getBirthDate());
                txtGender.setText(userProfile.getGender());
                Glide.with(activity.getApplicationContext()).load(userProfile.getProfilePicture()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        layoutProgress.setVisibility(View.GONE);
                        layoutDetail.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);



            }
        });
    }
}
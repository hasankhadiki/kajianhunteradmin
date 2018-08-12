package tehhutan.app.kajianhunteradmin.Menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import tehhutan.app.kajianhunteradmin.PostDanKomentar;
import tehhutan.app.kajianhunteradmin.R;
import tehhutan.app.kajianhunteradmin.model.Comment;
import tehhutan.app.kajianhunteradmin.model.Post;
import tehhutan.app.kajianhunteradmin.model.User;
import tehhutan.app.kajianhunteradmin.utils.Constants;
import tehhutan.app.kajianhunteradmin.utils.FirebaseUtils;

public class Timeline extends Fragment {
    private static final String BUNDLE_COMMENT = "comment";
    private Post mPost;
    private EditText komentar, isiPost, judulPost;
    private Comment mComment;
    private Button submitPost;
    private FirebaseRecyclerAdapter<Post, PostHolder> mPostAdapter;
    private RecyclerView daftarPost;
    private ProgressDialog mProgressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Timeline");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_timeline, null);
        /*FloatingActionButton fab = (FloatingActionButton) mRootVIew.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCreateDialog dialog = new PostCreateDialog();
                dialog.show(getFragmentManager(), null);
            }
        }); */
        initDaftarPost(v);
        FloatingActionButton fab = (FloatingActionButton)v.findViewById(R.id.fab_timeline);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View vv  = getLayoutInflater().inflate(R.layout.add_post_timeline, null);
                judulPost = (EditText)  vv.findViewById(R.id.et_judulpost_timeline);
                isiPost = (EditText)  vv.findViewById(R.id.et_isipost_timeline);
                submitPost = (Button)  vv.findViewById(R.id.btn_submit_post);

                mBuilder.setView( vv);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                // set listeners
                judulPost.addTextChangedListener(mTextWatcher);
                isiPost.addTextChangedListener(mTextWatcher);

                // run once to disable if empty
                checkFieldsForEmptyValues();

                submitPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                        if (activeNetwork != null) { // connected to the internet
                            FirebaseUtils.getUserRef(FirebaseUtils.getUserID(getContext()).replace(".", ","))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            final String postId = FirebaseUtils.getUid();
                                            //TextView postDialogTextView = (TextView) mRootView.findViewById(R.id.post_dialog_edittext);
                                            //String text = postDialogTextView.getText().toString();
                                            mProgressDialog = new ProgressDialog(getActivity());
                                            mProgressDialog.setMessage("Mengirim post...");
                                            mProgressDialog.setCancelable(false);
                                            mProgressDialog.setIndeterminate(true);
                                            mProgressDialog.show();
                                            Post newPost = new Post();
                                            newPost.setPostId(postId);
                                            newPost.setJumlahComments(0);
                                            newPost.setJumlahLikes(0);
                                            newPost.setUser(user);
                                            newPost.setPostText(isiPost.getText().toString());
                                            newPost.setPostTitle(judulPost.getText().toString());
                                            kirimPost(postId, newPost, dialog);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            mProgressDialog.dismiss();
                                        }
                                    });

                        } else {
                            Toast.makeText(getActivity(), "Internet Connection Not Available, Please Check Your Connection Setting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });
        return v;
    }

    private void kirimPost(String postId, Post mPost, final AlertDialog dialog){
        FirebaseUtils.getPostRef().child(postId)
                .setValue(mPost);
        FirebaseUtils.getMyPostRef(getContext()).child(postId).setValue(true)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        dialog.dismiss();
                    }
                });

        FirebaseUtils.addToMyRecord(Constants.POST_KEY, postId, getContext());
    }

    private void initDaftarPost(View v) {
        daftarPost = (RecyclerView) v.findViewById(R.id.daftarPost);
        daftarPost.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter();
        daftarPost.setAdapter(mPostAdapter);
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(
                Post.class,
                R.layout.timeline_item,
                PostHolder.class,
                FirebaseUtils.getPostRef()
        ) {
            @Override
            protected void populateViewHolder(final PostHolder viewHolder, final Post model, int position) {
                viewHolder.setNumComments(String.valueOf(model.getJumlahComments()));
                viewHolder.setNumLikes(String.valueOf(model.getJumlahLikes()));
                viewHolder.setUsername(model.getUser().getNama());
                viewHolder.setPostText(model.getPostText());
                viewHolder.setPostTitle(model.getPostTitle());
                // cekLike(viewHolder.ic_like, model.getPostId());
                if(model.getUser().getPhoto()!=null){
                    Glide.with(getActivity())
                            .load(model.getUser().getPhoto())
                            .into(viewHolder.photoProfilAdminPost);
                }
                viewHolder.postLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeClick(model.getPostId(), viewHolder.ic_like);
                    }
                });

                viewHolder.postCommentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PostDanKomentar.class);
                        intent.putExtra(Constants.EXTRA_POST, model);
                        startActivity(intent);
                    }
                });
                viewHolder.balasTombol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PostDanKomentar.class);
                        intent.putExtra(Constants.EXTRA_POST, model);
                        startActivity(intent);
                    }
                });
            }
        };
    }
    private void cekLike(final ImageView iv, String postId){
        FirebaseUtils.getPostLikedRef(postId, getActivity()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    iv.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    iv.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.tab_indicator_text), android.graphics.PorterDuff.Mode.SRC_IN);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void onLikeClick(final String postId, final ImageView iv) {
        FirebaseUtils.getPostLikedRef(postId, getContext())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //User liked
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            if(num>0){
                                                mutableData.setValue(num - 1);}
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId, getContext())
                                                    .setValue(null);
                                            iv.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.tab_indicator_text), android.graphics.PorterDuff.Mode.SRC_IN);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId, getContext())
                                                    .setValue(true);
                                            iv.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setTextSize(18);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);


        textView.setTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(textView);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues(){

        String s1 = judulPost.getText().toString();
        String s2 = isiPost.getText().toString();

        if(s1.equals("")
                || s2.equals("")
                ){
            submitPost.setEnabled(false);
        } else {
            submitPost.setEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, mComment);
        super.onSaveInstanceState(outState);
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        ImageView photoProfilAdminPost;
        TextView namaProfilAdmin;
        LinearLayout postLikeLayout;
        LinearLayout postCommentLayout;
        RelativeLayout balasTombol;
        TextView jumlahLikes;
        TextView jumlahKomentar;
        TextView isiPost, judulPost;
        ImageView ic_like;


        public PostHolder(View v) {
            super(v);
            photoProfilAdminPost = (ImageView)v. findViewById(R.id.gambar_admin_timeline);
            namaProfilAdmin = (TextView) v.findViewById(R.id.nama_admin_timeline);
            postLikeLayout = (LinearLayout) v.findViewById(R.id.like_layout);
            postCommentLayout = (LinearLayout) v.findViewById(R.id.comment_layout);
            jumlahLikes = (TextView) v.findViewById(R.id.tv_likes);
            jumlahKomentar = (TextView) v.findViewById(R.id.tv_comments);
            balasTombol = (RelativeLayout) v.findViewById(R.id.balas_tombol);
            isiPost = (TextView)v.findViewById(R.id.deskripsi_pengumuman_timeline);
            judulPost = (TextView)v.findViewById(R.id.judul_kajian_timeline);
            ic_like = (ImageView) v.findViewById(R.id.iv_like);
        }



        public void setUsername(String username) {
            namaProfilAdmin.setText(username);
        }

        public void setNumLikes(String numLikes) {
            jumlahLikes.setText(numLikes);
        }

        public void setNumComments(String numComments) {
            jumlahKomentar.setText(numComments);
        }

        public void setPostText(String text) {
            isiPost.setText(text);
        }

        public void setPostTitle(String judul){
            judulPost.setText(judul);
        }

    }
}

package tehhutan.app.kajianhunteradmin.model;

import java.io.Serializable;

public class Post implements Serializable {
    private User user;
    private String postText;
    private String postId;
    private String postTitle;
    private long jumlahLikes;
    private long jumlahComments;

    public Post() {
    }

    public Post(User user, String postText, String postId, long jumlahLikes, long jumlahComments, String posttitle) {

        this.user = user;
        this.postText = postText;
        this.postId = postId;
        this.jumlahLikes = jumlahLikes;
        this.jumlahComments = jumlahComments;
        this.postTitle = posttitle;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getJumlahLikes() {
        return jumlahLikes;
    }

    public void setJumlahLikes(long jumlahLikes) {
        this.jumlahLikes = jumlahLikes;
    }

    public long getJumlahComments() {
        return jumlahComments;
    }

    public void setJumlahComments(long jumlahComments) {
        this.jumlahComments = jumlahComments;
    }

}

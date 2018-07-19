package tehhutan.app.kajianhunteradmin.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private User user;
    private String commentId;
    private String comment;

    public Comment() {
    }

    public Comment(User user, String commentId, String comment) {

        this.user = user;
        this.commentId = commentId;
        this.comment = comment;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

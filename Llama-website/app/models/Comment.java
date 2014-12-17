package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by bonini_j on 29/10/2014.
 */
@Entity
@Table(name = "comment")
public class Comment extends Model {
    @Id
    public Long id;
    public Long userId;
    public String userLogin;
    public String text;
    public float rating;
    public Date date;

    @ManyToOne
    public Product product;

    public static Model.Finder<Long, Comment> find = new Model.Finder<>(Long.class, Comment.class);

    public static List<Comment> findForProduct(Long productId) {
        return find.where().eq("productId", productId).orderBy("date ASC").findList();
    }
}

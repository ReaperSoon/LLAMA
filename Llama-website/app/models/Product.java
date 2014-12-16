package models;

import com.google.common.collect.Lists;
import org.h2.expression.ExpressionList;
import play.data.Form;
import play.db.ebean.Model;
import utils.Session;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by bonini_j on 27/10/2014.
 */
@Entity
@Table(name = "product")
public class Product extends Model {

    @Id
    public Long id;
    public String name;
    public String description;
    public float price;
    public int quantity;

    @OneToMany
    public List<Comment> comments;

    public Product() {}

    public Product(Form<Product> filledForm) {
        this.id = (filledForm.get().id == null) ? -1 : filledForm.get().id;
        this.name = filledForm.get().name;
        this.description = filledForm.get().description;
        this.price = filledForm.get().price;
        this.quantity = filledForm.get().quantity;
    }

    public static Model.Finder<Long, Product> find = new Model.Finder<>(Long.class, Product.class);

    public static void save(Product product) {
        product.save();
    }

    public static List<Product> findWithFilter(String type, String filter) {
        com.avaje.ebean.ExpressionList<Product> expr = find.fetch("comments").where();
        List<Product> products;
        final String orderby = (Session.orderBy().compareTo("name")==0)?Session.orderBy()+" ASC" : Session.orderBy()+" DESC";
        if (type.isEmpty())
            return expr.orderBy(orderby).findList();
        if (type.compareTo("name") == 0)
            expr.ilike("name", "%"+filter+"%");
        else if (type.compareTo("description") == 0)
            expr.ilike("description", "%"+filter+"%");
        products = expr.orderBy(orderby).findList();
        return products;
    }

    public float rating() {
        float rating = 0;int i = 0;
        for (Comment comment : comments) {
            rating+= comment.rating;
            i++;
        }
        return Math.round((rating/i)*10)/10;
    }
}

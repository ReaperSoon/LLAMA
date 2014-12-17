package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by bonini_j on 28/10/2014.
 */
@Entity
@Table(name = "bill_line")
public class BillLine extends Model {
    @Id
    public Long id;

    public Long productId;
    public String productName;
    public int quantity;
    public float productPrice;

    public Long billId;

    public static Model.Finder<Long, BillLine> find = new Model.Finder<>(Long.class, BillLine.class);

    public float total(){
        float total = productPrice*quantity;
        return Math.round(total*100)/100;
    }
}

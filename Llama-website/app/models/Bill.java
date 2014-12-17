package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by bonini_j on 28/10/2014.
 */
@Entity
@Table(name = "bill")
public class Bill extends Model {
    @Id
    public Long id;

    @ManyToMany
    public List<BillLine> billLines;

    public Long userId;
    public String userLogin;
    public String shipmentAddress;
    public String buyerName;
    public Date date;
    public float total;

    public static Model.Finder<Long, Bill> find = new Model.Finder<>(Long.class, Bill.class);

    public void addAndSaveLines(List<BillLine> lines) {
        float total = 0;
        for (BillLine line : lines) {
            line.billId = this.id;
            this.billLines.add(line);
            total += line.productPrice*line.quantity;
            line.save();
        }
        this.total = Math.round(total*100)/100;
    }

    public static List<Bill> findForUser(Long userId) {
        return find.fetch("billLines").where().eq("userId", userId).orderBy("date DESC").findList();
    }

    public float total() {
        float total = 0;
        for (BillLine bill : billLines)
            total += bill.total();
        return total;
    }
}

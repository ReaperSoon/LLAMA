package controllers;

import com.google.common.base.Strings;
import models.Bill;
import models.BillLine;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Session;

import java.util.Date;
import java.util.List;

/**
 * Created by bonini_j on 28/10/2014.
 */
public class Orders extends Controller {

    public static Result orders(){
        if (!Session.isLogged()) {
            flash().put("error", "You must be logged in");
            return redirect(controllers.routes.Application.index());
        }
        List<Bill> bills = Bill.findForUser(Session.userId());
        return ok(views.html.orders.orders.render(bills));
    }

    public static Result ordersAdmin() {
        Result allow = Application.allow();
        if (allow != null)
            return allow;
        List<Bill> bills = Bill.find.all();
        return ok(views.html.admin.ordersAdmin.render(bills));
    }

    public static Result createBill() {
        if (!Session.isLogged()) {
            flash().put("error", "You must be logged in");
            return redirect(controllers.routes.Application.index());
        }
        Form<Bill> filledForm = Form.form(Bill.class).bindFromRequest();
        List<BillLine> lines = ShoppingCart.generateBillLine();
        try {
            Bill bill = new Bill();
            bill.userId = Session.userId();
            bill.userLogin = Session.userLogin();
            bill.buyerName = Strings.isNullOrEmpty(filledForm.get().buyerName) ? "" : filledForm.get().buyerName;
            bill.shipmentAddress = filledForm.get().shipmentAddress;
            bill.date = new Date();
            bill.addAndSaveLines(lines);
            bill.save();
            Session.flushCart();
        } catch (Throwable e) {
            flash().put("error", "Couldn't finish the order");
            return redirect(controllers.admin.routes.Products.products());
        }
        return redirect(controllers.routes.Orders.orders());
    }

}

package controllers;

import com.google.common.collect.Lists;
import models.BillLine;
import models.Product;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Session;

import java.util.List;

/**
 * Created by bonini_j on 28/10/2014.
 */
public class ShoppingCart extends Controller {

    public static class CartItem {
        public int key;

        public Long id;
        public String name;
        public int quantity;
        public float price;

        public float price(){
            return Math.round(price*100)/100;
        }
    }

    public static List<CartItem> items() {
        int maxKey = Session.getEntryKey();
        List<CartItem> items = Lists.newArrayList();
        CartItem item;
        for (int i = 1; i <= maxKey; i++) {
            if ((item = Session.getFromCart(i)) != null)
                items.add(item);
        }
        return items;
    }

    public static List<BillLine> generateBillLine() {
        int maxKey = Session.getEntryKey();
        List<BillLine> items = Lists.newArrayList();
        CartItem item;
        for (int i = 1; i <= maxKey; i++) {
            if ((item = Session.getFromCart(i)) != null) {
                Product product = Product.find.byId(item.id);
                if (product != null) {
                    BillLine line = new BillLine();
                    line.productId = product.id;
                    line.productName = product.name;
                    line.productPrice = product.price;
                    line.quantity = item.quantity;
                    items.add(line);
                }

            }
        }
        return items;
    }

    public static Result addToCart(final Long id, final int quantity) {
        Product product = Product.find.byId(id);
        if (product == null){
            flash().put("error", "Unable to add product to cart");
            return controllers.admin.Products.products();
        }
        String key = Session.addToCart(product, quantity);
        return ok(views.html.templates.cartRow.render(Session.getFromCart(Integer.valueOf(key))));
    }

    public static Result removeFromCart(final int key) {
        Session.removeFromCart(key);
        return ok();
    }

    public static Result order() {
        return ok(views.html.order.render());
    }
}

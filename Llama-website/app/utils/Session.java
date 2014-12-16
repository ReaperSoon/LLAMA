package utils;

import com.google.common.base.Strings;
import controllers.ShoppingCart;
import models.Product;
import models.User;
import play.mvc.Http;

/**
 * Created by bonini_j on 26/10/2014.
 */
public final class Session {

    public static final String USER_ID_KEY = "user";
    public static final String USER_MAIL_KEY = "user_mail";
    public static final String USER_ADMIN_KEY = "user_admin";
    public static final String USER_LOGIN_KEY = "user_login";
    public static final String CART_ENTRY_KEY = "cart_entry";
    public static final String FILTER_TYPE = "filter_type";
    public static final String FILTER_STRING = "filter_string";
    public static final String ORDER_BY = "order_by";

    private Session() {
    }

    public static String addToCart(Product product, int quantity) {
        final String entry = String.valueOf(product.id)+","+String.valueOf(quantity);
        final String key = String.valueOf(Session.generateCartEntryKey());
        session().put(key, entry);
        return key;
    }

    public static void removeFromCart(int key) {
        session().remove(String.valueOf(key));
    }

    public static ShoppingCart.CartItem getFromCart(int key) {
        String entry;
        if ((entry = session().get(String.valueOf(key))) == null || entry.isEmpty())
            return null;
        ShoppingCart.CartItem item = new ShoppingCart.CartItem();
        Product product = Product.find.byId(Long.valueOf(entry.split(",")[0]));
        if (product == null)
            return null;
        item.quantity = Integer.valueOf(entry.split(",")[1]);
        item.key = key;
        item.price = item.quantity*product.price;
        item.name = product.name;
        item.id = product.id;
        return item;
    }

    public static int generateCartEntryKey() {
        int key = getEntryKey() + 1;
        System.out.print("Add Key["+key+"]");
        session().put(CART_ENTRY_KEY, String.valueOf(key));
        return key;
    }

    public static int getEntryKey() {
        return (!session().containsKey(CART_ENTRY_KEY)) ? 0 : Integer.valueOf(session().get(CART_ENTRY_KEY));
    }

    public static void store(User user) {
        session().put(USER_ID_KEY, String.valueOf(user.id()));
        session().put(USER_MAIL_KEY, user.mail());
        session().put(USER_LOGIN_KEY, user.login());
        if (user.admin() > 0)
            session().put(USER_ADMIN_KEY, "true");
    }

    public static Long userId() {
        final String userIdAsString = session().get(USER_ID_KEY);
        if(Strings.isNullOrEmpty(userIdAsString)) {
            return null;
        }

        try {
            return Long.valueOf(userIdAsString);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String userMail() {
        final String userIdAsString = session().get(USER_MAIL_KEY);
        if(Strings.isNullOrEmpty(userIdAsString)) {
            return null;
        }

        try {
            return userIdAsString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String userLogin() {
        final String userIdAsString = session().get(USER_LOGIN_KEY);
        if(Strings.isNullOrEmpty(userIdAsString)) {
            return null;
        }

        try {
            return userIdAsString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean isLogged(){
        try {
            final String userIdAsString = session().get(USER_ID_KEY);
            return !Strings.isNullOrEmpty(userIdAsString);
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isAdmin(){
        final String userIdAsString = session().get(USER_ADMIN_KEY);
        if(Strings.isNullOrEmpty(userIdAsString)) {
            return false;
        }

        try {
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void setFilterType(final String type) {
        session().put(FILTER_TYPE, type);
    }

    public static void setFilterString(final String string) {
        session().put(FILTER_STRING, string);
    }

    public static void setOrderBy(final String order) {
        session().put(ORDER_BY, order);
    }

    public static String getFilterType(){
        String type = session().get(FILTER_TYPE);
        return (Strings.isNullOrEmpty(type))? "" : type;
    }

    public static String getFilterString(){
        String string = session().get(FILTER_STRING);
        return (Strings.isNullOrEmpty(string))? "" : string;
    }

    public static String orderBy(){
        String order = session().get(ORDER_BY);
        if (Strings.isNullOrEmpty(order)){
            order = "name";
            setOrderBy(order);
        }
        return order;
    }

    public static void clear() {
        session().remove(USER_ADMIN_KEY);
        session().remove(USER_MAIL_KEY);
        session().remove(USER_ID_KEY);
    }

    public static void flushCart(){
        User user = User.find.byId(Session.userId());
        session().clear();
        Session.store(user);
    }

    public static void flush(){
        session().clear();
    }

    private static Http.Session session() {
        return Http.Context.current().session();
    }
}

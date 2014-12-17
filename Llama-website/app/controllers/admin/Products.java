package controllers.admin;

import controllers.Application;
import models.Comment;
import models.Product;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Session;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by bonini_j on 27/10/2014.
 */
public class Products extends Controller {

    public static final String PICTURE_FOLDER = "public/images/llamas/";

    // USER FUNCS
    public static Result products() {
        List<Product> products = Product.findWithFilter(Session.getFilterType(), Session.getFilterString());
        return ok(views.html.products.render(products));
    }

    // ADMIN FUNCS
    public static Result productsAdmin() {
        Result res = Application.allow();
        if (res != null)
            return res;
        List<Product> products = Product.find.all();
        return ok(views.html.admin.productsAdmin.render(products));
    }

    public static Result productFilter(String type, String filter) {
        if (type.compareTo("nofilter") == 0)
            type = "";
        Session.setFilterString(filter);
        Session.setFilterType(type);
        List<Product> products = Product.findWithFilter(Session.getFilterType(), Session.getFilterString());
        return ok(views.html.templates.productList.render(products));
    }

    public static Result resetFilter(){
        Session.setFilterString("");
        Session.setFilterType("");
        return redirect(controllers.admin.routes.Products.products());
    }

    public static Result orderBy(final String order) {
        Session.setOrderBy(order);
        List<Product> products = Product.findWithFilter(Session.getFilterType(), Session.getFilterString());
        return ok(views.html.templates.productList.render(products));
    }

    public static Result comments(Long productId) {
        Product product = Product.find.byId(productId);
        if (product == null) {
            flash().put("error", "Unable to add comment");
            return redirect(controllers.routes.Application.index());
        }
        return ok(views.html.comments.render(product));
    }

    public static Result addComment() {
        DynamicForm filledForm = Form.form().bindFromRequest();

        try {
            Long productId = Long.valueOf(filledForm.get("productId"));
            Product product = Product.find.byId(productId);
            if (product == null)
                return null;
            User user = User.find.byId(Session.userId());
            if (user == null)
                return null;
            Comment comment = new Comment();
            comment.date = new Date();
            comment.product = product;
            comment.rating = Float.parseFloat(filledForm.get("rating"));
            comment.text = filledForm.get("text");
            comment.userId = user.id;
            comment.userLogin = user.login;
            comment.save();
            product.comments.add(comment);
            product.save();
            return ok(views.html.templates.commentRow.render(comment));
        } catch(Throwable ex) {
            return badRequest();
        }
    }

    public static Result productPicture(Long id) {
        Product product = Product.find.byId(id);
        if (product == null)
            return badRequest();
        return ok(views.html.templates.bigPicture.render(product));
    }

    public static Result productForm() {
        Result res = Application.allow();
        if (res != null)
            return res;
        return ok(views.html.admin.productForm.render(Form.form(Product.class).fill(new Product())));
    }

    public static Result createProduct() {
        Result res = Application.allow();
        if (res != null)
            return res;
        Form<Product> filledForm = Form.form(Product.class).bindFromRequest();
        Product product = new Product(filledForm);
        product.id = null;
        product.save();
        setFile(product.id);
        return redirect(controllers.admin.routes.Products.productsAdmin());
    }

    public static Result editProduct(Long id) {
        Result res = Application.allow();
        if (res != null)
            return res;
        Product product;
        if ((product = Product.find.byId(id)) != null) {
            return ok(views.html.admin.productForm.render(Form.form(Product.class).fill(product)));
        }

        return ok();
    }

    public static Result doEditProduct() {
        Result res = Application.allow();
        if (res != null)
            return res;
        Form<Product> filledForm = Form.form(Product.class).bindFromRequest();
        Product product = new Product(filledForm);
        product.update();
        setFile(product.id);
        flash().put("msg", "Product updated");
        return redirect(controllers.admin.routes.Products.productsAdmin());
    }

    public static Result deleteProduct(Long id) {
        Result res = Application.allow();
        if (res != null)
            return res;
        Product product = Product.find.byId(id);
        if (product != null) {
            product.delete();
        }
        else {
            flash().put("error", "Unable to delete the product");
        }
        return redirect(controllers.admin.routes.Products.productsAdmin());
    }

    public static boolean havePicture(Long id) {
        return new File(PICTURE_FOLDER+"llama-"+String.valueOf(id)+".jpg").exists();
    }

    public static void setFile(Long id) {
        Http.MultipartFormData.FilePart pict = request().body().asMultipartFormData().getFile("picture");
        if (pict != null) {
            File file = new File(PICTURE_FOLDER+"llama-"+String.valueOf(id)+".jpg");
            if (file.exists())
                file.delete();
            file = pict.getFile();
            file.renameTo(new File(PICTURE_FOLDER, "llama-"+String.valueOf(id)+".jpg"));
        }
    }
}

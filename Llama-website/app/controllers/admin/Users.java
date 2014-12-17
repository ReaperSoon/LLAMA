package controllers.admin;

import controllers.Application;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Session;

import java.util.List;

/**
 * Created by bonini_j on 27/10/2014.
 */
public class Users extends Controller {
    public static Result users() {
        Result res = Application.allow();
        if (res != null)
            return res;

        List<User> users = User.find.all();
        return ok(views.html.admin.users.render(users));
    }

    public static Result editUser(Long id) {
        Result res = Application.allow();
        if (res != null)
            return res;
        User user;
        if ((user = User.find.byId(id)) != null) {
            return ok(views.html.admin.editUser.render(Form.form(User.UserForm.class).fill(new User.UserForm(user))));
        }

        return ok();
    }

    public static Result doEditUser() {
        Result res = Application.allow();
        if (res != null)
            return res;
        User.update(Form.form(User.UserForm.class).bindFromRequest());
        return users();
    }

    public static Result doCreateAccount() {
        Form<User.UserForm> filledForm = Form.form(User.UserForm.class).bindFromRequest();

        if (User.exists(filledForm.get().login))
            flash().put("error", "Login already exists");
        else {
            User user;
            user = User.create(filledForm);

            Session.store(user);
        }
        return users();
    }

    public static Result deleteUser(Long id){
        Result res = Application.allow();
        if (res != null)
            return res;
        User user = User.find.byId(id);
        if (user != null) {
            user.delete();
        }
        else {
            flash().put("error", "Unable to delete the user");
        }

        return users();
    }
}

package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Hash;
import utils.Session;

/**
 * Created by bonini_j on 27/10/2014.
 */
public class Login extends Controller {

    public static Result createAccount() {
        return ok(views.html.login.createAccount.render(Form.form(User.UserForm.class).fill(new User.UserForm())));
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
        return redirect(filledForm.get().url);
    }

    public static Result editAccount() {
        User user = User.find.byId(Session.userId());
        return ok(views.html.login.editAccount.render(Form.form(User.UserForm.class).fill(new User.UserForm(user))));
    }

    public static Result doEditAccount() {
        Form<User.UserForm> filledForm = Form.form(User.UserForm.class).bindFromRequest();
        User user = User.find.byId(Session.userId());
        user.login = filledForm.get().login;
        user.mail = filledForm.get().mail;
        if (!Strings.isNullOrEmpty(filledForm.get().password))
            user.password = Hash.sha1(filledForm.get().password);
        user.update();
        return ok();
    }

    public static Result showLogin() {
        return ok(views.html.login.login.render());
    }

    public static Result login() {
        Form<User.UserForm> filledForm = Form.form(User.UserForm.class).bindFromRequest();
        User user;
        if ((user = User.authenticate(filledForm.get().login, filledForm.get().password)) != null) {
            Session.store(user);
        }
        else
            flash().put("error", "Authentication failed");
        return redirect(filledForm.get().url);
    }

    public static Result disconnect() {
        Session.clear();
        return redirect(controllers.routes.Application.index());
    }

}

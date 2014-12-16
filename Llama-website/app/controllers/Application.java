package controllers;

import play.*;
import play.mvc.*;

import utils.Session;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return redirect(controllers.admin.routes.Products.products());
    }

    public static Result allow(){
        if (Session.isAdmin())
            return null;
        flash().put("error", "You can't access this page");
        return redirect(controllers.admin.routes.Products.products());
    }

    public static Result flushSession() {
        Session.flush();
        return redirect(controllers.routes.Application.index());
    }
}

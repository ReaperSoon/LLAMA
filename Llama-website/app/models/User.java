package models;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import com.google.common.collect.Lists;
import play.data.Form;
import play.db.ebean.Model;
import security.SecurityRole;
import utils.Hash;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by bonini_j on 26/10/2014.
 */
@Entity
@Table(name = "user")
public class User extends Model implements Subject {
    @Id
    public Long id;
    public String mail;
    public String login;
    public String password;
    public int isAdmin;

    private List<SecurityRole> roles;

    public static class UserForm {
        public Long id;
        public String mail;
        public String login;
        public String password;

        public String url;

        public UserForm() {

        }

        public UserForm (User user) {
            this.id = user.id;
            this.mail = user.mail;
            this.login = user.login;
            this.password = user.password;
        }
    }

    public static Model.Finder<Long, User> find = new Model.Finder<>(Long.class, User.class);

    public static User authenticate(final String login, final String password) {
        return User.find.where()
                .eq("login", login)
                .eq("password", Hash.sha1(password))
                .findUnique();
    }

    public static User create(Form<UserForm> filledForm) {
        User user = new User();
        user.password(Hash.sha1(filledForm.get().password));
        user.login(filledForm.get().login);
        user.mail(filledForm.get().mail);

        user.save();
        return user;
    }

    public static User update(Form<UserForm> filledForm) {
        User user = User.find.byId(filledForm.get().id);
        user.mail = filledForm.get().mail;
        user.login = filledForm.get().login;
        if (!filledForm.get().password.isEmpty())
            user.password = Hash.sha1(filledForm.get().mail);

        user.update();
        return user;
    }

    public static boolean exists(final String login) {
        return (User.find.where().eq("login", login).findUnique()) != null;
    }

    public Long id() {
        return id;
    }

    public String mail() {
        return mail;
    }

    public void mail(String mail) {
        this.mail = mail;
    }

    public String login() {
        return login;
    }

    public void login(String login) {
        this.login = login;
    }

    public String password() {
        return password;
    }

    public void password(String password) {
        this.password = password;
    }

    public int admin() {
        return isAdmin;
    }

    public void admin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public List<? extends Role> getRoles() {
        List<SecurityRole> role = Lists.newArrayList();
        if (this.isAdmin==1)
            role.add(new SecurityRole("admin"));
        return role;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

}

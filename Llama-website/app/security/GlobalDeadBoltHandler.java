package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import play.libs.F;
import play.mvc.Http;

import play.mvc.SimpleResult;


/**
 * User: jbonini
 * Date: 16/07/13
 */
public class GlobalDeadBoltHandler extends AbstractDeadboltHandler {


    @Override
    public F.Promise<SimpleResult> beforeAuthCheck(Http.Context context) {
        return null;
    }
}

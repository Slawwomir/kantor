package pl.slawomir.kantor;

/**
 * Created by Sławomir on 06.09.2017.
 */
import io.ddavison.conductor.Browser;
import io.ddavison.conductor.Config;
import io.ddavison.conductor.Locomotive;
import org.junit.Test;

@Config(
        browser = Browser.CHROME,
        url     = LoginAction.panelURL
)

public class KantorTest extends Locomotive {
    @Test
    public void testLoginButtonExists(){
        validatePresent(LoginAction.LOC_BTN_LOGIN);
    }

    @Test
    public void testTabsExist() {
       /* validatePresent(LoginAction.LOC_BTN_LOGIN)
                .validatePresent(LoginAction.LOC_LNK_DOWNLOADTAB)
                .validatePresent(LoginAction.LOC_LNK_DOCUMENTATIONTAB)
                .validatePresent(LoginAction.LOC_LNK_SUPPORTTAB)
                .validatePresent(LoginAction.LOC_LNK_ABOUTTAB)
        ;*/
    }
}

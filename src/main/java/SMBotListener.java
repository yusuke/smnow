/**
 * Created by yusuke on 12/9/14.
 */

import smnow.Main;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class SMBotListener implements ServletContextListener {

    Main main = new Main();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Thread thread = new Thread(main);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        main.alive = false;
    }

}

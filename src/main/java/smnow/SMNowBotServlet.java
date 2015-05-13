package smnow;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yusuke on 12/9/14.
 */
public class SMNowBotServlet extends HttpServlet {
    Thread thread;
    Main main;
    @Override
    public void init(ServletConfig config){
        thread = new Thread(new Main());
        thread.setDaemon(true);
        thread.start();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    public void destroy(){
        main.alive = false;
        thread.interrupt();
    }
}

package com.gonali.task.utils;

import com.gonali.task.config.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by TianyuanPan on 7/6/16.
 */
public class SessionUtils {

    private static final String username = "Administrator";
    private static String password;

    static {

        password = Config.getTaskConfig().getAdminPassword();
    }

    public static boolean isValidSession(HttpServletRequest request) {

        String username;
        String password;
        long sessionTime;
        try {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
            password = (String) session.getAttribute("password");
            sessionTime = (long) session.getAttribute("sessionTime");
        } catch (Exception e) {
            username = null;
            password = null;
            sessionTime = 0;
            //e.printStackTrace();
        }

        if (username == null || password == null)
            return false;

        if (System.currentTimeMillis() - sessionTime > 15*60*1000)
            return false;

        return (username.equals(SessionUtils.username) &&
                password.equals(SessionUtils.password));

    }

    public static void setSession(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("sessionTime", System.currentTimeMillis());
    }

    public static void updateSessionTime(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("sessionTime", System.currentTimeMillis());
    }

    public static void removeSession(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.removeAttribute("username");
        session.removeAttribute("password");
        session.removeAttribute("sessionTime");
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}

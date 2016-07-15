package com.gonali.task.dao.application.filter;

import com.gonali.task.dao.utils.SessionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by TianyuanPan on 7/6/16.
 */
public class InterceptorFilter implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //System.out.println(">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
        //return true;// 只有返回true才会继续向下执行，返回false取消当前请求

        if (SessionUtils.isValidSession(request)){
            SessionUtils.updateSessionTime(request);
            return true;
        }

        System.out.println("request uri: " + request.getRequestURI());

        if (request.getRequestURI().equals("/"))
            return true;

        if(!SessionUtils.isValidSession(request) && request.getRequestURI().equals("/api/unloginproccess"))
            return true;

        if (!SessionUtils.isValidSession(request) && !request.getRequestURI().equals("/api/login"))
            response.sendRedirect("/api/unloginproccess");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        //System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");

    }
}

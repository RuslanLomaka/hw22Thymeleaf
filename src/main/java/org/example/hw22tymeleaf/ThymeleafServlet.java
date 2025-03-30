package org.example.hw22tymeleaf;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/thymeleaf")
public class ThymeleafServlet extends HttpServlet {

@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write("Hello Ruslan!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

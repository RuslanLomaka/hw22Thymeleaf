package org.example.hw22tymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(getServletContext());
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);

        engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1. Отримуємо часовий пояс з параметра або з cookie
        String zoneIdStr = Optional.ofNullable(req.getParameter("zone"))
                .orElseGet(() -> getZoneFromCookie(req));
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(zoneIdStr);
        } catch (Exception e) {
            zoneId = ZoneId.of("UTC");
        }

        // 2. Обчислюємо час
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 3. Зберігаємо час у cookie
        Cookie cookie = new Cookie("zone", zoneId.getId());
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 днів
        resp.addCookie(cookie);

        // 4. Створюємо контекст
        Context context = new Context(req.getLocale());
        context.setVariable("zone", zoneId.getId());
        context.setVariable("localTime", formattedTime);

        // 5. Виводимо шаблон
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        engine.process("time", context, resp.getWriter());
    }

    private String getZoneFromCookie(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (c.getName().equals("zone")) {
                    return c.getValue();
                }
            }
        }
        return "UTC";
    }
}

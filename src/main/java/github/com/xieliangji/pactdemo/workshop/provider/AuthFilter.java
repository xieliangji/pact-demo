package github.com.xieliangji.pactdemo.workshop.provider;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Coder   谢良基
 * Date    2021/11/25 10:56
 */
@Component
public class AuthFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = ((HttpServletRequest) request).getHeader("Authorization");
        System.out.println(authHeader);
        if (authHeader == null) {
            ((HttpServletResponse) response).sendError(401, "Unauthorized");
            return;
        }
        authHeader = authHeader.replaceAll("Bearer ", "");
        if (!isValidAuthTimestamp(authHeader)) {
            ((HttpServletResponse) response).sendError(401, "Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValidAuthTimestamp(String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date headerDate = format.parse(timestamp);
            long diff = (System.currentTimeMillis() - headerDate.getTime()) / 1000;
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}

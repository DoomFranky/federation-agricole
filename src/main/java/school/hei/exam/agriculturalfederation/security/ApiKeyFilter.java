package school.hei.exam.agriculturalfederation.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    private final String apiKeyHeader = "x-api-key";
    private final String validApiKey;

    public ApiKeyFilter() {
        String envKey = System.getenv("AGRICULTURAL_API_KEY");
        this.validApiKey = envKey != null && !envKey.isBlank() ? envKey : "agri-secure-key";
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String apiKey = httpRequest.getHeader(apiKeyHeader);

        if (apiKey == null || !validApiKey.equals(apiKey)) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write("Bad credentials");
            return;
        }

        chain.doFilter(request, response);
    }
}

package preproject.spring_boot_security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";

    private final String ADMIN_URL = "/admin.html";
    private final String USER_URL = "/user.html";
    private final String DEFAULT_URL = "/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(ROLE_ADMIN)) {
            response.sendRedirect(ADMIN_URL);
            return;
        }
        if (roles.contains(ROLE_USER)) {
            response.sendRedirect(USER_URL);
            return;
        }
        response.sendRedirect(DEFAULT_URL);
    }
}
package ojosama.talkak.annotation;

import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {

    @Override
    public SecurityContext createSecurityContext(WithMockMember annotation) {

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken(annotation.id(), null, new ArrayList<>());

        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        return securityContext;
    }
}

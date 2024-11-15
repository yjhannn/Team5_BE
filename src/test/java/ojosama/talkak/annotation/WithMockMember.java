package ojosama.talkak.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMemberSecurityContextFactory.class)
public @interface WithMockMember {

    String id() default "1";
}

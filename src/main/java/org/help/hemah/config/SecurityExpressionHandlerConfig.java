package org.help.hemah.config;

import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

public class SecurityExpressionHandlerConfig implements SecurityExpressionHandler {


    @Override
    public ExpressionParser getExpressionParser() {
        return null;
    }

    @Override
    public EvaluationContext createEvaluationContext(Authentication authentication, Object invocation) {
        return null;
    }


}

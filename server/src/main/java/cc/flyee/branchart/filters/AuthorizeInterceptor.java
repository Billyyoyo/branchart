package cc.flyee.branchart.filters;


import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.models.ResultCode;
import cc.flyee.branchart.models.Token;
import cc.flyee.branchart.services.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthorizeInterceptor implements HandlerInterceptor {

    //客户端登录后将获得一个token   将token放在header: ba_token_id, userid放在header：ba_user_id
    //服务端收到api后   拿到token  到redis上 对比token/uuid/blacklist
    @Autowired
    TokenRepository tokenRepository;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.OPTIONS.toString())) {
            return true;
        }
        String ba_token_id = request.getHeader("ba_token_id");
        String ba_user_id = request.getHeader("ba_user_id");
        if (Strings.isEmpty(ba_user_id)
                || Strings.isEmpty(ba_token_id)) {
            throw new LogicException(ResultCode.ERROR_NO_LOGIN);
        }
        Token token = tokenRepository.getToken(ba_token_id);
        if (token == null) {
            throw new LogicException(ResultCode.ERROR_NO_LOGIN);
        }
        if (token.getForbid().intValue() == Token.FORBID_TRUE) {
            throw new LogicException(ResultCode.ERROR_FORBID);
        }
        return true;
    }

}

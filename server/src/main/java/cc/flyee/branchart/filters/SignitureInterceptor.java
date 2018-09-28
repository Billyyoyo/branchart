package cc.flyee.branchart.filters;


import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.models.ResultCode;
import cc.flyee.branchart.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SignitureInterceptor implements HandlerInterceptor {

    int i = 0;

    //客户端将url+UUID部分加密 加密字符串放在header：ba_signiture_code  UUID放在header：ba_uuid_code
    //服务端将url+UUID加密   对比api的签名
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.OPTIONS.toString())) {
            return true;
        }
        String ba_signiture_code = request.getHeader("ba_signiture_code");
        String ba_uuid_code = request.getHeader("ba_uuid_code");

        if (Strings.isEmpty(ba_uuid_code)) {
            throw new LogicException(ResultCode.ERROR_NO_UUID);
        }
        if (Strings.isEmpty(ba_signiture_code)) {
            throw new LogicException(ResultCode.ERROR_NO_SIGN);
        }
        String sign = request.getRequestURI() + "|" + ba_uuid_code;
        sign = CodeUtil.sigature(sign);
        if (!ba_signiture_code.equals(sign)) {
            throw new LogicException(ResultCode.ERROR_SIGNITURE);
        }
        return true;
    }

}

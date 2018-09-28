package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.AuthForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.TokenRepository;
import cc.flyee.branchart.services.UserExtraRepository;
import cc.flyee.branchart.services.UserRepository;
import cc.flyee.branchart.util.CodeUtil;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserExtraRepository userExtraRepository;

    /**
     * 注册  创建User，UserExtra，设置token,
     *
     * @param uuid
     * @return 返回user，userExtra，tokenId
     */
    @PostMapping(value = "/register")
    public ResultEntry register(@RequestHeader(name = "ba_uuid_code") String uuid,
                                @RequestBody AuthForm form) {
        if (Strings.isEmpty(form.getLoginName())) {
            throw new LogicException(ResultCode.ERROR_LOGINNAME_NULL);
        }
        if (Strings.isEmpty(form.getPassword())) {
            throw new LogicException(ResultCode.ERROR_PASSWORD_NULL);
        }
        Optional<User> userOptional = userRepository.findByLoginName(form.getLoginName());
        userOptional.ifPresent(user -> {
            throw new LogicException(ResultCode.ERROR_ACCOUNT_EXIST);
        });
        User user = new User();
        user.setLoginName(form.getLoginName());
        user.setPassword(CodeUtil.sigature(form.getPassword()));
        user.setCreateTime(System.currentTimeMillis());
        user.setModifyTime(System.currentTimeMillis());
        userRepository.save(user);
        UserExtra extra = new UserExtra();
        extra.setRole(user.getRole());
        extra.setUserId(user.getId());
        extra.setCreateTime(System.currentTimeMillis());
        extra.setModifyTime(System.currentTimeMillis());
        userExtraRepository.save(extra);
        user.setExtraId(extra.getId());
        userRepository.save(user);
        String tokenId = tokenRepository.setToken(user.getId(), uuid);
        user.setPassword(null);
        return new ResultEntry(ResultCode.SUCCESS,
                ResultData.build().put("user", user)
                        .put("extra", extra)
                        .put("token", tokenId).getData());
    }

    /**
     * 登录   验证deleteFlag和password   重新设置token
     *
     * @param uuid
     * @return user, userExtra, tokenId
     */
    @PostMapping(value = "/login")
    public ResultEntry login(@RequestHeader(name = "ba_uuid_code") String uuid,
                             @RequestBody AuthForm form) {
        if (Strings.isEmpty(form.getLoginName())) {
            throw new LogicException(ResultCode.ERROR_LOGINNAME_NULL);
        }
        if (Strings.isEmpty(form.getPassword())) {
            throw new LogicException(ResultCode.ERROR_PASSWORD_NULL);
        }
        Optional<User> userOptional = userRepository.findByLoginName(form.getLoginName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (CodeUtil.sigature(form.getPassword()).equals(user.getPassword())) {
                if (user.getDeleteFlag().intValue() == BaseModel.DELETE_FLAG_YES) {
                    throw new LogicException(ResultCode.ERROR_FORBID);
                } else {
                    user.setPassword(null);
                    String tokenId = tokenRepository.setToken(user.getId(), uuid);
                    Optional<UserExtra> extraOptional = userExtraRepository.findById(user.getExtraId());
                    UserExtra extra = extraOptional.get();
                    return new ResultEntry(ResultCode.SUCCESS,
                            ResultData.build().put("user", user)
                                    .put("extra", extra)
                                    .put("token", tokenId).getData());
                }
            } else {
                throw new LogicException(ResultCode.ERROR_PASSWORD);
            }
        } else {
            throw new LogicException(ResultCode.ERROR_ACCOUNT_NOT_EXIST);
        }
    }

    /**
     * 注销   清除token
     *
     * @param tokenId
     * @return true/false
     */
    @GetMapping(value = "/logout")
    public ResultEntry logout(@RequestHeader(name = "ba_token_id") String tokenId) {
        tokenRepository.delToken(tokenId);
        return new ResultEntry(ResultCode.SUCCESS);
    }

}

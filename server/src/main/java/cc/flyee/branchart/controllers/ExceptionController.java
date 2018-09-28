package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.models.ResultCode;
import cc.flyee.branchart.models.ResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResultEntry handleException(Exception e) {
        Locale locale = LocaleContextHolder.getLocale();
        log.error(e.getMessage());
        if (e instanceof LogicException) {
            LogicException logicException = (LogicException) e;
            String msg = messageSource.getMessage("error." + logicException.getErrorMsg(), null, locale);
            return new ResultEntry(logicException.getErrorCode(), msg);
        } else {
            e.printStackTrace();
            return new ResultEntry(ResultCode.ERROR_INTERNAL, e.getMessage());
        }
    }

}

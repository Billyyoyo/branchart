package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.models.ResultCode;
import cc.flyee.branchart.models.ResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TestController {

    @RequestMapping(value = "/")
    public ResultEntry test() {
        throw new LogicException(ResultCode.ERROR_UNKNOWN);
    }

}

package com.ruoguang.dcs.advice;



import com.ruoguang.dcs.enums.BusinessExceptionEnum;
import com.ruoguang.dcs.exception.BusinessException;
import com.ruoguang.dcs.model.ReturnInfo;
import com.ruoguang.dcs.util.ReturnInfoUtil;
import com.ruoguang.dcs.util.UniqueKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;


/**
 * ExceptionAdvice
 * 全局异常处理器封装成ReturnInfo
 *
 * @author: cc
 * @date: 2020-02-29 14:00
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("all")
public class GlobalExceptionAdvice {


    private String uniqueKey = "";


    private void before() {
        uniqueKey = UniqueKeyUtil.curTimeSSSHex();
    }


    /**
     * 全局异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ReturnInfo exceptionHandler(Throwable e) {
        before();
        ReturnInfo result = ReturnInfoUtil.exception(uniqueKey, BusinessExceptionEnum.SYSTEM_ERROE);
        log.error("Exception({})-->{}", uniqueKey, e);
        return result;
    }

    /**
     * 全局异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ReturnInfo exceptionHandler(Exception e) {
        before();
        ReturnInfo result = ReturnInfoUtil.exception(uniqueKey, BusinessExceptionEnum.SYSTEM_ERROE);
        log.error("Exception({})-->{}", uniqueKey, e);
        return result;
    }


    @ExceptionHandler(value = FileNotFoundException.class)
    public ReturnInfo exceptionHandler(FileNotFoundException e) {
        before();
        ReturnInfo result = ReturnInfoUtil.exception(uniqueKey, BusinessExceptionEnum.SYSTEM_ERROE);
        log.error("Exception({})-->{}", uniqueKey, e);
        return result;
    }
    /**
     * MethodArgumentNotValidException
     * 请求参数异常校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ReturnInfo exceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        before();
        ReturnInfo result = ReturnInfoUtil.exception(e);
        log.error("MethodArgumentNotValidException-->{}", e);
        log.error("equestURI -->{}", request.getRequestURI());
        return result;
    }

    /**
     * HttpRequestMethodNotSupportedException
     * 请求方式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ReturnInfo exceptionHandler(HttpRequestMethodNotSupportedException e) {
        before();
        ReturnInfo result = ReturnInfoUtil.exception(uniqueKey, BusinessExceptionEnum.NO_REQUEST_METHOD);
        log.error("HttpRequestMethodNotSupportedException({})-->{}", uniqueKey, e);
        return result;
    }


    /**
     * BusinessException
     * 自定义业务异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ReturnInfo BusinessExceptionHandler(BusinessException e) {
        before();
        ReturnInfo result = ReturnInfoUtil.exception(uniqueKey, e);
        log.error("BusinessException({})-->{}", uniqueKey, e);
        return result;
    }

    /**
     * MySQLIntegrityConstraintViolationException
     * SQL完整性约束违例异常
     * @param req
     * @param e
     * @returne
     */
//    @ExceptionHandler(value = MySQLIntegrityConstraintViolationException.class)
//    public ReturnInfo defaultErrorHandler(MySQLIntegrityConstraintViolationException e)  {
//        ReturnInfo result=new ReturnInfo(BusinessExceptionEnum.MYSQL_ICV_EXCEPTION);
//        log.error("MySQLIntegrityConstraintViolationException-->{}", e);
//        return result;
//    }

    /**
     * 数据库重复键异常提示语转换map
     */
    //private Map<String,String> duplicateKeyExceptionMap=new HashMap<>();

    /**
     * 数据库重复键异常
     * DuplicateKeyException
     * @param ex
     * @return
     */
//    @ExceptionHandler(value = DuplicateKeyException.class)
//    public ReturnInfo exceptionHandler(DuplicateKeyException e) {
//        duplicateKeyExceptionMap.put("phone","手机号");
//        duplicateKeyExceptionMap.put("username","用户名");
//        String message=BusinessExceptionEnum.DUPLICATE_KEY_EXCEPTION.getMessage();
//
//        String keyStr= e.getCause().getMessage();
//        if(StringUtils.isNotBlank(keyStr)){
//            String key = keyStr.split("key")[1].split("'")[1];
//            String value = duplicateKeyExceptionMap.get(key);
//            message=StringUtils.isBlank(value)?message:String.format("该%s已存在，请更换",value);
//        }
//
//        ReturnInfo result=new ReturnInfo(BusinessExceptionEnum.DUPLICATE_KEY_EXCEPTION.getCode(),message);
//        log.error("DuplicateKeyException-->{}",e );
//        return result;
//    }

    /**
     * UnauthorizedException
     * 授权异常处理
     * @param req
     * @param e
     * @returne
     */
//    @ExceptionHandler(value = UnauthorizedException.class)
//    public ReturnInfo defaultErrorHandler(UnauthorizedException e)  {
//        ReturnInfo result=new ReturnInfo(BusinessExceptionEnum.UNAUTHORIZED);
//        log.error("UnauthorizedException-->{}", e);
//        return result;
//    }

    /**
     * UnauthenticatedException
     * 认证异常处理
     * @param e
     * @return
     */
//    @ExceptionHandler(value = UnauthenticatedException.class)
//    public ReturnInfo defaultUnauthenticatedExceptionHandler(UnauthenticatedException e)  {
//        ReturnInfo result=new ReturnInfo(BusinessExceptionEnum.UNAUTHENTICATED);
//        log.error("UnauthenticatedException-->{}", e);
//        return result;
//    }
}

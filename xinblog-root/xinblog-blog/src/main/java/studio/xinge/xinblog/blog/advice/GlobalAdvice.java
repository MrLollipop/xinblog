package studio.xinge.xinblog.blog.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;

import java.util.HashMap;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2021/12/18
 * @Description
 */

@Slf4j
@RestControllerAdvice(basePackages = "studio.xinge.xinblog.blog.controller")
public class GlobalAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentValidation(MethodArgumentNotValidException e) {
        HashMap<String, String> errorFields = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach((item) -> {
                errorFields.put(item.getField(), item.getDefaultMessage());
            });
        }
        log.warn("参数校验异常{}，类型{}", e.getMessage(), e.getClass());
        return R.error(ReturnCode.PARAM_ERROR).put("error", errorFields);
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error("系统异常{}，类型{}，位置{}", e.getMessage(), e.getClass(), e.getStackTrace()[0]);
        return R.error().put("error", e.getMessage());
    }
}

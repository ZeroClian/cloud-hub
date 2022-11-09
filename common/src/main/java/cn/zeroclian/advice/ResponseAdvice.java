package cn.zeroclian.advice;

import cn.zeroclian.response.Response;
import cn.zeroclian.response.ResponseCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Justin
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {
    /**
     * 判断是否需要对响应进行处理
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        // 如果当前方法所在的类标识了 @IgnoreResponseAdvice 注解, 不需要处理
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 如果当前方法标识了 @IgnoreResponseAdvice 注解, 不需要处理
        return !methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class);
    }

    /**
     * 响应返回之前的处理
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Response<Object> result;
        if (body instanceof Response) {
            result = (Response<Object>) body;
        } else {
            result = Response.result(ResponseCode.SUCCESS, body);
        }
        return result;
    }
}

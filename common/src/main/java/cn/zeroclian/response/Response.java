package cn.zeroclian.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Justin
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    public static <T> Response<T> OK() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        ResponseCode code = ResponseCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            code = ResponseCode.SYSTEM_EXECUTION_ERROR;
        }
        return result(code, data);
    }

    public static <T> Response<T> failed() {
        return result(ResponseCode.SYSTEM_EXECUTION_ERROR, null);
    }

    public static <T> Response<T> failed(String message) {
        return result(ResponseCode.SYSTEM_EXECUTION_ERROR.getCode(), message, null);
    }

    public static <T> Response<T> result(IResponse response, T data) {
        return result(response.getCode(), response.getMessage(), data);
    }

    public static <T> Response<T> result(String code, String message, T data) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}

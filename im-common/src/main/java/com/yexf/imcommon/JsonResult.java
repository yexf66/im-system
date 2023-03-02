package com.yexf.imcommon;

//import com.fasterxml.jackson.annotation.JsonInclude;
import com.yexf.imcommon.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult<T> implements Serializable {

    private Integer code;

    /**
     * 当有结果返回业务数据时, 其内容是T类型,范型
     */
    private T data;

    /**
     * 当失败时描述具体原因,要求多语言化内容
     */
    private String message;


    public JsonResult(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }

    public JsonResult(Integer code, String msg) {
        this(code, null, msg);
    }

    public static <T> JsonResult<T> ok() {
        return new JsonResult<>(200, "success");
    }

    public static <T> JsonResult<T> ok(T data) {
        return new JsonResult<>(200, data, "success");
    }

    public static <T> JsonResult<T> fail(String msg) {
        return new JsonResult<>(400, null, msg);
    }


    public static <T> JsonResult<T> fail(String msg, T data) {
        return new JsonResult<>(400, data, msg);
    }

    public static <T> JsonResult<T> fail(String msg, T data, Integer code) {
        return new JsonResult<>(code, data, msg);
    }

    public static <T> JsonResult<T> fail(ResultCodeEnum codeEnum) {
        return new JsonResult<>(codeEnum.getCode(), null, codeEnum.getMessage());
    }
}

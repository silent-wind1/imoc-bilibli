package com.imooc.bilibli.domain.exception;

/**
 * 继承运行时异常类，自定义一个异常类
 *
 */
public class ConditionException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String code;

    public ConditionException(String code, String name) {
        super(name);
        this.code = code;
    }

    public ConditionException(String name) {
        super(name);
        code = "500";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

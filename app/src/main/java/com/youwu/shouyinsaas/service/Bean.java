package com.youwu.shouyinsaas.service;


/**
 * 快递查询的bjavaean
 */
public class Bean {

    public String message;//状态 OK

    public int code;
    public Object result;
    public Object data;
    public String success;
    public String key;
    public String timestamp;

    @Override
    public String toString() {
        return "Bean{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", result=" + result +
                ", data=" + data +
                ", success='" + success + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

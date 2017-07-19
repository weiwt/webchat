package com.amayadream.webchat.utils;

public class ResultDTO<T> {

    private boolean success;
    private String errorMsg;
    private T model;

    private ResultDTO(){}

    private ResultDTO(boolean success,String errorMsg,T model){
        this.success = success;
        this.errorMsg = errorMsg;
        this.model = model;
    }

    public static ResultDTO success(){
        ResultDTO resultDTO = new ResultDTO(true,null,null);
        return resultDTO;
    }

    public static <T> ResultDTO<T> successWith(T model) {
        ResultDTO resultDTO = new ResultDTO(true, null, model);
        return resultDTO;
    }

    public static ResultDTO fail(String errorMsg){
        ResultDTO resultDTO = new ResultDTO(false,errorMsg,null);
        return resultDTO;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isUnSuccess() {
        return !success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}

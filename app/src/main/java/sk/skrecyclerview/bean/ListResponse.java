package sk.skrecyclerview.bean;


public class ListResponse {

    public boolean isSuccess() {
        return state == 1001 || state == 1002;
    }

    public boolean isTokenInvalid () {
        return state == 3002;
    }

    public int state;

}

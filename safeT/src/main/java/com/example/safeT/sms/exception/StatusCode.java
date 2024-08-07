// HTTP 상태 코드를 정수 상수로 정의한 클래스

package com.example.safeT.sms.exception;

public class StatusCode {
    public static final Integer OK = 200;
    public static final Integer BAD_REQUEST =  400;
    public static final Integer UNAUTHORIZED = 401;
    public static final Integer NOT_FOUND = 404;
    public static final Integer INTERNAL_SERVER_ERROR = 500;
    public static final Integer DB_ERROR = 600;
}
// 사용자 정의 예외 클래스

package com.example.safeT.sms.exception;

public class CustomExceptions {
    public static class Exception extends RuntimeException { // 기본 예외 클래스
        public Exception(String message) { super(message);}
    }

    public static class SmsCertificationNumberMismatchException extends RuntimeException{ // sms 인증 번호 불일치 예외 클래스
        public SmsCertificationNumberMismatchException(String message){super(message);}
    }

    public static class MessageNotSentException extends RuntimeException { // 메시지 전송 실패 예외 클래스
        public MessageNotSentException(String message) { super(message);}
    }

    public static class UserPhoneNumberNotFoundException extends  RuntimeException{ // 등록되지 않은 사용자 전화번호 예외 클래스
        public UserPhoneNumberNotFoundException(String message){super(message);}
    }
}
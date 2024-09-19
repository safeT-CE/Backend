// REST API에서 사용할 수 있는 응답 형식을 정의한 클래스. API 응답 표준화 및 클라이언트 상호작용 일관성 유지를 위함
// ResponseEntity를 반환할 때 JSON으로 변환되어 클라이언트에게 전달

package com.example.kickboard.sms.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultRes<T> {
    private Integer statusCode;
    private String responseMessage;
    private T data;

    public DefaultRes(final Integer statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> DefaultRes<T> res(final Integer statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static<T> DefaultRes<T> res(final Integer statusCode, final String responseMessage, final T t) {
        return DefaultRes.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}
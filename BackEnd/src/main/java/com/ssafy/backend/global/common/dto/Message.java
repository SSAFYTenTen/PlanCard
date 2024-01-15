package com.ssafy.backend.global.common.dto;


import lombok.Builder;
import lombok.Getter;

/**
 * 응답 메시지를 위한 제네릭 클래스입니다.
 * @param <T> 메시지 본문의 데이터 타입입니다.
 */
@Getter
@Builder
public class Message<T> {

    private final DataHeader dataHeader;
    private final T dataBody;

    /**
     * 메시지의 헤더를 나타내는 DataHeader 클래스입니다.
     */
    @Getter
    @Builder
    private static class DataHeader {
        private final int successCode;
        private final String resultCode;
        private final String resultMessage;

        /**
         * 기본값으로 성공 DataHeader를 생성합니다.
         * @return 성공한 DataHeader 인스턴스입니다.
         */
        private static DataHeader success() {
            return DataHeader.builder()
                    .successCode(0)
                    .build();
        }

        /**
         * 지정된 코드와 메시지로 성공 DataHeader를 생성합니다.
         * @param code 결과 코드입니다.
         * @param resultMessage 결과 메시지입니다.
         * @return 성공한 DataHeader 인스턴스입니다.
         */
        private static DataHeader success(String code, String resultMessage) {
            return DataHeader.builder()
                    .successCode(0)
                    .resultCode(code)
                    .resultMessage(resultMessage)
                    .build();
        }

        /**
         * 지정된 코드와 메시지로 실패 DataHeader를 생성합니다.
         * @param resultCode 결과 코드입니다.
         * @param resultMessage 결과 메시지입니다.
         * @return 실패한 DataHeader 인스턴스입니다.
         */
        private static DataHeader fail(String resultCode, String resultMessage) {
            return DataHeader.builder()
                    .successCode(1)
                    .resultCode(resultCode)
                    .resultMessage(resultMessage)
                    .build();
        }
    }

    /**
     * 본문을 포함한 성공 메시지를 생성합니다.
     * @param dataBody 메시지 본문입니다.
     * @param <T> 데이터 본문의 타입입니다.
     * @return 성공한 메시지 인스턴스입니다.
     */
    public static <T> Message<T> success(T dataBody) {
        return Message.<T>builder()
                .dataHeader(DataHeader.success())
                .dataBody(dataBody)
                .build();
    }

    /**
     * 본문, 코드, 메시지를 포함한 성공 메시지를 생성합니다.
     * @param dataBody 메시지 본문입니다.
     * @param code 결과 코드입니다.
     * @param resultMessage 결과 메시지입니다.
     * @param <T> 데이터 본문의 타입입니다.
     * @return 성공한 메시지 인스턴스입니다.
     */
    public static <T> Message<T> success(T dataBody, String code, String resultMessage) {
        return Message.<T>builder()
                .dataHeader(DataHeader.success(code, resultMessage))
                .dataBody(dataBody)
                .build();
    }

    /**
     * 본문 없이 성공 메시지를 생성합니다.
     * @return 성공한 메시지 인스턴스입니다.
     */
    public static Message<Void> success() {
        return Message.<Void>builder()
                .dataHeader(DataHeader.success())
                .build();
    }

    /**
     * 실패 메시지를 생성합니다.
     * @param resultCode 결과 코드입니다.
     * @param resultMessage 결과 메시지입니다.
     * @param <T> 데이터 본문의 타입입니다.
     * @return 실패한 메시지 인스턴스입니다.
     */
    public static <T> Message<T> fail(String resultCode, String resultMessage) {
        return Message.<T>builder()
                .dataHeader(DataHeader.fail(resultCode, resultMessage))
                .dataBody(null)
                .build();
    }
}


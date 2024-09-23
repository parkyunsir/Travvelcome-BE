package com.example.backend.apiPayload.code.status;

import com.example.backend.apiPayload.code.BaseErrorCode;
import com.example.backend.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

  // 가장 일반적인 응답
  _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
  _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
  _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
  _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

  // 멤버 관련 에러
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),

  // 랜드마크 관련 에러
  INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "LANDMARK4001", "잘못된 카테고리입니다. (nature, history, culture) 중에 입력해주세요."),
  NULL_CATEGORY(HttpStatus.BAD_REQUEST, "LANDMARK4002", "카테고리의 값이 null입니다."),
  LANDMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "LANDMARK4003", "랜드마크가 존재하지 않습니다."),
  ALREADY_FIND_LANDMARK(HttpStatus.BAD_REQUEST, "LANDMARK4004", "이미 발견한 랜드마크 입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ErrorReasonDTO getReason() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .build();
  }

  @Override
  public ErrorReasonDTO getReasonHttpStatus() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build()
        ;
  }
}
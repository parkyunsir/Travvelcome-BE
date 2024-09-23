package com.example.backend.apiPayload.exception.handler;

import com.example.backend.apiPayload.code.BaseErrorCode;
import com.example.backend.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

  public TempHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}

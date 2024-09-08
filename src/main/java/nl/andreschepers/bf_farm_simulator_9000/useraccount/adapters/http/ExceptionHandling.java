/*
 * Copyright 2024 André Schepers
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.http;

import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.service.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandling {

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<ErrorResponseBody> handleRuntimeException(RuntimeException e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponseBody> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    var fieldErrorsAsStringList =
        e.getFieldErrors().stream()
            .map(
                fieldError ->
                    "Field: [%s] has error: [%s]"
                        .formatted(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

    return ResponseEntity.badRequest()
        .body(
            ErrorResponseBody.builder()
                .errorCode(ErrorCode.INVALID_REQUEST)
                .errorMessages(fieldErrorsAsStringList)
                .build());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponseBody> handleException(Exception e) {

    log.error("Unexpected error", e);

    return ResponseEntity.internalServerError()
        .body(
            ErrorResponseBody.builder()
                .errorCode(ErrorCode.SERVER_ERROR)
                .errorMessages(List.of(""))
                .build());
  }

  public enum ErrorCode {
    RESOURCE_NOT_FOUND,
    INVALID_REQUEST,
    SERVER_ERROR;
  }

  @Builder
  public record ErrorResponseBody(ErrorCode errorCode, List<String> errorMessages) {}
}

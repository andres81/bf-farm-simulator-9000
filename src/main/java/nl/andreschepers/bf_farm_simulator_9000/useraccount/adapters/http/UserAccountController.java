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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.http.validation.ValidUUID;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.input.CreateUserAccountUseCase;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.input.FindUserAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-account")
@RequiredArgsConstructor
public class UserAccountController {

  private final CreateUserAccountUseCase createUserAccountUseCase;
  private final FindUserAccountUseCase findUserAccountUseCase;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("/{accountId}")
  public UserAccountResponseDto getUserAccountByAccountId(
      @PathVariable("accountId") @ValidUUID String accountId) {
    var domainUserAccount = findUserAccountUseCase.findUserAccountByAccountId(
        UUID.fromString(accountId));
    return new UserAccountResponseDto(domainUserAccount.getUserAccountId().id(),
        domainUserAccount.getEmail(),
        domainUserAccount.getUserName());
  }

  @GetMapping
  public UserAccountResponseDto getUserAccountByUsernameOrEmail(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email) {
    var domainUserAccount =
        findUserAccountUseCase.findUserAccountByUsernameOrEmail(username, email);
    return new UserAccountResponseDto(
        domainUserAccount.getUserAccountId().id(),
        domainUserAccount.getEmail(),
        domainUserAccount.getUserName());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createUserAccount(@RequestBody @Valid UserAccountRequestCreationDto userAccount) {
    createUserAccountUseCase.createAccount(
        userAccount.email, userAccount.userName, userAccount.password());
  }

  public record UserAccountRequestCreationDto(
      @NotNull(message = "email cannot be null") String email,
      @NotNull String userName,
      @NotNull String password) {}

  public record UserAccountResponseDto(UUID accountId, String email, String userName) {}
}

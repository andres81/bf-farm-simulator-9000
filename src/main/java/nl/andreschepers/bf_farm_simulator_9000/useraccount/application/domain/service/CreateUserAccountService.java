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

package nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.entity.UserAccount;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.entity.UserAccountPassword;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.service.exception.ResourceAlreadyExistsException;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.input.CreateUserAccountUseCase;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output.UserAccountPasswordPersistencePort;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output.UserAccountPersistencePort;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.util.PasswordHashUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserAccountService implements CreateUserAccountUseCase {

  private final UserAccountPersistencePort userAccountPersistencePort;
  private final UserAccountPasswordPersistencePort userAccountPasswordPersistencePort;
  private final PasswordHashUtil passwordHashUtil;

  @Override
  public UserAccount createAccount(String email, String username, String password) {
    if (userAccountPersistencePort.existsByUserName(username)) {
      throw new ResourceAlreadyExistsException("User account with given username already exists.");
    } else if (userAccountPersistencePort.existsByEmail(email)) {
      throw new ResourceAlreadyExistsException("User account with given email already exists.");
    }

    var userAccountId = new UserAccount.UserAccountId(UUID.randomUUID());
    var userAccount =
        UserAccount.builder()
            .userAccountId(userAccountId)
            .userName(username)
            .email(email)
            .build();
    userAccountPersistencePort.persistUserAccount(userAccount);
    var userAccountPassword =
        UserAccountPassword.builder()
            .userAccountId(userAccountId)
            .password(
                /*

                Image source: https://ascii.co.uk/art/skulls

                                 uuuuuuu
                             uu$$$$$$$$$$$uu
                          uu$$$$$$$$$$$$$$$$$uu
                         u$$$$$$$$$$$$$$$$$$$$$u
                        u$$$$$$$$$$$$$$$$$$$$$$$u
                       u$$$$$$$$$$$$$$$$$$$$$$$$$u
                       u$$$$$$$$$$$$$$$$$$$$$$$$$u
                       u$$$$$$"   "$$$"   "$$$$$$u
                       "$$$$"      u$u       $$$$"
                        $$$u       u$u       u$$$
                        $$$u      u$$$u      u$$$
                         "$$$$uu$$$   $$$uu$$$$"
                          "$$$$$$$"   "$$$$$$$"
                            u$$$$$$$u$$$$$$$u
                             u$"$"$"$"$"$"$u
                  uuu        $$u$ $ $ $ $u$$       uuu
                 u$$$$        $$$$$u$u$u$$$       u$$$$
                  $$$$$uu      "$$$$$$$$$"     uu$$$$$$
                u$$$$$$$$$$$uu    """""    uuuu$$$$$$$$$$
                $$$$"""$$$$$$$$$$uuu   uu$$$$$$$$$"""$$$"
                 """      ""$$$$$$$$$$$uu ""$"""
                           uuuu ""$$$$$$$$$$uuu
                  u$$$uuu$$$$$$$$$uu ""$$$$$$$$$$$uuu$$$
                  $$$$$$$$$$""""           ""$$$$$$$$$$$"
                   "$$$$$"                      ""$$$$""
                     $$$"                         $$$$"


                     HashTag-CareerEndingChoice: Never do this yourself. Use OAuth2 or something
                     !!!!!!! https://www.youtube.com/watch?v=8ZtInClXe1Q !!!!!!
                 */
                passwordHashUtil.hashSaltPassword(password)) //
            .build();
    userAccountPasswordPersistencePort.persistUserAccountPassword(userAccountPassword);
    return userAccount;
  }
}

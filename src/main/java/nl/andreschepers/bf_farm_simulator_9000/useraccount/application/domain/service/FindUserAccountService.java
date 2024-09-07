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
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.service.exception.ResourceNotFoundException;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.input.FindUserAccountUseCase;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output.UserAccountPersistencePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserAccountService implements FindUserAccountUseCase {

  private final UserAccountPersistencePort userAccountPersistencePort;

  @Override
  public UserAccount findUserAccountByAccountId(UUID accountId) {
    return userAccountPersistencePort
        .findUserAccountByAccountId(accountId)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public UserAccount findUserAccountByEmail(String email) {
    return userAccountPersistencePort
        .findUserAccountByEmail(email)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public UserAccount findUserAccountByUsername(String username) {
    return userAccountPersistencePort
        .findUserAccountByUsername(username)
        .orElseThrow(ResourceNotFoundException::new);
  }
}

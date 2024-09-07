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

package nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.jpaentity.UserAccountJpaEntity;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.repository.UserAccountJpaEntityRepository;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.entity.UserAccount;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output.UserAccountPersistencePort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAccountPersistenceAdapter implements UserAccountPersistencePort {

  private final UserAccountJpaEntityRepository userAccountJpaEntityRepository;

  @Override
  public boolean existsByUserName(String username) {
    return userAccountJpaEntityRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userAccountJpaEntityRepository.existsByEmail(email);
  }

  @Override
  public void persistUserAccount(UserAccount userAccount) {
    userAccountJpaEntityRepository.save(
        UserAccountJpaEntity.builder()
            .username(userAccount.getUserName())
            .email(userAccount.getEmail())
            .accountId(userAccount.getUserAccountId().id())
            .build());
  }

  @Override
  public Optional<UserAccount> findUserAccountByAccountId(UUID accountId) {
    return userAccountJpaEntityRepository
        .findByAccountId(accountId)
        .map(
            userAccountJpaEntity ->
                UserAccount.builder()
                    .userAccountId(new UserAccount.UserAccountId(UUID.randomUUID()))
                    .userName(userAccountJpaEntity.getUsername())
                    .email(userAccountJpaEntity.getEmail())
                    .build());
  }

  @Override
  public Optional<UserAccount> findUserAccountByUsername(String username) {
    return Optional.empty();
  }

  @Override
  public Optional<UserAccount> findUserAccountByEmail(String email) {
//    return userAccountJpaEntityRepository.findByEmail(email);
    return null;
  }
}

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

import lombok.RequiredArgsConstructor;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.jpaentity.UserAccountPasswordJpaEntity;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.repository.UserAccountPasswordJpaEntityRepository;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.entity.UserAccountPassword;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output.UserAccountPasswordPersistencePort;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserAccountPasswordPersistenceAdapter implements UserAccountPasswordPersistencePort {

  private final UserAccountPasswordJpaEntityRepository userAccountPasswordJpaEntityRepository;

  @Override
  @Transactional
  public void persistUserAccountPassword(UserAccountPassword password) {

    if (userAccountPasswordJpaEntityRepository.exists(
        Example.of(
            UserAccountPasswordJpaEntity.builder()
                .userAccountId(password.getUserAccountId().id())
                .build()))) {
      var existing = userAccountPasswordJpaEntityRepository.findByUserAccountId(password.getUserAccountId().id());
      existing.get().setPassword(password.getPassword());
      return;
    }

    var newPassword = UserAccountPasswordJpaEntity.builder()
        .userAccountId(password.getUserAccountId().id())
        .password(password.getPassword())
        .build();
    userAccountPasswordJpaEntityRepository.save(newPassword);
  }
}

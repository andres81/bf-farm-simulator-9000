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

package nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output;

import java.util.Optional;
import java.util.UUID;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.jpaentity.UserAccountJpaEntity;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.entity.UserAccount;

public interface UserAccountPersistencePort {

  boolean existsByUserName(String userName);

  boolean existsByEmail(String email);

  void persistUserAccount(UserAccount userAccount);

  Optional<UserAccount> findUserAccountByAccountId(UUID accountId);

  Optional<UserAccount> findUserAccountByUsername(String username);

  Optional<UserAccount> findUserAccountByEmail(String email);
}

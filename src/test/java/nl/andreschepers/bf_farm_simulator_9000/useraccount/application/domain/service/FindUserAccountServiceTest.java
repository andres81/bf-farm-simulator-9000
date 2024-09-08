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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.domain.entity.UserAccount;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.ports.output.UserAccountPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindUserAccountServiceTest {

  private static final UUID ACCOUNT_ID = UUID.randomUUID();
  private static final String USER_NAME = "USER_NAME";
  private static final String EMAIL = "EMAIL";

  @Mock private UserAccountPersistencePort userAccountPersistencePort;

  @InjectMocks private FindUserAccountService sut;

  @Test
  void findUserAccountByAccountId() {

    var userAccount = mock(UserAccount.class);
    when(userAccountPersistencePort.findUserAccountByAccountId(ACCOUNT_ID))
        .thenReturn(Optional.of(userAccount));

    var retrievedUserAccount = sut.findUserAccountByAccountId(ACCOUNT_ID);

    assertEquals(userAccount, retrievedUserAccount);
  }

  @Test
  void findUserAccountByEmail() {
    var userAccount = mock(UserAccount.class);
    when(userAccountPersistencePort.findUserAccountByEmail(EMAIL))
        .thenReturn(Optional.of(userAccount));

    var retrievedUserAccount = sut.findUserAccountByUsernameOrEmail(null, EMAIL);

    assertEquals(userAccount, retrievedUserAccount);
  }

  @Test
  void findUserAccountByUsername() {
    var userAccount = mock(UserAccount.class);
    when(userAccountPersistencePort.findUserAccountByUsername(USER_NAME))
        .thenReturn(Optional.of(userAccount));

    var retrievedUserAccount = sut.findUserAccountByUsernameOrEmail(USER_NAME, null);

    assertEquals(userAccount, retrievedUserAccount);
  }
}

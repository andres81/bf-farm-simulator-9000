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

package nl.andreschepers.bf_farm_simulator_9000;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.http.UserAccountController.UserAccountRequestCreationDto;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.http.UserAccountController.UserAccountResponseDto;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.repository.UserAccountJpaEntityRepository;
import nl.andreschepers.bf_farm_simulator_9000.useraccount.adapters.persistence.repository.UserAccountPasswordJpaEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class End2EndUserAccountCreationUseCaseTest {

  private static final String USER_ACCOUNT_ENDPOINT_PATH = "/api/user-account";

  private static final String EMAIL = "EMAIL";
  private static final String USERNAME = "USERNAME";
  private static final String PASSWORD = "PASSWORD";

  @Autowired private MockMvc mockMvc;

  @Autowired private UserAccountJpaEntityRepository userAccountJpaEntityRepository;
  @Autowired private UserAccountPasswordJpaEntityRepository userAccountPasswordJpaEntityRepository;

  @Test
  void end2endUserAccountCreationUseCase() throws Exception {

    assertEquals(0, userAccountJpaEntityRepository.findAll().size());
    assertEquals(0, userAccountPasswordJpaEntityRepository.findAll().size());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(USER_ACCOUNT_ENDPOINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(new UserAccountRequestCreationDto(EMAIL, USERNAME, PASSWORD))))
        .andExpect(status().isCreated());

    assertEquals(1, userAccountJpaEntityRepository.findAll().size());
    assertEquals(1, userAccountPasswordJpaEntityRepository.findAll().size());

    var userAccount =
        deserialize(
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get(USER_ACCOUNT_ENDPOINT_PATH)
                        .queryParam("email", EMAIL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAccountResponseDto.class);

    assertEquals(EMAIL, userAccount.email());
    assertEquals(USERNAME, userAccount.userName());

    var userAccountId = userAccount.accountId();

    userAccount =
        deserialize(
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get(USER_ACCOUNT_ENDPOINT_PATH)
                        .queryParam("username", USERNAME)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAccountResponseDto.class);

    assertEquals(EMAIL, userAccount.email());
    assertEquals(USERNAME, userAccount.userName());
    assertEquals(userAccountId, userAccount.accountId());

    userAccount =
        deserialize(
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get(
                            USER_ACCOUNT_ENDPOINT_PATH + "/" + userAccount.accountId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAccountResponseDto.class);

    assertEquals(EMAIL, userAccount.email());
    assertEquals(USERNAME, userAccount.userName());
    assertEquals(userAccountId, userAccount.accountId());

    assertEquals(1, userAccountJpaEntityRepository.findAll().size());
    assertEquals(1, userAccountPasswordJpaEntityRepository.findAll().size());
  }

  private <T> T deserialize(String contentAsString, Class<T> userAccountClass)
      throws JsonProcessingException {
    return new ObjectMapper().readValue(contentAsString, userAccountClass);
  }

  private String serialize(UserAccountRequestCreationDto dto) throws JsonProcessingException {
    return new ObjectMapper().writer().writeValueAsString(dto);
  }
}

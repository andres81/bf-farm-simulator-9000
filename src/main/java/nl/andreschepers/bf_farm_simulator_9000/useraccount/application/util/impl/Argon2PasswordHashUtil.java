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

package nl.andreschepers.bf_farm_simulator_9000.useraccount.application.util.impl;

import nl.andreschepers.bf_farm_simulator_9000.useraccount.application.util.PasswordHashUtil;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Argon2PasswordHashUtil implements PasswordHashUtil {

  @Override
  public String hashSaltPassword(String rawPassword) {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode(rawPassword);
  }

  @Override
  public boolean verifyPassword(String password, String hashSaltPassword) {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        .matches(password, hashSaltPassword);
  }
}

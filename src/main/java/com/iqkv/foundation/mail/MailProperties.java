/*
 * Copyright 2026 iQKV Foundation Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.foundation.mail;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Mail transport configuration, bound from {@code iqkv.notification.mail.*}.
 *
 * <p>{@code provider} selects the active {@link EmailSender} implementation: {@code smtp}
 * (default, backed by Spring's {@code JavaMailSender}) or {@code resend} (Resend HTTP API).
 *
 * @param from     the sender address
 * @param fromName the sender display name
 * @param replyTo  optional reply-to address
 * @param provider active transport: {@code smtp} or {@code resend}
 * @param resend   Resend-specific configuration, required when {@code provider=resend}
 */
@Validated
@ConfigurationProperties(prefix = "iqkv.notification.mail")
public record MailProperties(
    @NotBlank String from,
    @NotBlank String fromName,
    String replyTo,
    @NotBlank @Pattern(regexp = "^(smtp|resend)$", message = "provider must be 'smtp' or 'resend'") String provider,
    @Valid Resend resend
) {

  /**
   * Resend-specific configuration, required when {@code provider=resend}.
   */
  public record Resend(String apiKey) {
  }
}

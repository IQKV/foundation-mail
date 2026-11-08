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

package com.iqkv.foundation.mail.config;

import com.iqkv.foundation.mail.EmailSender;
import com.iqkv.foundation.mail.MailProperties;
import com.iqkv.foundation.mail.ResendEmailSender;
import com.iqkv.foundation.mail.SmtpEmailSender;
import com.resend.Resend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Auto-configuration that registers exactly one {@link EmailSender} bean depending on
 * {@code iqkv.notification.mail.provider}.
 *
 * <ul>
 *   <li>{@code smtp} (default) — {@link SmtpEmailSender} via Spring's {@link JavaMailSender}.</li>
 *   <li>{@code resend} — {@link ResendEmailSender} via the Resend HTTP API; requires
 *       {@code iqkv.notification.mail.resend.api-key} to be set.</li>
 * </ul>
 *
 * <p>Each provider lives in its own {@link ConditionalOnClass class-guarded} configuration so
 * that a service only needs the transport SDK it actually uses on the classpath.
 */
@AutoConfiguration
@EnableConfigurationProperties(MailProperties.class)
public class MailAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MailAutoConfiguration.class);

  /**
   * SMTP sender — active when {@code iqkv.notification.mail.provider=smtp} or the property
   * is absent ({@code matchIfMissing=true} keeps backwards compatibility).
   */
  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(JavaMailSender.class)
  @ConditionalOnProperty(name = "iqkv.notification.mail.provider", havingValue = "smtp", matchIfMissing = true)
  static class SmtpConfiguration {

    @Bean
    public EmailSender smtpEmailSender(final JavaMailSender javaMailSender) {
      log.info("Email provider: SMTP");
      return new SmtpEmailSender(javaMailSender);
    }
  }

  /**
   * Resend sender — active when {@code iqkv.notification.mail.provider=resend}.
   * The API key is read from the properties record to keep it out of logs.
   */
  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(Resend.class)
  @ConditionalOnProperty(name = "iqkv.notification.mail.provider", havingValue = "resend")
  static class ResendConfiguration {

    @Bean
    public EmailSender resendEmailSender(final MailProperties props) {
      final var resend = props.resend();
      if (resend == null || resend.apiKey() == null || resend.apiKey().isBlank()) {
        throw new IllegalStateException(
            "iqkv.notification.mail.resend.api-key must be set when provider=resend");
      }
      log.info("Email provider: Resend");
      return new ResendEmailSender(resend.apiKey());
    }
  }
}

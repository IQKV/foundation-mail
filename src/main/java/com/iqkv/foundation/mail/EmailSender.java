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

/**
 * Transport abstraction for outbound email delivery.
 *
 * <p>Callers own template rendering and subject resolution; once those are complete they
 * delegate the actual transport to the active implementation of this interface. Switching
 * providers (SMTP ↔ Resend) is therefore a configuration-only concern — no business code
 * changes required.
 *
 * <p>Implementations must throw {@link EmailDeliveryException} on delivery failure so that
 * the caller can handle or propagate it uniformly.
 */
public interface EmailSender {

  /**
   * Send a single email message.
   *
   * @param request the fully-resolved, ready-to-send message descriptor
   * @throws EmailDeliveryException if the underlying transport reports a failure
   */
  void send(EmailSendRequest request);
}

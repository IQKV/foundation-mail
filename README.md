# Foundation Mail ✉️

Shared email transport strategy for IQKV microservices. Selects between SMTP (Spring JavaMail) and Resend at runtime via `iqkv.notification.mail.provider`, exposing a single `EmailSender` port so services no longer duplicate send adapters.

## Quick Links

- [Contributing Guidelines](.github/CONTRIBUTING.md)

## Key Components

### EmailSender

Transport-agnostic port. `send(EmailSendRequest)` sends an HTML message and throws `EmailDeliveryException` on failure.

### EmailSendRequest

Immutable record: `from`, `fromName`, `replyTo`, `to`, `subject`, `htmlBody`.

### SmtpEmailSender

Sends through Spring's `JavaMailSender` as a UTF-8 multipart HTML message.

### ResendEmailSender

Sends through the Resend HTTP API (`com.resend:resend-java`), formatting the sender as `Name <address>`.

### MailProperties

Binds `iqkv.notification.mail.*`:

```yaml
iqkv:
    notification:
        mail:
            from: no-reply@example.com
            from-name: Example
            reply-to: support@example.com
            provider: smtp # smtp (default) or resend
            resend:
                api-key: ${RESEND_API_KEY}
```

## Usage

### Maven Dependency

```xml
<dependency>
  <groupId>com.iqkv</groupId>
  <artifactId>foundation-mail</artifactId>
  <version>0.24.0-SNAPSHOT</version>
</dependency>
```

### Spring Boot Auto-Configuration

When added to a Spring Boot project, `MailAutoConfiguration` registers exactly one `EmailSender` bean based on the classpath and `iqkv.notification.mail.provider`:

- **SMTP** when `spring-boot-starter-mail` is present and the provider is `smtp` (or unset).
- **Resend** when `com.resend:resend-java` is present, the provider is `resend`, and `iqkv.notification.mail.resend.api-key` is set.

The transport SDKs are optional dependencies of this library — add the one your service uses.

## Development

```bash
# Build and install to local Maven repository
./mvnw clean install -Dcheckstyle.skip=true
```

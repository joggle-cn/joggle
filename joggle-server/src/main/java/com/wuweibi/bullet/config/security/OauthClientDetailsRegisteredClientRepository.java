package com.wuweibi.bullet.config.security;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

/**
 * 兼容旧 oauth_client_details 表的 Spring Authorization Server 客户端仓储。
 */
public class OauthClientDetailsRegisteredClientRepository implements RegisteredClientRepository {

    private static final int DEFAULT_ACCESS_TOKEN_VALIDITY_SECONDS = 7200;

    private static final int DEFAULT_REFRESH_TOKEN_VALIDITY_SECONDS = 108000;

    private static final AuthorizationGrantType PASSWORD_GRANT_TYPE = new AuthorizationGrantType("password");

    private final JdbcTemplate jdbcTemplate;

    public OauthClientDetailsRegisteredClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("oauth_client_details is managed by the existing admin data model");
    }

    @Override
    public RegisteredClient findById(String id) {
        return findByClientId(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        try {
            return jdbcTemplate.queryForObject("""
                            select client_id, client_secret, scope, authorized_grant_types,
                                   web_server_redirect_uri, access_token_validity,
                                   refresh_token_validity, additional_information, autoapprove
                              from oauth_client_details
                             where client_id = ?
                            """, new ClientDetailsRowMapper(), clientId);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static RegisteredClient buildRegisteredClient(ClientDetailsRow row) {
        RegisteredClient.Builder builder = RegisteredClient.withId(row.clientId())
                .clientId(row.clientId())
                .clientSecret(row.clientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);

        split(row.authorizedGrantTypes()).forEach(grantType ->
                builder.authorizationGrantType(resolveGrantType(grantType)));
        split(row.redirectUri()).forEach(builder::redirectUri);
        split(row.scope()).forEach(builder::scope);

        ClientSettings.Builder clientSettings = ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(!"true".equalsIgnoreCase(StringUtils.trim(row.autoApprove())));
        if (StringUtils.isNotBlank(row.additionalInformation())) {
            try {
                Map<String, Object> settings = JSON.parseObject(row.additionalInformation());
                settings.forEach(clientSettings::setting);
            } catch (RuntimeException ignored) {
                // 旧字段是扩展 JSON，解析失败不影响客户端基础认证。
            }
        }

        int accessTokenValidity = row.accessTokenValidity() == null
                ? DEFAULT_ACCESS_TOKEN_VALIDITY_SECONDS : row.accessTokenValidity();
        int refreshTokenValidity = row.refreshTokenValidity() == null
                ? DEFAULT_REFRESH_TOKEN_VALIDITY_SECONDS : row.refreshTokenValidity();

        return builder.clientSettings(clientSettings.build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(accessTokenValidity))
                        .refreshTokenTimeToLive(Duration.ofSeconds(refreshTokenValidity))
                        .build())
                .build();
    }

    private static AuthorizationGrantType resolveGrantType(String grantType) {
        return switch (grantType) {
            case "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE;
            case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
            case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
            case "password" -> PASSWORD_GRANT_TYPE;
            default -> new AuthorizationGrantType(grantType);
        };
    }

    private static String encodeClientSecret(String clientSecret) {
        if (StringUtils.isBlank(clientSecret)) {
            return "{noop}";
        }
        String secret = StringUtils.trim(clientSecret);
        if (secret.startsWith("{")) {
            return secret;
        }
        if (secret.startsWith("$2a$") || secret.startsWith("$2b$") || secret.startsWith("$2y$")) {
            return "{bcrypt}" + secret;
        }
        return "{noop}" + secret;
    }

    private static java.util.List<String> split(String value) {
        if (StringUtils.isBlank(value)) {
            return java.util.List.of();
        }
        return Arrays.stream(value.split("[,\\s]+"))
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .toList();
    }

    private record ClientDetailsRow(String clientId, String clientSecret, String scope,
                                    String authorizedGrantTypes, String redirectUri,
                                    Integer accessTokenValidity, Integer refreshTokenValidity,
                                    String additionalInformation, String autoApprove) {
    }

    private static final class ClientDetailsRowMapper implements RowMapper<RegisteredClient> {
        @Override
        public RegisteredClient mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientDetailsRow row = new ClientDetailsRow(
                    rs.getString("client_id"),
                    rs.getString("client_secret"),
                    rs.getString("scope"),
                    rs.getString("authorized_grant_types"),
                    rs.getString("web_server_redirect_uri"),
                    (Integer) rs.getObject("access_token_validity"),
                    (Integer) rs.getObject("refresh_token_validity"),
                    rs.getString("additional_information"),
                    rs.getString("autoapprove")
            );
            return buildRegisteredClient(row);
        }
    }
}

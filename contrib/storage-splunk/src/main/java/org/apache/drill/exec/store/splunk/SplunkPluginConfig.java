/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.drill.exec.store.splunk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.drill.common.PlanStringBuilder;
import org.apache.drill.common.logical.StoragePluginConfig;
import org.apache.drill.common.logical.security.CredentialsProvider;
import org.apache.drill.common.logical.security.PlainCredentialsProvider;
import org.apache.drill.exec.store.security.CredentialProviderUtils;
import org.apache.drill.exec.store.security.UsernamePasswordCredentials;

import java.util.Objects;
import java.util.Optional;

@JsonTypeName(SplunkPluginConfig.NAME)
public class SplunkPluginConfig extends StoragePluginConfig {

  public static final String NAME = "splunk";
  public static final int DISABLED_RECONNECT_RETRIES = 1;
  public static final int DEFAULT_WRITER_BATCH_SIZE = 1000;
  public static final int DEFAULT_MAX_READER_COLUMNS = 1024;
  public static final int DEFAULT_MAX_CACHE_SIZE = 10000;
  public static final int DEFAULT_CACHE_EXPIRATION = 1024;

  private final String scheme;
  private final String hostname;
  private final String earliestTime;
  private final String latestTime;
  private final Integer port;
  private final String app;
  private final String owner;
  private final String token;
  private final String cookie;
  private final boolean validateCertificates;
  private final boolean validateHostname;
  private final Integer reconnectRetries;
  private final boolean writable;
  private final Integer writerBatchSize;
  private final Integer maxColumns;
  private final Integer maxCacheSize;
  private final Integer cacheExpiration;

  @JsonCreator
  public SplunkPluginConfig(@JsonProperty("username") String username,
                            @JsonProperty("password") String password,
                            @JsonProperty("scheme") String scheme,
                            @JsonProperty("hostname") String hostname,
                            @JsonProperty("port") Integer port,
                            @JsonProperty("app") String app,
                            @JsonProperty("owner") String owner,
                            @JsonProperty("token") String token,
                            @JsonProperty("cookie") String cookie,
                            @JsonProperty("validateCertificates") boolean validateCertificates,
                            @JsonProperty("validateHostname") boolean validateHostname,
                            @JsonProperty("earliestTime") String earliestTime,
                            @JsonProperty("latestTime") String latestTime,
                            @JsonProperty("credentialsProvider") CredentialsProvider credentialsProvider,
                            @JsonProperty("reconnectRetries") Integer reconnectRetries,
                            @JsonProperty("authMode") String authMode,
                            @JsonProperty("writable") boolean writable,
                            @JsonProperty("writableBatchSize") Integer writerBatchSize,
                            @JsonProperty("maxColumns") Integer maxColumns,
                            @JsonProperty("maxCacheSize") Integer maxCacheSize,
                            @JsonProperty("cacheExpiration") Integer cacheExpiration
      ) {
    super(CredentialProviderUtils.getCredentialsProvider(username, password, credentialsProvider),
        credentialsProvider == null, AuthMode.parseOrDefault(authMode, AuthMode.SHARED_USER));
    this.scheme = scheme;
    this.hostname = hostname;
    this.port = port;
    this.app = app;
    this.owner = owner;
    this.token = token;
    this.cookie = cookie;
    this.writable = writable;
    this.validateCertificates = validateCertificates;
    this.validateHostname = validateHostname;
    this.earliestTime = earliestTime;
    this.latestTime = latestTime == null ? "now" : latestTime;
    this.reconnectRetries = reconnectRetries;
    this.writerBatchSize = writerBatchSize;
    this.maxColumns = maxColumns;
    this.maxCacheSize = maxCacheSize;
    this.cacheExpiration = cacheExpiration;
  }

  private SplunkPluginConfig(SplunkPluginConfig that, CredentialsProvider credentialsProvider) {
    super(getCredentialsProvider(credentialsProvider), credentialsProvider == null, that.authMode);
    this.scheme = that.scheme;
    this.hostname = that.hostname;
    this.port = that.port;
    this.app = that.app;
    this.owner = that.owner;
    this.token = that.token;
    this.writable = that.writable;
    this.cookie = that.cookie;
    this.validateCertificates = that.validateCertificates;
    this.validateHostname = that.validateHostname;
    this.earliestTime = that.earliestTime;
    this.latestTime = that.latestTime;
    this.reconnectRetries = that.reconnectRetries;
    this.writerBatchSize = that.writerBatchSize;
    this.maxColumns = that.maxColumns;
    this.maxCacheSize = that.maxCacheSize;
    this.cacheExpiration = that.cacheExpiration;
  }

  /**
   * Gets the credentials. This method is used when user translation is not enabled.
   * @return An {@link Optional} containing {@link UsernamePasswordCredentials} from the config.
   */
  @JsonIgnore
  public Optional<UsernamePasswordCredentials> getUsernamePasswordCredentials() {
    return new UsernamePasswordCredentials.Builder()
      .setCredentialsProvider(credentialsProvider)
      .build();
  }

  /**
   * Gets the credentials. This method is used when user translation is enabled.
   * @return An {@link Optional} containing {@link UsernamePasswordCredentials} from the config.
   */
  @JsonIgnore
  public Optional<UsernamePasswordCredentials> getUsernamePasswordCredentials(String username) {
    return new UsernamePasswordCredentials.Builder()
      .setCredentialsProvider(credentialsProvider)
      .setQueryUser(username)
      .build();
  }

  @JsonProperty("username")
  public String getUsername() {
    if (!directCredentials) {
      return null;
    }
    return getUsernamePasswordCredentials(null)
      .map(UsernamePasswordCredentials::getUsername)
      .orElse(null);
  }

  @JsonProperty("password")
  public String getPassword() {
    if (!directCredentials) {
      return null;
    }
    return getUsernamePasswordCredentials(null)
      .map(UsernamePasswordCredentials::getPassword)
      .orElse(null);
  }

  @JsonProperty("scheme")
  public String getScheme() {
    return scheme;
  }

  @JsonProperty("hostname")
  public String getHostname() {
    return hostname;
  }

  @JsonProperty("port")
  public int getPort() {
    return port;
  }

  public Boolean isWritable() {
    return writable;
  }

  @JsonProperty("app")
  public String getApp() {
    return app;
  }

  @JsonProperty("owner")
  public String getOwner() {
    return owner;
  }

  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  @JsonProperty("cookie")
  public String getCookie() {
    return cookie;
  }

  @JsonProperty("validateCertificates")
  @JsonInclude(Include.NON_DEFAULT)
  public boolean getValidateCertificates() {
    return validateCertificates;
  }

  @JsonProperty("validateHostname")
  @JsonInclude(Include.NON_DEFAULT)
  public boolean getValidateHostname() {
    return validateHostname;
  }

  @JsonProperty("earliestTime")
  public String getEarliestTime() {
    return earliestTime;
  }

  @JsonProperty("latestTime")
  public String getLatestTime() {
    return latestTime;
  }

  @JsonProperty("reconnectRetries")
  public int getReconnectRetries() {
    return reconnectRetries != null ? reconnectRetries : DISABLED_RECONNECT_RETRIES;
  }

  @JsonProperty("writerBatchSize")
  public int getWriterBatchSize() {
    return writerBatchSize != null ? writerBatchSize : DEFAULT_WRITER_BATCH_SIZE;
  }

  @JsonProperty("maxColumns")
  public int getMaxColumns() {
    return maxColumns != null ? maxColumns : DEFAULT_MAX_READER_COLUMNS;
  }

  @JsonProperty("maxCacheSize")
  public int getMaxCacheSize() {
    return maxCacheSize != null ? maxCacheSize : DEFAULT_MAX_CACHE_SIZE;
  }

  @JsonProperty("cacheExpiration")
  public int getCacheExpiration() {
    return cacheExpiration != null ? cacheExpiration : DEFAULT_CACHE_EXPIRATION;
  }

  private static CredentialsProvider getCredentialsProvider(CredentialsProvider credentialsProvider) {
    return credentialsProvider != null ? credentialsProvider : PlainCredentialsProvider.EMPTY_CREDENTIALS_PROVIDER;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    } else if (that == null || getClass() != that.getClass()) {
      return false;
    }
    SplunkPluginConfig thatConfig = (SplunkPluginConfig) that;
    return Objects.equals(credentialsProvider, thatConfig.credentialsProvider) &&
      Objects.equals(scheme, thatConfig.scheme) &&
      Objects.equals(hostname, thatConfig.hostname) &&
      Objects.equals(port, thatConfig.port) &&
      Objects.equals(app, thatConfig.app) &&
      Objects.equals(writable, thatConfig.writable) &&
      Objects.equals(owner, thatConfig.owner) &&
      Objects.equals(token, thatConfig.token) &&
      Objects.equals(cookie, thatConfig.cookie) &&
      Objects.equals(validateCertificates, thatConfig.validateCertificates) &&
      Objects.equals(validateHostname, thatConfig.validateHostname) &&
      Objects.equals(earliestTime, thatConfig.earliestTime) &&
      Objects.equals(latestTime, thatConfig.latestTime) &&
      Objects.equals(authMode, thatConfig.authMode) &&
        Objects.equals(maxCacheSize, thatConfig.maxCacheSize) &&
        Objects.equals(maxColumns, thatConfig.maxColumns) &&
        Objects.equals(cacheExpiration, thatConfig.cacheExpiration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        credentialsProvider,
        scheme,
        hostname,
        port,
        app,
        owner,
        token,
        cookie,
        writable,
        validateCertificates,
        validateHostname,
        earliestTime,
        latestTime,
        authMode,
        cacheExpiration,
        maxCacheSize,
        maxColumns
    );
  }

  @Override
  public String toString() {
    return new PlanStringBuilder(this)
      .field("credentialsProvider", credentialsProvider)
      .field("scheme", scheme)
      .field("hostname", hostname)
      .field("port", port)
      .field("writable", writable)
      .field("app", app)
      .field("owner", owner)
      .field("token", token)
      .field("cookie", cookie)
      .field("validateCertificates", validateCertificates)
      .field("validateHostname", validateHostname)
      .field("earliestTime", earliestTime)
      .field("latestTime", latestTime)
      .field("Authentication Mode", authMode)
      .field("maxColumns", maxColumns)
      .field("maxCacheSize", maxCacheSize)
      .field("cacheExpiration", cacheExpiration)
      .toString();
  }

  @Override
  public SplunkPluginConfig updateCredentialProvider(CredentialsProvider credentialsProvider) {
    return new SplunkPluginConfig(this, credentialsProvider);
  }
}

package com.mulesoft.modules.okta.config;

import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.rest.RestHeaderParam;
import org.mule.api.annotations.rest.RestUriParam;

@Configuration(friendlyName = "Configuration")
public class OktaConnectorConfig {
    /**
     * API Token
     */
    @Configurable
    @RestHeaderParam("Authorization")
    private String apiToken;

    /**
     * Okta host name
     */
    @Configurable
    @RestUriParam("host")
    private String host;

    /**
     * Okta API Version
     */
    @Configurable
    @RestUriParam("version")
    @Default("v1")
    private String version;

    public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = "SSWS " + apiToken;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
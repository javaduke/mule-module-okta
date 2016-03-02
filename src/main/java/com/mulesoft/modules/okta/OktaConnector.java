/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.modules.okta;
import java.io.IOException;

import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.rest.HttpMethod;
import org.mule.api.annotations.rest.RestCall;
import org.mule.api.annotations.rest.RestExceptionOn;
import org.mule.api.annotations.rest.RestQueryParam;
import org.mule.api.annotations.rest.RestUriParam;

import com.mulesoft.modules.okta.config.OktaConnectorConfig;

/**
 * Okta Anypoint Connector
 * @see http://developer.okta.com/docs/api
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="okta", schemaVersion="1.0", friendlyName="Okta", minMuleVersion="3.5")
public abstract class OktaConnector
{

	@Config
	OktaConnectorConfig config;

    /**
     * Create new user
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:create-user}
     *
     * @param profile Profile attributes for user
     * @param activate Executes activation lifecycle operation when creating the user
     * @return User profile
     * @exception IOException if the call fails
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String createUser(@Default("#[message.payload]") String profile,
    								  @RestQueryParam("activate") @Default("false") boolean activate) throws IOException;

    /**
     * Get user
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:get-user}
     *
     * @param id User ID, login, or login shortname (as long as it is unambiguous)
	 *
     * @return User profile
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}",
    		  method=HttpMethod.GET, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String getUser(@RestUriParam("id") String id) throws IOException;

    /**
     * List users
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:list-users}
     *
     * @param query Searches firstName, lastName, and email attributes of users for matching value
     * @param limit Specified the number of results
     * @param filter Filter expression for users
     * @param after Specifies the pagination cursor for the next page of users
	 *
     * @return Array of User objects
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users",
    		  method=HttpMethod.GET, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String listUsers(@RestQueryParam("q") @Optional String query,
    								 @RestQueryParam("limit") @Default("10000") int limit,
    								 @RestQueryParam("filter") @Optional String filter,
    								 @RestQueryParam("after") @Optional String after) throws IOException;
    
    /**
     * Update a user profile and/or credentials.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:update-user}
     *
     * @param profile Updated profile
	 * @param id user ID
     * @return User profile
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}",
    		  method=HttpMethod.PUT, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String updateUser(@Default("#[message.payload]") String profile,
    								  @RestUriParam("id") String id) throws IOException;

    /**
     * Fetches appLinks for all direct or indirect (via group membership) assigned applications
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:get-user-app-links}
     *
     * @param id user id
	 *
     * @return Array of App Links
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/appLinks",
    		  method=HttpMethod.GET, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String getUserAppLinks(@RestUriParam("id") String id) throws IOException;
    
    /**
     * Fetches the groups of which the user is a member.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:get-user-groups}
     *
     * @param id user id
	 *
     * @return Array of Groups
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/groups",
    		  method=HttpMethod.GET, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String getUserGroups(@RestUriParam("id") String id) throws IOException;   
    
    /**
     * Activates a user. This operation can only be performed on users with a STAGED status. 
     * Activation of a user is an asynchronous operation. The user will have the transitioningToStatus property 
     * with a value of ACTIVE during activation to indicate that the user hasn't completed the asynchronous operation. 
     * The user will have a status of ACTIVE when the activation process is complete.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:activate-user}
     *
     * @param id User ID
     * @param sendEmail Sends an activation email to the user if true
	 *
     * @return empty object by default. When sendEmail is false, returns an activation link for the user to set up their account.
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/lifecycle/activate",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String activateUser(@RestUriParam("id") String id, @RestQueryParam("sendEmail") @Default("true") boolean sendEmail) throws IOException;
    
    /**
     * Deactivates a user. This operation can only be performed on users that do not have a DEPROVISIONED status. 
     * Deactivation of a user is an asynchronous operation. The user will have the transitioningToStatus property 
     * with a value of DEPROVISIONED during deactivation to indicate that the user hasn't completed the asynchronous 
     * operation. The user will have a status of DEPROVISIONED when the deactivation process is complete.
     * 
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:deactivate-user}
     *
     * @param id User ID
	 *
     * @return empty object.
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/lifecycle/deactivate",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String deactivateUser(@RestUriParam("id") String id) throws IOException;
    
    /**
     * Unlocks a user with a LOCKED_OUT status and returns them to ACTIVE status. 
     * Users will be able to login with their current password.
     * 
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:unlock-user}
     *
     * @param id User ID
	 *
     * @return empty object.
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/lifecycle/unlock",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String unlockUser(@RestUriParam("id") String id) throws IOException;
      
    /**
     * Generates a one-time token (OTT) that can be used to reset a user's password. 
     * The OTT link can be automatically emailed to the user or returned to the API caller and distributed using a custom flow.
     * This operation will transition the user to the status of RECOVERY and the user 
     * will not be able to login or initiate a forgot password flow until they complete the reset flow.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:reset-password}
     *
     * @param id User ID
     * @param sendEmail Sends an activation email to the user if true
	 *
     * @return empty object by default. When sendEmail is false, returns a link for the user to reset their password.
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/lifecycle/reset_password",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String resetPassword(@RestUriParam("id") String id, @RestQueryParam("sendEmail") @Default("true") boolean sendEmail) throws IOException;
    
    /**
     * This operation will transition the user to the status of PASSWORD_EXPIRED and the user will be required to 
     * change their password at their next login. If tempPassword is passed, the user's password is reset to a temporary 
     * password that is returned, and then the temporary password is expired.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:expire-password}
     *
     * @param id User ID
     * @param tempPassword Sets the user's password to a temporary password, if true
	 *
     * @return the complete user object by default. When tempPassword is true, returns the temporary password.
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/lifecycle/expire_password",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String expirePassword(@RestUriParam("id") String id, @RestQueryParam("tempPassword") @Default("false") boolean tempPassword) throws IOException;
    
    /**
     * Generates a one-time token (OTT) that can be used to reset a user's password. 
     * The user will be required to validate their security question's answer when visiting the reset link. 
     * This operation can only be performed on users with a valid recovery question credential and have an 
     * ACTIVE status.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:forgot-password}
     *
     * @param id User ID
     * @param sendEmail Sends a forgot password email to the user if true
	 *
     * @return empty object by default. When sendEmail is false, returns a link for the user to reset their password.
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/lifecycle/forgot_password",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String forgotPassword(@RestUriParam("id") String id, @RestQueryParam("sendEmail") @Default("true") boolean sendEmail) throws IOException;
    
    /**
     * Sets a new password for a user by validating the user's answer to their current recovery question. 
     * This operation can only be performed on users with a valid recovery question credential and have 
     * an ACTIVE status.
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:password-recovery}
     *
     * @param id User ID
     * @param credentials New password for user - must include answer to the recovery question, e.g.
     * {
     *     "password": { "value": "MyN3wP@55w0rd" }, 
     *     "recovery_question": { "answer": "Cowboy Dan" } 
     * }
	 *
     * @return Credentials of the user
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/credentials/forgot_password",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String passwordRecovery(@RestUriParam("id") String id, @Default("#[message.payload]") String credentials) throws IOException;
    
    /**
     * Changes a user's password by validating the user's current password. 
     * This operation can only be performed on users in STAGED, ACTIVE or RECOVERY status 
     * that have a valid password credential
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:change-password}
     *
     * @param id User ID
     * @param passwords Old and new passwords, e.g.:
     * {
     *     "oldPassword": { "value": "GoAw@y123" },
     *     "newPassword": { "value": "MyN3wP@55w0rd" } 
     * }	
	 *
     * @return Credentials of the user
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/credentials/change_password",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String changePassword(@RestUriParam("id") String id, @Default("#[message.payload]") String passwords) throws IOException;
    
    /**
     * Changes a user's recovery question & answer credential by validating the user's current password. 
     * This operation can only be performed on users in STAGED, ACTIVE or RECOVERY status that have a valid 
     * password credential
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:change-recovery-question}
     *
     * @param id User ID
     * @param credentials Password and new recovery q&a, e.g.:
     * {
     *     "password": { "value": "GoAw@y123" }, 
     *     "recovery_question": {
     *       "question" : "What happens when I update my question?",
     *       "answer": "My recovery credentials are updated" 
     *     } 
     * }	
	 *
     * @return Credentials of the user
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/users/{id}/credentials/change_recovery_question",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String changeRecoveryQuestion(@RestUriParam("id") String id, @Default("#[message.payload]") String credentials) throws IOException;

    //========================================================================
    
	/**
	 * Primary authentication with username/password credentials
	 *
	 * {@sample.xml ../../../doc/okta-connector.xml.sample okta:authenticate}
	 * 
	 * @see http://developer.okta.com/docs/api/resources/authn.html#primary-authentication
	 * 
	 * @param credentials The authentication object in JSON format, e.g." 
	 * 		{ 
	 * 			"username" : "dade.murphy@example.com", 
	 * 			"password" : "tlpWENT2m",
	 *          "relayState": "/myapp/some/deep/link/i/want/to/return/to",
	 *          "context": { 
	 *          	"ipAddress": "192.168.12.11", 
	 *          	"userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3)",
	 *            	"deviceToken": "26q43Ak9Eh04p7H6Nnx0m69JqYOrfVBY" 
	 *          }
	 *      }   
	 *
	 * @return authentication object: 
	 * 		{
  	 * 			"expiresAt": "2014-11-03T10:15:57.000Z",
  	 *   		"status": "SUCCESS",
  	 *   		"relayState": "/myapp/some/deep/link/i/want/to/return/to",
  	 *   		"sessionToken": "00Fpzf4en68pCXTsMjcX8JPMctzN2Wiw4LDOBL_9pe",
  	 *   		"_embedded": {
  	 *     			"user": {
  	 *       			"id": "00ub0oNGTSWTBKOLGLNR",
  	 *       			"profile": {
  	 *         				"login": "dade.murphy@example.com",
  	 *         				"firstName": "Dade",
  	 *         				"lastName": "Murphy",
  	 *         				"locale": "en_US",
  	 *        	 			"timeZone": "America/Los_Angeles"
  	 *      	 		}
  	 *     			}
  	 *   		}
  	 * 		}
	 * @throws IOException
	 */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/sessions",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")} )
    public abstract String authenticate(@Default("#[message.payload]") String credentials) throws IOException;
 
    
    /*


Change Expired Password
POST /authn/credentials/change_password

Enroll Factor
POST /authn/factor

Activate Factor
POST /authn/factors/:fid/lifecycle/activate

Verify Factor
POST /authn/factors/:fid/verify
     */
    
    //========================================================================
    
    
    /**
     * Creates a new session for a user with a valid session token. 
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:create-session}
     * @see http://developer.okta.com/docs/api/resources/sessions.html#create-session-with-session-token
     * 
     * @param sessionToken The valid Session token (obtained by authenticating) in JSON format, e.g.:
	 *		{
     *			"sessionToken": "00HiohZYpJgMSHwmL9TQy7RRzuY-q9soKp1SPmYYow"
   	 *		}	
   	 * @param additionalFields Optional session properties, comma-separated
   	 * @return session and user IDs:
   	 * 		{
     *			"id": "101yT5gGJ3KRQyXKWqNMUsbqw",
     *			"userId": "00u57id14rji6Kmui0h7",
     *			"mfaActive": false
   	 * 		}
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/sessions",
    		  method=HttpMethod.POST, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")} )
    public abstract String createSession(@Default("#[message.payload]") String sessionToken, @Optional @RestQueryParam("additionalFields")  String additionalFields) throws IOException;
    
    /**
     * Validate/extend an existing session. This method can be used instead of GET /sessions/{id} because it both validates the session and extends its lifetime. 
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:extend-session}
     * @see http://developer.okta.com/docs/api/resources/sessions.html#extend-session
     * 
     * @param sessionToken The valid Session token (obtained by authenticating)
	 *	
   	 * @return session and user IDs:
   	 * 		{
     *			"id": "101yT5gGJ3KRQyXKWqNMUsbqw",
     *			"userId": "00u57id14rji6Kmui0h7",
     *			"mfaActive": false
   	 * 		}
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/sessions/{sessionId}",
    		  method=HttpMethod.PUT, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String extendSession(@Default("#[message.payload]") @RestUriParam("sessionId") String sessionId) throws IOException;
    
    /**
     * Closes a user's session (logout).
     *
     * {@sample.xml ../../../doc/okta-connector.xml.sample okta:close-session}
     * @see http://developer.okta.com/docs/api/resources/sessions.html#close-session
     * 
     * @param sessionToken The valid Session token (obtained by authenticating)
     * 
   	 * @return session and user IDs:
   	 * 		{
     *			"id": "101yT5gGJ3KRQyXKWqNMUsbqw",
     *			"userId": "00u57id14rji6Kmui0h7",
     *			"mfaActive": false
   	 * 		}
     * @throws IOException
     */
    @Processor
    @RestCall(uri="https://{host}/api/{version}/sessions/{sessionId}",
    		  method=HttpMethod.PUT, 
    		  contentType="application/json", 
    		  exceptions={@RestExceptionOn(expression="#[message.inboundProperties['http.status'] > 206]")})
    public abstract String closeSession(@Default("#[message.payload]") @RestUriParam("sessionId") String sessionId) throws IOException;
   
    //========================================================================
 
    public OktaConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(OktaConnectorConfig config) {
        this.config = config;
    }

}
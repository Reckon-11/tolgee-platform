package io.tolgee.ee.api.v2.controllers

import io.tolgee.constants.Message
import io.tolgee.ee.data.DomainRequest
import io.tolgee.ee.data.SsoUrlResponse
import io.tolgee.ee.exceptions.OAuthAuthorizationException
import io.tolgee.ee.model.SsoTenant
import io.tolgee.ee.service.OAuthService
import io.tolgee.ee.service.TenantService
import io.tolgee.security.payload.JwtAuthenticationResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v2/public/oauth2/callback/")
class OAuth2CallbackController(
  private val oauthService: OAuthService,
  private val tenantService: TenantService,
) {
  @PostMapping("/get-authentication-url")
  fun getAuthenticationUrl(
    @RequestBody request: DomainRequest,
  ): SsoUrlResponse {
    val registrationId = request.domain
    val tenant = tenantService.getByDomain(registrationId)
    if (!tenant.isEnabledForThisOrganization) {
      throw OAuthAuthorizationException(Message.SSO_DOMAIN_NOT_ENABLED, "Domain is not enabled for this organization")
    }
    val redirectUrl = buildAuthUrl(tenant, state = request.state)

    return SsoUrlResponse(redirectUrl)
  }

  private fun buildAuthUrl(
    tenant: SsoTenant,
    state: String,
  ): String =
    "${tenant.authorizationUri}?" +
      "client_id=${tenant.clientId}&" +
      "redirect_uri=${tenant.redirectUriBase + "/openId/auth-callback/" + tenant.domain}&" +
      "response_type=code&" +
      "scope=openid profile email roles&" +
      "state=$state"

  @GetMapping("/{registrationId}")
  fun handleCallback(
    @RequestParam(value = "code", required = true) code: String,
    @RequestParam(value = "redirect_uri", required = true) redirectUrl: String,
    @RequestParam(defaultValue = "") error: String,
    @RequestParam(defaultValue = "") error_description: String,
    @RequestParam(value = "invitationCode", required = false) invitationCode: String?,
    response: HttpServletResponse,
    @PathVariable registrationId: String,
  ): JwtAuthenticationResponse? =
    oauthService.handleOAuthCallback(
      registrationId = registrationId,
      code = code,
      redirectUrl = redirectUrl,
      error = error,
      errorDescription = error_description,
      invitationCode = invitationCode,
    )
}
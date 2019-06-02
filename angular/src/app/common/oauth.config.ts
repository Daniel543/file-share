import { AuthConfig } from 'angular-oauth2-oidc';

export const oauthConfig: AuthConfig = {

  loginUrl: 'http://localhost:8080/oauth/authorize',
  redirectUri: window.location.origin + '/',
  clientId: 'fileshare-angular',
  scope: 'read write',
  showDebugInformation: true,
  requireHttps:false,
  logoutUrl: window.location.origin + '/logout',
  oidc: false
}

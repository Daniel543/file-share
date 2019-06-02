import { Injectable } from '@angular/core';
import {
  JwksValidationHandler,
  OAuthService
} from 'angular-oauth2-oidc';
import {oauthConfig} from './oauth.config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(oauthConfig);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.setStorage(sessionStorage);
    this.oauthService.tryLogin({});
  }
  public login() {
    this.oauthService.initImplicitFlow();
  }

  public logout() {
    this.oauthService.logOut();
  }
  isLoggedIn() {
    if (this.oauthService.getAccessToken() === null) {
      return false;
    }
    return true;
  }
  getAccessToken(): string {
    return this.oauthService.getAccessToken();
  }
  loadUserDetails() {
    this.oauthService.loadUserProfile();
  }
}

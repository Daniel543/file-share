import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import {OAuthService} from 'angular-oauth2-oidc';
import { Observable } from 'rxjs';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private oauthService: OAuthService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
   // if (window.sessionStorage.getItem('token')) {
      request = request.clone({
        setHeaders: {
          Authorization: "Bearer " + this.oauthService.getAccessToken()
        }
      });
  //  }
    return next.handle(request);
  }
}

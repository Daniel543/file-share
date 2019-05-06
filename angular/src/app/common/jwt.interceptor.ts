import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor() { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (window.sessionStorage.getItem('token')) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${JSON.parse(window.sessionStorage.getItem('token')).access_token}`
        }
      });
    }
    return next.handle(request);
  }
}

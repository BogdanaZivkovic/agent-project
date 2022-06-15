import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../service/user.service';

@Injectable()
export class Interceptor implements HttpInterceptor {
  constructor(private userService : UserService) { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.userService.user.username != "") {
        let authorization = this.userService.user.username;
        request = request.clone({
        setHeaders: {
          Authorization: `${authorization}`
        }
      });
    }
    return next.handle(request);
  }
}
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/empty';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Platform } from 'ionic-angular';
@Injectable()
export class DefaultHttpInterceptor implements HttpInterceptor {
	constructor(private platform: Platform) { }
	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		let skip = false;
		if (req.body instanceof FormData) {
			skip = true;
		}
		let authReq: HttpRequest<any>;
		if (skip) {
			authReq = req.clone({
				withCredentials: false,
				setHeaders: {
					'Authorization': localStorage.getItem('auth_token'),
					'platform': this.platform.platforms()
				}
			});
		} else {
			authReq = req.clone({
				withCredentials: false,
				setHeaders: {
					'Content-Type': 'application/json',
					'Authorization': localStorage.getItem('auth_token') + '',
					'platform': this.platform.platforms()
				}
			});
		}
		return next.handle(authReq);
	}
}
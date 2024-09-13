import {Injectable} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class DefaultHttpInterceptor implements HttpInterceptor {

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		const authReq = req.clone({
			withCredentials: true,
			setHeaders: {
				'Content-Type': 'application/json'
			}
		});
		return next.handle(authReq);
	}
}
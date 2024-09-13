import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Config} from './env.config';
import {catchError, last, map, tap} from 'rxjs/operators';
import {BaseService} from '../shared/base.service';
@Injectable()
export class PrvAuth extends BaseService {
	constructor(
		private http: HttpClient) {
		super();
	}

	login(username, password) {
		const credentials = JSON.stringify({username, password})		;
//		return this.http.post(Config.API + 'auth/login', body);

		return this.http.post(
			Config.API + 'auth/login',
			credentials
		).pipe(
			tap((res: any) => {
				if (res.username) {
					localStorage.setItem('auth_info', JSON.stringify(res));
				}

				return res.username;
			}), catchError(this.handleError)
		);

	}

	//	private handleError(error: Response | any) {
	//		return observableThrowError(error || 'Server error');
	//	}

	register(dataUser: any) {
		let body = JSON.stringify({'txtNama': dataUser.namaLengkap, 'txtUsername': dataUser.username, 'stremail': dataUser.email, 'txtPass1': dataUser.password});
		return this.http.post(Config.API + 'CustomerRegister', body);
	}
}
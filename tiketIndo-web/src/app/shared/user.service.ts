import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BaseService} from './base.service';
import {catchError, tap} from 'rxjs/operators';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class UserService extends BaseService {
	private _userUrl = this.apiUrl + 'user';
	redirectUrl: string;
	constructor(private http: HttpClient) {
		super();
	}

	findAll(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'user?start=' + start + '&max=' + max, JSON.stringify(tq));
	}

	exportData(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'user/xls?start=' + start + '&max=' + max, JSON.stringify(tq), {
			responseType: 'blob'
		});
	}


	deleteRecord(id) {
		return this.http.delete(this.apiUrl + 'user/' + id);
	}

	findById(id: number) {
		return this.http.get(this.apiUrl + 'user/byId/' + id);
	}

	getMyProfile() {
		return this.http.get(this.apiUrl + 'user/myProfile');
	}

	getRoles() {
		return this.http.get(this._userUrl + '/roles');
	}

	create(model: any): Observable<any> {
		return this.http.post(this._userUrl + '/new', JSON.stringify(model));
	}

	update(model: any): Observable<any> {
		return this.http.put(this._userUrl + '/update', JSON.stringify(model));
	}

	deleteUser(userId: number) {
		return this.http.post(this._userUrl + '/del/' + userId, '{}');
	}

	login(username, password) {
		let credentials = JSON.stringify({username, password});

		return this.http.post(
			this.apiUrl + 'auth/login',
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

	logout() {
		localStorage.removeItem('auth_info');
		let httpOptions = {
			headers: new HttpHeaders({'Content-Type': 'application/json'})
		};
		this.http
			.post(
				this.apiUrl + 'auth/logout',
				'', httpOptions
			).subscribe();
	}


	isLoggedIn() {
		if (localStorage.getItem('auth_info')) {
			return true;
		}
		return false;
	}

	getUserByRole(role: string) {
		return this.http.get(this._userUrl + '/byRole/' + role);
	}

}

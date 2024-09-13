import {Injectable} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {Config} from '../shared/index';
import {Observable} from 'rxjs';
import {BaseService} from './base.service';

@Injectable()
export class SrvMasterData extends BaseService {
	constructor(private http: HttpClient) {
		super();
	}

	getAuthInfo() {
		return JSON.parse(localStorage.getItem('auth_info'));
	}
	getUsername() {
		let authInfo = this.getAuthInfo();
		if (authInfo) {
			return authInfo.username;
		}
		return 'unknown';
	}
	getName() {
		let authInfo = this.getAuthInfo();
		if (authInfo) {
			return authInfo.nama;
		}
		return 'unknown';
	}
	getRole() {
		let authInfo = this.getAuthInfo();
		if (authInfo) {
			return authInfo.role;
		}
		return 'unknown';
	}
	getDefaultYear(prefix: string, routeParams: ActivatedRoute, paramName: string): number {
		let value = this.getDefaultParameter(prefix, routeParams, paramName);
		if (value) {
			return +value;
		}
		return this.getCurrentYear();
	}
	getDefaultParameter(prefix: string, route: ActivatedRoute, paramName: string): string {
		let result = '';
		if (route) {
			let params: any = route.params;
			let result = params.value[paramName];
			if (result && result !== 'null') {
				this.saveDefaultParameter(prefix, paramName, result);
				return result;
			}
		}
		let cacheName = prefix + '_' + paramName;
		result = localStorage.getItem(cacheName);
		if (result === 'null' || result == undefined) {
			result = "";
		}
		return result;
	}
	getCurrentYear() {
		var today = new Date();
		var yyyy = today.getFullYear();
		return yyyy;
	}
	getCurrentMonth() {
		var today = new Date();
		var MM = today.getMonth();
		return MM;
	}
	saveDefaultParameter(prefix: string, paramName: string, value: string) {
		let cacheName = prefix + '_' + paramName;
		localStorage.setItem(cacheName, value);
	}
	getUserId(): number {
		let authInfo = this.getAuthInfo();
		if (authInfo) {
			return +authInfo.id;
		}
		return 0;
	}

	saveAuthInfo(authInfo: any) {
		localStorage.setItem('auth_info', JSON.stringify(authInfo));
	}

	saveUserSetting(category: string, name: string, value: string): Observable<any> {
		let model = {category: category, name: name, value: value};
		return this.http.post(Config.API + 'user/setting', JSON.stringify(model));
	}

	getUserSettings(category: string): Observable<any> {
		return this.http.get(Config.API + 'user/setting/' + category);
	}

	deleteUserSetting(category: string, name: string): Observable<any> {
		let model = {category: category, name: name};
		return this.http.post(Config.API + 'user/setting/del', JSON.stringify(model));
	}

	isTestMode() {
		return this.getAuthInfo().testMode;
	}

	hasRole(role: string) {
		let authInfo = this.getAuthInfo();
		if (authInfo) {
			return role === authInfo.role;
		}
		return false;
	}

	isAdmin() {
		return this.hasRole('admin');
	}

	hasPermission(permission: string) {
		let authInfo = this.getAuthInfo();
		if (authInfo) {
			let permissionList: string[] = authInfo.permissions;
			let result = permissionList.indexOf(permission) > -1
			return result;
		}
		return false;
	}

	checkAuth() {
		return this.http.get(this.apiUrl + 'auth/check');
	}
	
	errorNoParam() {
		return this.http.get(this.apiUrl + 'city/errornoparam');
	}
	errorWithParam() {
		return this.http.get(this.apiUrl + 'city/errorwithparam');
	}
}
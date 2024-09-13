//import {OnDestroy} from '@angular/core';
//import {PanictUtil} from '../shared/panict.util';
//import {HttpErrorResponse} from '@angular/common/http';
//import {Router} from '@angular/router';
//import {ActivatedRoute} from '@angular/router';
//import {Observable} from 'rxjs';
//
//export class BaseComponent implements OnDestroy {
//	protected PanictUtil = PanictUtil;
//
//	ngOnDestroy() {
//		PanictUtil.hideRequestIndicator();
//		PanictUtil.hideAlert();
//	}
//	protected handleErrorReturnObservable(error, router: Router) {
//		this.handleError(error, router);
//		return Observable.create(observer => null);
//	}
//	protected handleError(error, router: Router) {
//		this.PanictUtil.hideRequestIndicator();
//
//		let errMsg: string;
//		if (error instanceof HttpErrorResponse) {
//			try {
//				const body = error.error || '';
//				if (error.status === 401) {
//					PanictUtil.toLoginPage(router);
//					return;
//				} else if (error.error) {
//					errMsg = 'Error ' + error.status + '. ' + error.error['message'];
//				} else if (error.status === 403 || error.status === 400) {
//					const err = body.error || JSON.stringify(body);
//					errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
//				} else if (error.status > 0) {
//					const err = body.error || JSON.stringify(body);
//					errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
//				} else {
//					console.error(error);
//					errMsg = 'Unknown error. Please check server log.';
//				}
//			} catch (e) {
//				const body = error.error || '';
//				if (error.status > 0) {
//					errMsg = body;
//				} else {
//					console.error(error);
//				}
//			}
//		} else {
//			errMsg = error.message ? error.message : error.toString();
//		}
//		PanictUtil.showAlertDanger(errMsg);
//	}
//
//
//	protected getDefaultParameter(route: ActivatedRoute, paramName: string) {
//		return this.PanictUtil.getDefaultParameter(route, paramName);
//	}
//
//	protected emptyStringIfUndefined(param: any): string {
//		return param === undefined || param == null || param === 'null' ? '' : param;
//	}
//
//	protected defaultIfUndefined(param: any, def: string): string {
//		return param === undefined || param == null || param === 'null' ? def : param;
//	}
//
//	protected zeroIfUndefined(param: any): number {
//		return param === undefined ? 0 : +param;
//	}
//
//	protected booleanIfUndefined(param: any): boolean {
//		if (param === 'false') {
//			return false;
//		} else if (param === 'true') {
//			return true;
//		}
//		return param === undefined ? false : param;
//	}
//	
//	hasPermission(permission: string) {
//		let authInfo = JSON.parse(localStorage.getItem('auth_info'));
//		let idx = authInfo.permissions.indexOf(permission);
//		return idx >= 0;
//	}
//}

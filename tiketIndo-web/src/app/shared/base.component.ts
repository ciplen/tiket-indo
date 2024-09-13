import {OnDestroy} from '@angular/core';
import {PanictUtil} from '../shared/panict.util';
import {Router} from '@angular/router';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {TranslateService} from '@ngx-translate/core';

declare var jQuery: any;

export class BaseComponent implements OnDestroy {
	protected PanictUtil = PanictUtil;
	constructor(protected translate: TranslateService) {}

	ngOnDestroy() {
		PanictUtil.hideRequestIndicator();
		PanictUtil.hideAlert();
	}
	protected handleErrorReturnObservable(error, router: Router) {
		this.handleError(error, router);
		return Observable.create(observer => null);
	}

	protected handleError(error, router: Router) {
		console.error(`Backend returned error ${JSON.stringify(error)}`);
		this.PanictUtil.hideRequestIndicator();
		let subTitle = '';
		let title = error.status;
		let body = null;
		if (error.error) {
			//this is error from http	
			body = error.error;
			if (body instanceof Blob) {
				let reader = new FileReader();
				reader.addEventListener("loadend", (e) => {
					error.error = JSON.parse(reader.result + '');
					this.handleError(error, router);
				});
				reader.readAsText(body);
				return;
			}
		} else if (error.body) {
			//this is error from ionic filetransfer	
			body = JSON.parse(error.body);
			title = error.http_status;
		}


		if (body && body.code) {
			title = body.code;

			let paramObject = {};
			if (body.parameters) {
				let params: any[] = body.parameters;
				params.forEach((val, idx) => paramObject['par' + idx] = val);
			}

			subTitle = this.translate.instant(body.code, paramObject);
			if (subTitle == body.code) {
				subTitle = body.message;
			}
		}
		if (error.status === 401) {
			PanictUtil.toLoginPage(router);
			return;
		}
		PanictUtil.showAlertDanger(subTitle);
	}


	protected getDefaultParameter(route: ActivatedRoute, paramName: string) {
		return this.PanictUtil.getDefaultParameter(route, paramName);
	}

	protected emptyStringIfUndefined(param: any): string {
		return param === undefined || param == null || param === 'null' ? '' : param;
	}

	protected defaultIfUndefined(param: any, def: string): string {
		return param === undefined || param == null || param === 'null' ? def : param;
	}

	protected zeroIfUndefined(param: any): number {
		return param === undefined ? 0 : +param;
	}

	protected booleanIfUndefined(param: any): boolean {
		if (param === 'false') {
			return false;
		} else if (param === 'true') {
			return true;
		}
		return param === undefined ? false : param;
	}

	// hasPermission(permission: string) {
	// 	let authInfo = JSON.parse(localStorage.getItem('auth_info'));
	// 	let idx = authInfo.permissions.indexOf(permission);
	// 	return idx >= 0;
	// }
	
	expandCollapse(selector:string) {
		jQuery(selector).collapse('toggle');
	}
	
	expandPanel(selector:string) {
		jQuery(selector).collapse('show');
	}
	collapsePanel(selector:string) {
		jQuery(selector).collapse('hide');
	}
}

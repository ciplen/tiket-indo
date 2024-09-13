import {Observable} from 'rxjs';
import {Alert} from './alert.component';
import {Router} from '@angular/router';
import {ActivatedRoute} from '@angular/router';

export class PanictUtil {

	private static alertObserver: any;
	private static alertObservable = Observable.create(observer => {
		PanictUtil.alertObserver = observer;
	});

	private static requestObserver: any;
	private static requestObservable = Observable.create(observer => {
		PanictUtil.requestObserver = observer;
	});


	static getAlertObservable(): Observable<Alert> {
		return PanictUtil.alertObservable;
	}

	static showAlertSuccess(message: string) {
		let obj = new Alert(message, 'success');
		PanictUtil.alertObserver.next(obj);
	}

	static showAlertInfo(message: string) {
		let obj = new Alert(message, 'info');
		PanictUtil.alertObserver.next(obj);
	}

	static showAlertDanger(message: string) {
		let obj = new Alert(message, 'danger');
		PanictUtil.alertObserver.next(obj);
	}

	static showAlertWarning(message: string) {
		let obj = new Alert(message, 'warning');
		PanictUtil.alertObserver.next(obj);
	}

	static hideAlert() {
		if (PanictUtil.alertObserver) {
			PanictUtil.alertObserver.next(null);
		}
	}

	static showRequestIndicator() {
		PanictUtil.requestObserver.next(true);
	}

	static hideRequestIndicator() {
		if (PanictUtil.requestObserver) {
			PanictUtil.requestObserver.next(false);
		}
	}

	static getRequestObservable(): Observable<boolean> {
		return PanictUtil.requestObservable;
	}

	static toLoginPage(router: Router, withOrigin: boolean = true) {
		localStorage.removeItem('auth_info');
		if (withOrigin) {
			router.navigate(['/login', {origin: encodeURIComponent(router.url)}]);
		} else {
			router.navigate(['/login', {origin: encodeURIComponent('/home')}]);
		}
	}

	static getDefaultParameter(route: ActivatedRoute, paramName: string): string {
		let params: any = route.params;
		let result = params.value[paramName];
		if (result) {
			this.saveDefaultParameter(route, paramName, result);
			return result;
		}
		let component: any = route.component;
		let path = component.name;
		let cacheName = path + '_' + paramName;
		return localStorage.getItem(cacheName);
	}

	static saveDefaultParameter(route: ActivatedRoute, paramName: string, value: string) {
		let component: any = route.component;
		let path = component.name;
		let cacheName = path + '_' + paramName;
		localStorage.setItem(cacheName, value);
	}

	static isoDateToTanggal(stringDate: string) {
		var result = '';
		if (typeof stringDate === 'number') {
			let date = new Date(stringDate);
			stringDate = date.getFullYear() + '-' + ("0" + (date.getMonth() + 1)).slice(-2) + '-' + ("0" + date.getDate()).slice(-2);
		}
		if (stringDate != null && stringDate.length == 10) {
			result = stringDate.substr(8, 2) + '-' + stringDate.substr(5, 2) + '-' + stringDate.substr(0, 4);
		}
		return result;
	}
	static isoDateToTanggalSql(stringDate: string) {
		if (typeof stringDate === 'number') {
			let date = new Date(stringDate);
			stringDate = date.getFullYear() + '-' + ("0" + (date.getMonth() + 1)).slice(-2) + '-' + ("0" + date.getDate()).slice(-2);
		}
		return stringDate;
	}
}
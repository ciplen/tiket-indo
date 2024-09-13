//import {Observable} from 'rxjs';
//import {MessageObject} from './dialog.component';
//import {Alert} from './alert.component';
//import {Router} from '@angular/router';
//import {ActivatedRoute} from '@angular/router';
//
//export var SUB_CATEGORY = ['Vehicle', 'Electronic', 'ElectronicPro', 'Property'];
//export var SUB_COMPARISON = [
//	{label: '>=', value: 'ge'},
//	{label: '>', value: 'gt'},
//	{label: '=', value: 'eq'},
//	{label: '<', value: 'lt'},
//];
//export var RULE = [4, 8, 20, -1];
//export var RULE_FILTER = [{label: '', value: '0'}, {label: '4thn', value: '4'}, {label: '8thn', value: '8'}, {label: '20thn', value: '20'}, {label: '-1', value: '-1'}];
//export var BULAN_LABEL_VALUE = [
//	{label: '', value: '0'},
//	{label: 'Januari', value: '1', labelShort: 'Jan'},
//	{label: 'Februari', value: '2', labelShort: 'Feb'},
//	{label: 'Maret', value: '3', labelShort: 'Mar'},
//	{label: 'April', value: '4', labelShort: 'Apr'},
//	{label: 'Mei', value: '5', labelShort: 'Mei'},
//	{label: 'Juni', value: '6', labelShort: 'Jun'},
//	{label: 'Juli', value: '7', labelShort: 'Jul'},
//	{label: 'Agustus', value: '8', labelShort: 'Ags'},
//	{label: 'September', value: '9', labelShort: 'Sep'},
//	{label: 'Oktober', value: '10', labelShort: 'Okt'},
//	{label: 'November', value: '11', labelShort: 'Nov'},
//	{label: 'Desember', value: '12', labelShort: 'Des'}
//];
//
//export class PanictUtil {
//	private static dialogObserver: any;
//	private static dialogObservable = Observable.create(observer => {
//		PanictUtil.dialogObserver = observer;
//	});
//
//	private static alertObserver: any;
//	private static alertObservable = Observable.create(observer => {
//		PanictUtil.alertObserver = observer;
//	});
//
//	private static requestObserver: any;
//	private static requestObservable = Observable.create(observer => {
//		PanictUtil.requestObserver = observer;
//	});
//
//
//	static getAlertObservable(): Observable<Alert> {
//		return PanictUtil.alertObservable;
//	}
//
//	static showAlertSuccess(message: string) {
//		let obj = new Alert(message, 'success');
//		PanictUtil.alertObserver.next(obj);
//	}
//
//	static showAlertInfo(message: string) {
//		let obj = new Alert(message, 'info');
//		PanictUtil.alertObserver.next(obj);
//	}
//
//	static showAlertDanger(message: string) {
//		let obj = new Alert(message, 'danger');
//		PanictUtil.alertObserver.next(obj);
//	}
//
//	static showAlertWarning(message: string) {
//		let obj = new Alert(message, 'warning');
//		PanictUtil.alertObserver.next(obj);
//	}
//
//	static hideAlert() {
//		PanictUtil.alertObserver.next(null);
//	}
//
//	static showRequestIndicator() {
//		PanictUtil.requestObserver.next(true);
//	}
//
//	static hideRequestIndicator() {
//		PanictUtil.requestObserver.next(false);
//	}
//
//	static getRequestObservable(): Observable<boolean> {
//		return PanictUtil.requestObservable;
//	}
//
//	static showDialogInfo(htmlContent: string) {
//		let obj: MessageObject = {title: 'INFO', content: htmlContent, type: 'info', yesLabel: 'Ok'};
//		PanictUtil.dialogObserver.next(obj);
//	}
//
//	static showDialogInfo2(htmlContent: string, onClose?: any) {
//		let obj: MessageObject = {title: 'INFO', content: htmlContent, type: 'info', yesLabel: 'Ok', onClose: onClose};
//		PanictUtil.dialogObserver.next(obj);
//	}
//
//	static showDialogError(htmlContent: string) {
//		let obj: MessageObject = {title: 'ERROR', content: htmlContent, type: 'error', yesLabel: 'Ok'};
//		PanictUtil.dialogObserver.next(obj);
//	}
//
//	static showDialogConfirmation(htmlContent: string, onClose?: any) {
//		let obj: MessageObject = {title: 'KONFIRMASI', content: htmlContent, type: 'confirmation', yesLabel: 'Ya', noLabel: 'Tidak', onClose: onClose};
//		PanictUtil.dialogObserver.next(obj);
//	}
//
//	static showDialog(title: string, htmlContent: string, type: string, yesLabel: string, noLabel: string, cancelLabel: string, onClose: any) {
//		let obj: MessageObject = {
//			title: title,
//			content: htmlContent,
//			type: type,
//			yesLabel: yesLabel,
//			noLabel: noLabel,
//			cancelLabel: cancelLabel,
//			onClose: onClose
//		};
//		PanictUtil.dialogObserver.next(obj);
//	}
//
//	static hideDialog() {
//		PanictUtil.dialogObserver.next(null);
//	}
//
//	static getDialogObservable(): Observable<MessageObject> {
//		return PanictUtil.dialogObservable;
//	}
//
//	static toLoginPage(router: Router, withOrigin: boolean = true) {
//		localStorage.removeItem('auth_info');
//		if (withOrigin) {
//			router.navigate(['/login', {origin: encodeURIComponent(router.url)}]);
//		} else {
//			router.navigate(['/login', {origin: encodeURIComponent('/home')}]);
//		}
//	}
//
//	static getDefaultParameter(route: ActivatedRoute, paramName: string): string {
//		let params: any = route.params;
//		let result = params.value[paramName];
//		if (result) {
//			this.saveDefaultParameter(route, paramName, result);
//			return result;
//		}
//		let component: any = route.component;
//		let path = component.name;
//		let cacheName = path + '_' + paramName;
//		return localStorage.getItem(cacheName);
//	}
//
//	static saveDefaultParameter(route: ActivatedRoute, paramName: string, value: string) {
//		let component: any = route.component;
//		let path = component.name;
//		let cacheName = path + '_' + paramName;
//		localStorage.setItem(cacheName, value);
//	}
//
//	static isoDateToTanggal(stringDate: string) {
//		var result = '';
//		if (typeof stringDate === 'number') {
//			let date = new Date(stringDate);
//			stringDate = date.getFullYear() + '-' + ("0" + (date.getMonth() + 1)).slice(-2) + '-' + ("0" + date.getDate()).slice(-2);
//		}
//		if (stringDate != null && stringDate.length == 10) {
//			result = stringDate.substr(8, 2) + '-' + stringDate.substr(5, 2) + '-' + stringDate.substr(0, 4);
//		}
//		return result;
//	}
//	static isoDateToTanggalSql(stringDate: string) {
//		if (typeof stringDate === 'number') {
//			let date = new Date(stringDate);
//			stringDate = date.getFullYear() + '-' + ("0" + (date.getMonth() + 1)).slice(-2) + '-' + ("0" + date.getDate()).slice(-2);
//		}
//		return stringDate;
//	}
//}
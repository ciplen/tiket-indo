import { Injectable } from '@angular/core';
import { MessageDialogObject, Type } from './MessageDialogObject';
import { MatDialog } from '@angular/material';
import { CmpMessageDialog } from './CmpMessageDialog';
import { Observable, Subject, Subscriber } from 'rxjs';

@Injectable()
export class SrvUtil {
	private requestObserver: any;
	private reloadSubject = new Subject<Date>();
	private requestObservable = new Observable<boolean>(observer => {
		this.requestObserver = observer;
	});

	constructor(public dialog: MatDialog) { }

	public showDialogInfo(htmlContent: string, onClose?: Function) {
		this.showDialog(htmlContent, '', Type.INFO, 'Ok', '', '', onClose);
	}

	public showDialogError(htmlContent: string, onClose?: Function) {
		this.showDialog(htmlContent, '', Type.ERROR, 'Ok', '', '', onClose);
	}

	public showDialogConfirm(htmlContent: string, onClose: Function) {
		this.showDialog(htmlContent, '', Type.CONFIRM, 'Yes', 'No', '', onClose);
	}

	public showDialog(htmlContent: string,
		title?: string,
		type?: Type,
		yesLabel?: string,
		noLabel?: string,
		cancelLabel?: string,
		onClose?: Function) {

		if (!yesLabel) { yesLabel = 'Ok'; }
		if (!type) { type = Type.INFO; }
		if (!title) {
			if (type === Type.INFO) {
				title = 'Info';
			} else if (type === Type.ERROR) {
				title = 'Error';
			} else if (type === Type.CONFIRM) {
				title = 'Confirm';
			}
		}
		let obj: MessageDialogObject = {
			title: title,
			content: htmlContent,
			type: type,
			yesLabel: yesLabel,
			noLabel: noLabel,
			cancelLabel: cancelLabel,
			onClose: onClose
		};
		const dialogRef = this.dialog.open(CmpMessageDialog, {
			data: obj
		});

		if (onClose) {
			dialogRef.afterClosed().subscribe(result => {
				onClose(result);
			});
		}
	}

	getRequestObservable(): Observable<boolean> {
		return this.requestObservable;
	}

	showRequestIndicator() {
		this.requestObserver.next(true);
	}

	hideRequestIndicator() {
		this.requestObserver.next(false);
	}

	getReloadAccountEvent() {
		return this.reloadSubject;
	}

	reloadAccount() {
		this.reloadSubject.next(new Date);
	}

	handleError(err) {
		console.error(err);
		this.hideRequestIndicator();
		let errorMessage = '';
		if (err instanceof Error) {
			errorMessage = err.message;
		} else if (err.error) {
			errorMessage = err.error['message'];
		}
		if (!errorMessage) {
			let now = new Date();
			errorMessage = 'There was an unknown error while processing your request.'
				+ '<br><br><small>Time: ' + now + '</small>';

			if (err.url) {
				errorMessage = errorMessage + '<br><small>URL: ' + err.url + '</small>';
			}
			errorMessage = errorMessage + '<br><br>Please contact CSS support.';
		}
		this.showDialogError(errorMessage);
	}

}
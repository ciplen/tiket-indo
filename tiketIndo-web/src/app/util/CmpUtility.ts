import {Component, AfterViewInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {BaseComponent} from '../shared/base.component';
import {SrvMasterData} from '../shared/SrvMasterData';
import {Router} from '@angular/router';
import {SrvUtil} from 'app/shared/SrvUtil';
import {Type} from 'app/shared/MessageDialogObject';

@Component({
	selector: 'cmp-utility',
	templateUrl: 'CmpUtility.html',
	styleUrls: ['./CmpUtility.scss']
})

export class CmpUtility extends BaseComponent implements AfterViewInit {
	requestIndicatorClicked = false;
	alertSuccessClicked = false;
	alertDangerClicked = false;
	alertWarningClicked = false;
	alertInfoClicked = false;
	errorMsg = '';

	constructor(translate: TranslateService,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		private router: Router) {
		super(translate);
	}

	ngAfterViewInit() {
	}

	showRequestIndicator() {
		this.requestIndicatorClicked = true;
		this.PanictUtil.showRequestIndicator();
	}

	hideRequestIndicator() {
		this.requestIndicatorClicked = false;
		this.PanictUtil.hideRequestIndicator();
	}

	showAlertSuccess() {
		this.alertSuccessClicked = true;
		this.PanictUtil.showAlertSuccess(this.translate.instant('alert.success'));
	}

	hideAlertSuccess() {
		this.alertSuccessClicked = false;
		this.PanictUtil.hideAlert();
	}

	showAlertDanger() {
		this.alertDangerClicked = true;
		this.PanictUtil.showAlertDanger(this.translate.instant('alert.danger'));
	}

	hideAlertDanger() {
		this.alertDangerClicked = false;
		this.PanictUtil.hideAlert();
	}

	showAlertWarning() {
		this.alertWarningClicked = true;
		this.PanictUtil.showAlertWarning(this.translate.instant('alert.warning'));
	}

	hideAlertWarning() {
		this.alertWarningClicked = false;
		this.PanictUtil.hideAlert();
	}

	showAlertInfo() {
		this.alertInfoClicked = true;
		this.PanictUtil.showAlertInfo(this.translate.instant('alert.info'));
	}

	hideAlertInfo() {
		this.alertInfoClicked = false;
		this.PanictUtil.hideAlert();
	}

	errorNoParam() {
		this.srvMasterData.errorNoParam().subscribe(
			data => {},
			error => this.handleError(error, this.router)
		);
	}

	errorWithParam() {
		this.srvMasterData.errorWithParam().subscribe(
			data => {},
			error => this.handleError(error, this.router)
		);
	}

	showDialogInfo() {
		this.srvUtil.showDialogInfo('This is an info dialog. Please hit OK button to close it');
	}
	showDialogConfirm() {
		this.srvUtil.showDialogConfirm('This is a confirmation dialog. Please hit yes or no',
			(answer: string) => this.srvUtil.showDialogInfo('You just selected a ' + answer + ' answer'));
	}
	showDialogError() {
		this.srvUtil.showDialogError('This is an error dialog. Please hit OK button to close it');
	}

	showDialogCustom() {
		this.srvUtil.showDialog('My name is <b>Amrullah</b>! <input>', 'Custom Title', Type.INFO, 'Go Ahead', 'Go Back', 'Stay Here',
			(answer) => {
				console.log('answer: ' + answer);
				if (answer == 'yes') {
					this.srvUtil.showDialogInfo(`"Go Ahead" was the label for <b>${answer}</b> answer`);
				} else if (answer == 'no') {
					this.srvUtil.showDialogInfo(`"Go Back" was the label for <b>${answer}</b> answer`);
				} else if (answer == 'cancel') {
					this.srvUtil.showDialogInfo(`"Stay Here" was the label for <b>${answer}</b> answer`, (answer2) => console.log(answer2));
				}
			});
	}
}

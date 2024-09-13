import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {Router, ActivatedRoute} from '@angular/router';
import {UserService} from '../shared/user.service';
import {NgForm} from '@angular/forms';
import {SrvMasterData} from '../shared/SrvMasterData';
import {TranslateService} from '@ngx-translate/core';

class Model {
	id: number;
	username: string;
	idRole: string;
	role: string;
	email: string;
	password: string;
	version: number;
	koordinator: string;
}


@Component({
	templateUrl: './CmpUserForm.html',
})

export class CmpUserForm extends BaseComponent implements OnInit {

	id: number;
	insertMode: boolean = false;
	model: Model;
	myForm: NgForm;
	@ViewChild('myForm') currentForm: NgForm;
	saving = false;
	lstRole: any[];
	password2: string;
	mySelf: boolean = false;
	profileMode = false;
	isReseller = false;
	lstKoordinator: any[];

	constructor(private route: ActivatedRoute,
		private prvUser: UserService,
		private _masterDataService: SrvMasterData,
		private router: Router,
		translate: TranslateService) {
		super(translate);
	}
	ngOnInit() {
		this.id = +this.route.snapshot.params['id'];
		this.profileMode = this.route.snapshot.queryParams['mode'] === 'profile';
		this.insertMode = this.id === 0;
		if (!this.insertMode) {
			this.PanictUtil.showRequestIndicator();
			if (this.profileMode) {
				this.prvUser.getMyProfile().subscribe(
					(data: any) => {
						this.model = data;
						this.mySelf = this._masterDataService.getUsername() === this.model.username;
						this.PanictUtil.hideRequestIndicator();
					},
					(error) => this.handleError(error, this.router)
				);
			} else {
				this.prvUser.findById(this.id).subscribe(
					(data: any) => {
						this.model = data;
						this.mySelf = this._masterDataService.getUsername() === this.model.username;
						this.showKoordinator();
						this.PanictUtil.hideRequestIndicator();
					},
					(error) => this.handleError(error, this.router)
				);
			}
		} else {
			this.model = new Model;
		}

		this.prvUser.getRoles().subscribe(
			(data: any) => this.lstRole = data
		);

		this.prvUser.getUserByRole('koordinator').subscribe(
			(data: any) => {
				this.lstKoordinator = data;
			}
		);
	}

	onSubmit(controls) {
		if (this.saving) return;
		this.saving = true;
		let toSave = new Model;
		if (!this.insertMode) {
			toSave.id = this.id;
		}
		toSave.idRole = controls.idRole.value;
		toSave.username = controls.username.value;
		toSave.email = controls.email.value;
		if (toSave.idRole == 'koordinator') {
			toSave.koordinator = toSave.username;
		} else if (toSave.idRole == 'reseller' && !this.profileMode) {
			toSave.koordinator = controls.koordinator.value;
		}
		if (controls.password.value || controls.password2.value) {
			if (controls.password.value !== controls.password2.value) {
				this.formErrors['password2'] = {key: 'same.password'};
				this.saving = false;
				return;
			}
			toSave.password = controls.password.value;
		}
		if (this.insertMode) {
			this.PanictUtil.showRequestIndicator();
			this.prvUser.create(toSave).subscribe(
				data => {
					this.afterSubmitSuccess();
				},
				error => {
					this.handleError(error, this.router);
					this.saving = false;
				}
			);
		} else {
			this.PanictUtil.showRequestIndicator();
			toSave.version = this.model.version;
			this.prvUser.update(toSave).subscribe(
				(data) => {
					this.afterSubmitSuccess();
				},
				error => {
					this.handleError(error, this.router);
					this.saving = false;
				}
			);
		}
	}

	private afterSubmitSuccess() {
		this.PanictUtil.hideRequestIndicator();
		this.saving = false;
		this.insertMode = false;
		this.PanictUtil.showAlertSuccess(this.translate.instant('data.saved'));
		if (!this.profileMode) {
			this.router.navigate(['../'], {relativeTo: this.route});
		}
	}

	ngAfterViewChecked() {
		this.formChanged();
	}

	formChanged() {
		if (this.currentForm === this.myForm) {return;}
		this.myForm = this.currentForm;
		if (this.myForm) {
			this.myForm.valueChanges
				.subscribe(data => this.onValueChanged(data));
		}
	}

	onValueChanged(data?: any) {
		if (!this.myForm) {return;}
		const form = this.myForm.form;

		for (const field in this.formErrors) {
			// clear previous error message (if any)
			this.formErrors[field] = '';
			const control = form.get(field);

			if (control && control.dirty && !control.valid) {
				const messages = this.validationMessages[field];
				for (const key in control.errors) {
					this.formErrors[field] = messages[key];
				}
			}
		}
	}

	showKoordinator() {
		if (this.model.role == 'reseller') {
			this.isReseller = true;
		} else {
			this.isReseller = false;
		}
	}

	formErrors = {
		'username': '',
		'idRole': '',
		'email': '',
		'password': '',
		'password2': null,
	};

	validationMessages = {
		'username': {
			'required': {key: 'this.field.is.required'},
			'minlength': {key: 'min.char', param: {par1: '4'}},
			'maxlength': {key: 'max.char', param: {par1: '255'}},
		},
		'idRole': {
			'required': {key: 'this.field.is.required'},
		},
		'email': {
			'required': {key: 'this.field.is.required'},
			'pattern': {key: 'valid.email'},
		},
		'password': {
			'required': {key: 'this.field.is.required'},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		},
		'password2': {
			'required': {key: 'this.field.is.required'},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		}
	};
}
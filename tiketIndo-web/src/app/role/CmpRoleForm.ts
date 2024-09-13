import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {Router, ActivatedRoute} from '@angular/router';
import {PrvRole} from './PrvRole';
import {NgForm} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {SrvMasterData} from '../shared/SrvMasterData';

class Model {
	id: number;
	name: string;
	description: string;
}

@Component({
	templateUrl: './CmpRoleForm.html',
})

export class CmpRoleForm extends BaseComponent implements OnInit {

	id: number;
	model: Model;
	myForm: NgForm;
	@ViewChild('myForm') currentForm: NgForm;
	saving = false;
	constructor(private route: ActivatedRoute,
		private prvRole: PrvRole,
		private router: Router,
		private srvMasterData: SrvMasterData,
		translate: TranslateService) {
		super(translate);
	}

	ngOnInit() {
		this.id = +this.route.snapshot.params['id'];
		this.getData();
	}

	getData() {
		this.PanictUtil.showRequestIndicator();
		this.prvRole.findById(this.id).subscribe(
			(data: Model) => {
				this.model = data;
				this.PanictUtil.hideRequestIndicator();
			},
			(error) => this.handleError(error, this.router)
		);
	}

	onSubmit() {
		if (this.saving) return;
		this.PanictUtil.showRequestIndicator();
		this.saving = true;
		this.prvRole.save(this.id, this.model).subscribe(
			(data: any) => {
				this.PanictUtil.hideRequestIndicator();
				this.saving = false;
				this.PanictUtil.showAlertSuccess(this.translate.instant('data.saved'));
				if (this.id === 0) {
					this.id = data.id;
					this.router.navigate(['../' + data.id], {relativeTo: this.route});
				}
				this.getData();
			},
			error => {
				this.handleError(error, this.router);
				this.saving = false;
			}
		);
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

	formErrors = {
		'name': '',
		'description': '',
	};

	validationMessages = {
		'name': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '20'}},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		},
		'description': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '25'}},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		}
	};
}
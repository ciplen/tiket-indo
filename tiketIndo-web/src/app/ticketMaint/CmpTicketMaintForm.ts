import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {Router, ActivatedRoute} from '@angular/router';
import {PrvTicketMaint} from './PrvTicketMaint';
import {NgForm} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {TableQuery, TableCriteria, SortingInfo} from '../shared/TabelQuery';
class Model {
	id: number;
	venueId: number;
	type: string;
	keterangan: string;
	amount: number;
	minOrder: number;
	diskon: number;
	jumlahTerjual: number;
	tanggalPeriode1: string;
	tanggalPeriode2: string;
	//	tanggalPeriode3: string;
	//	tanggalPeriode4: string;
	//	tanggalPeriode5: string;
	hargaPeriode1: string;
	hargaPeriode2: string;
	//	hargaPeriode3: string;
	//	hargaPeriode4: string;
	//	hargaPeriode5: string;
}

@Component({
	templateUrl: './CmpTicketMaintForm.html',
})

export class CmpTicketMaintForm extends BaseComponent implements OnInit {
	id: number;
	insertMode: boolean = false;
	model: Model;
	myForm: NgForm;
	@ViewChild('myForm') currentForm: NgForm;
	saving = false;
	sorts: any[];
	criteria: any = {
		venue: "",
	};
	lstVenue: any[];
	constructor(private route: ActivatedRoute,
		private prvTicket: PrvTicketMaint,
		private router: Router,
		translate: TranslateService) {
		super(translate);
	}
	ngOnInit() {
		console.log('onInit');
		this.id = +this.route.snapshot.params['id'];
		this.insertMode = this.id === 0;
		this.PanictUtil.showRequestIndicator();
		this.prvTicket.findById(this.id).subscribe(
			(data: Model) => {
				this.model = data;
				this.getLstVenue();
				this.PanictUtil.hideRequestIndicator();
			},
			(error) => this.handleError(error, this.router)
		);
	}
	getLstVenue() {
		let query = this.getQueryObject();
		this.PanictUtil.showRequestIndicator();
		this.prvTicket.findVenue(0, 1000, query).subscribe(
			(data: any) => {
				this.lstVenue = data.rows;
			}
		)

	}
	getQueryObject() {
		let query = new TableQuery;

		if (this.sorts && this.sorts.length > 0) {
			this.sorts.forEach(el => {
				query.sortingInfos.push(new SortingInfo(el.prop, el.dir));
			});
		}

		for (var k in this.criteria) {
			if (this.criteria[k]) {
				query.tableCriteria.push(new TableCriteria(k, this.criteria[k]));
			}
		}
		return query;
	}
	onSubmit() {
		if (this.saving) return;
		this.PanictUtil.showRequestIndicator();
		this.saving = true;
		this.prvTicket.save(this.id, this.model).subscribe(
			(data: any) => {
				this.PanictUtil.hideRequestIndicator();
				this.saving = false;
				this.insertMode = false;
				this.id = data.id;
				this.model.venueId = data.venueId;
				this.model.type = data.type;
				this.model.keterangan = data.keterangan;
				this.model.diskon = data.diskon;
				this.model.minOrder = data.minOrder;
				this.model.amount = data.amount;
				this.model.jumlahTerjual = data.jumlahTerjual;
				this.model.tanggalPeriode1 = data.tanggalPeriode1;
				this.model.tanggalPeriode2 = data.tanggalPeriode2;
				//				this.model.tanggalPeriode3 = data.tanggalPeriode3;
				//				this.model.tanggalPeriode4 = data.tanggalPeriode4;
				//				this.model.tanggalPeriode5 = data.tanggalPeriode5;
				this.model.hargaPeriode1 = data.hargaPeriode1;
				this.model.hargaPeriode2 = data.hargaPeriode2;
				//				this.model.hargaPeriode3 = data.hargaPeriode3;
				//				this.model.hargaPeriode4 = data.hargaPeriode4;
				//				this.model.hargaPeriode5 = data.hargaPeriode5;

				this.PanictUtil.showAlertSuccess(this.translate.instant('data.saved'));
				this.router.navigate(['../' + data.id], {relativeTo: this.route});
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
		'type': '',
		'keterangan': '',
		'amount': '',
		'jumlahTerjual': '',
	};

	validationMessages = {
		'type': {
			'required': {key: 'this.field.is.required'},
		},
		'amount': {
			'required': {key: 'this.field.is.required'},
		},
		'jumlahTerjual': {
			'required': {key: 'this.field.is.required'},
		},
		'keterangan': {
			'required': {key: 'this.field.is.required'},
		},
	};
}
import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {Router, ActivatedRoute} from '@angular/router';
import {PrvVenueMaint} from './PrvVenueMaint';
import {NgForm} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {Config} from '../shared/index';

class Model {
	id: number;
	nama: string;
	tempat: string;
	tanggalAwal: string;
	tanggalAkhir: string;
	typeVenue: string;
	smallBanner: any;
	bigBanner: any;
	smallBannerFile: any;
	bigBannerFile: any;
}

const CAMERA_PNG = 'assets/cam.png';

@Component({
	templateUrl: './CmpVenueMaintForm.html',
})

export class CmpVenueMaintForm extends BaseComponent implements OnInit {

	id: number;
	lstTypeVenue : any;
	insertMode: boolean = false;
	model: Model;
	myForm: NgForm;
	smallBannerUrl = CAMERA_PNG;
	bigBannerUrl = CAMERA_PNG;
	smallBannerFile: any;
	bigBannerFile: any;
	smallBannerSelected = false;
	bigBannerSelected = false;
	apiUrl = Config.API;
	@ViewChild('myForm') currentForm: NgForm;
	saving = false;
	constructor(private route: ActivatedRoute,
		private prvVenueMaint: PrvVenueMaint,
		private router: Router,
		translate: TranslateService) {
		super(translate);
	}
	ngOnInit() {
		console.log('onInit');
		this.id = +this.route.snapshot.params['id'];
		this.insertMode = this.id === 0;
		this.PanictUtil.showRequestIndicator();
		this.prvVenueMaint.findById(this.id).subscribe(
			(data: Model) => {
				this.model = data;
				if (this.model.smallBanner) {
					console.log(this.model.smallBanner);
					this.smallBannerUrl = this.apiUrl + 'vm/pict/small/' + this.id;
					this.smallBannerUrl = this.apiUrl + 'vm/pict/small/' + this.id;
					this.smallBannerSelected = true;
				} else {
					this.smallBannerUrl = CAMERA_PNG;
				}
				if (this.model.bigBanner) {
					console.log(this.model.bigBanner);
					this.bigBannerUrl = this.apiUrl + 'vm/pict/big/' + this.id;
					this.bigBannerUrl = this.apiUrl + 'vm/pict/big/' + this.id;
					this.bigBannerSelected = true;
				} else {
					this.bigBannerUrl = CAMERA_PNG;
				}
				this.PanictUtil.hideRequestIndicator();
			},
			(error) => this.handleError(error, this.router)
		);
	}

	onSubmit() {
		if (this.saving) return;
		this.PanictUtil.showRequestIndicator();
		this.saving = true;
		console.log('small : ' + this.smallBannerFile);
		console.log('big : ' + this.bigBannerFile)
		this.prvVenueMaint.save(this.id, this.model).subscribe(
			(data: any) => {
				console.log('id : ' + data.id)
				this.PanictUtil.hideRequestIndicator();
				this.saving = false;
				this.insertMode = false;
				this.id = data.id;
				this.model.nama = data.nama;
				this.model.tempat = data.tempat;
				this.model.tanggalAwal = data.tanggalAwal
				this.model.tanggalAkhir = data.tanggalAkhir
				this.model.typeVenue = data.typeVenue
				//console.log("data : " + data.smallBanner);
				
				// saveSmallBanner
				if (this.smallBannerFile) {
					this.prvVenueMaint.saveSmallBanner(this.smallBannerFile, this.id).subscribe(
						(data: any) => {
							//this.model.smallBanner = data.smallBanner
							// saveBigBanner
							if (this.bigBannerFile) {
								this.prvVenueMaint.saveBigBanner(this.bigBannerFile, this.id).subscribe(
									(data: any) => {
										//this.model.bigBanner = data.bigBanner
									}
								);
							}
						}
					);
				}
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

	onSelectFile(event, type?: string) {
		if (event.target.files && event.target.files[0]) {
			if (type === 'small') {
				const reader = new FileReader();
				reader.readAsDataURL(event.target.files[0]);
				this.smallBannerFile = event.target.files[0];
				console.log('data: ' + this.smallBannerFile)
				this.smallBannerSelected = true;
				reader.onload = (evt: any) => {
					this.smallBannerUrl = evt.target.result;
				}
				//this.model.smallBannerFile = event.target.files[0];

			} else {
				const reader = new FileReader();
				reader.readAsDataURL(event.target.files[0]);
				this.bigBannerFile = event.target.files[0];
				console.log('data: ' + this.bigBannerFile)
				this.bigBannerSelected = true;
				reader.onload = (evt: any) => {
					this.bigBannerUrl = evt.target.result;
				}
				//this.model.bigBannerFile = event.target.files[0];
			}
		}
	}

	deleteAsset(type?: string) {
		if (type === 'small') {
			this.model.smallBannerFile = null;
			this.model.smallBanner = null;
			this.smallBannerFile = null;
			this.smallBannerUrl = CAMERA_PNG;
			this.smallBannerSelected = false;
		} else {
			this.model.bigBannerFile = null;
			this.model.bigBanner = null;
			this.bigBannerFile = null;
			this.bigBannerUrl = CAMERA_PNG;
			this.bigBannerSelected = false;
		}
	}

	formErrors = {
		'nama': '',
		'tempat': '',
		'typeVenue': '',
		'tanggalAwal': '',
		'tanggalAkhir': ''
	};

	validationMessages = {
		'nama': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '50'}},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		},
		'tempat': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '50'}},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		},
		'typeVenue': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '50'}},
			'minlength': {key: 'min.char', param: {par1: '4'}},
		},
		'tanggalAwal': {
			'required': {key: 'this.field.is.required'},
		},
		//'tanggalAkhir': {
		//	'required': {key: 'this.field.is.required'},
		//}
		// 'fileInput': {
		// 	'required': {key: 'this.filed.is.required'},
		// },
		// 'fileInput2': {
		// 	'required': {key: 'this.filed.is.required'},
		// },
	};
}
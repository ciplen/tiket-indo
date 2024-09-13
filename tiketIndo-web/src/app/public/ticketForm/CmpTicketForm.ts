import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {SrvUtil} from 'app/shared/SrvUtil';
import {PrvTicketMaint} from '../../ticketMaint/PrvTicketMaint';
import {PrvVenueMaint} from '../../venueMaint/PrvVenueMaint';
import {formatDate} from '@angular/common';
import {BaseComponent} from '../../shared/base.component';
import {jsonpCallbackContext} from '@angular/common/http/src/module';
import {JsonPipe} from '@angular/common';

class UniqueIdentity {
	nama: string;
	ktp: string;
	tipeKaos: string;
	ukuranKaos: string;
}

class Model {
	identityList: UniqueIdentity[] = [];
	venueId: number;
	ticketCode: string;
	idTicMaint: number;
	tanggalAwal: any;
	email: string;
	noTelepon: number;
	hargaTotal: number;
	hargaCode: number;
	hargaCodeTaxDis: number;
	an: string;
	hargaPeriode1: any;
	hargaPeriode5: any;
	type: any;
	noRek: string;
	rekTujuan: string;
	event: string;
	codeUnique: number;
	qty: number;
	tanggalLahir: string;
	kotaAsal: string;
	umur: any;
	//	marchandise: string;
}

@Component({
	selector: 'ticketForm',
	templateUrl: 'CmpTicketForm.html',
	styleUrls: ['./CmpTicketForm.scss'],
})

export class CmpTicketForm implements OnInit {
	@ViewChild('tickForm') currentForm: NgForm;
	today = new Date();
	jtoday: any;
	model: Model;
	tickForm: NgForm;
	uniq: UniqueIdentity;
	saving = false;
	insertMode = false;
	lstPayment: any[];
	lstUnique: any[];
	id: number;
	dataTicMain: any;
	harga: number;
	ktp: number;
	totalHarga: number;
	data: any;
	type: any;
	event: any;
	tempat: any;
	venueData: any;
	ticMainData: any;
	idVenue: number;
	hargaPeriode1: any;
	hargaPeriode5: any;
	qty: number;
	payment: {};
	ganti: boolean = false;
	rekTujuan: any;
	noRek: any;
	an: string;
	pict: string;
	path_picture: any;
	label: string;
	hargaCode: number;
	discount: number;
	notifDscnt: string;
	discountData: any;
	minDiskon: number;
	hargaDiskon: number;
	hargaDiskonTotal: number;
	hargaCodeTaxDis: number;
	hargaFinal: number;
	tax: any;
	venueId: number;
	codeUnique: any;
	lstProvinsi: any[];
	lstKota: any[];
	provinsiId: any;
	constructor(public el: ElementRef,
		private router: Router,
		public dialog: MatDialog,
		private prvVenueMaint: PrvVenueMaint,
		private prvTicketMaint: PrvTicketMaint,
		private srvUtil: SrvUtil,
		private route: ActivatedRoute,
		translate: TranslateService) {
		this.model = new Model;
		this.lstPayment = [
			{'id': 1, 'label': 'Transfer BCA', 'rek': '4560977901', 'pict': 'https://assets.kitabisa.cc/images/banks/bca.png', 'name': 'BCA', 'an': 'Ignatius Prasetyadi'},
			{'id': 3, 'label': 'Transfer BRI', 'rek': '6927-01-013915-53-1', 'pict': 'https://assets.kitabisa.cc/images/banks/bri.png', 'name': 'BRI', 'an': 'Rachmat Bayu Firdas'},
			//			{'id': 4, 'label': 'Transfer Mandiri', 'rek': '1370016465813', 'pict': 'https://assets.kitabisa.cc/images/banks/mandiri.png', 'name': 'Mandiri', 'an': 'Rachmat Bayu Firdas'},
			{'id': 2, 'label': 'Transfer BNI', 'rek': '0837832041', 'pict': 'https://assets.kitabisa.cc/images/banks/bni.png', 'name': 'BNI', 'an': 'Rachmat Bayu Firdas'},
		]
		this.payment = this.lstPayment[0];
		this.rekTujuan = 'BCA';
		this.label = 'Transfer BCA';
		this.pict = 'https://assets.kitabisa.cc/images/banks/bca.png';
		this.noRek = '4560977901';
		this.an = 'Ignatius Prasetyadi';
	}


	ngOnInit() {
		this.id = this.route.snapshot.params['id'];
		this.qty = this.route.snapshot.params['qty'];
		this.jtoday = formatDate(this.today, 'dd-MM-yyyy', 'en-US', '+0530');
		console.log('Today = ' + this.jtoday);
		this.getProvinsi();
		this.getData();
	}

	checkAvailability() {
		this.prvTicketMaint.checkAvailability(this.ticMainData.type, this.qty).subscribe(
			data => {

			}, error => {
				this.srvUtil.showDialogError('Oops!' + error.error.parameters[0]);
			}
		)
	}

	getData() {
		this.prvTicketMaint.findById(this.id).subscribe(
			(data) => {
				this.ticMainData = data;
				this.checkAvailability();
				this.model.hargaPeriode1 = this.ticMainData.hargaPeriode1;
				this.model.hargaPeriode5 = this.ticMainData.hargaPeriode5;
				this.model.tanggalAwal = this.ticMainData.tanggalAwal;
				this.totalHarga = this.model.hargaPeriode1 * this.qty;
				this.model.type = this.ticMainData.type;
				this.model.venueId = this.ticMainData.venueId;
				this.model.qty = this.qty;
				this.getVenueMain(this.model.venueId);
				this.model.identityList = [];
				if (this.ticMainData.venueId != 5) { //5 is id for Wisata Taman Lampion
					for (let idx = 0; idx < this.qty; idx++) {
						this.model.identityList.push(new UniqueIdentity);
					}
				} else {
					this.model.identityList.push(new UniqueIdentity);
				}
				this.tax = (this.totalHarga / 100) * 10;
			},
			error => {
				console.log("error")
			});
	}
	getVenueMain(id: any) {
		this.prvVenueMaint.findById(this.model.venueId).subscribe(
			(data) => {
				this.venueData = data;
				this.venueId = this.venueData.id;
				this.event = this.venueData.nama;
				this.tempat = this.venueData.tempat;
				this.harga = this.model.hargaPeriode1;
				console.log("data dari venue id : " + this.venueData);
			},
			error => {
				console.log("error")
			});
	}
	onSubmit() {
		//		this.getDiscount();
	}
	saveData() {
		if (this.saving) {
			return;
		}
		this.saving = true;
		this.CalculateAge();
		this.model.noRek = this.noRek;
		this.model.rekTujuan = this.rekTujuan;
		this.model.idTicMaint = this.id;
		this.model.hargaTotal = this.totalHarga;
		this.model.codeUnique = this.codeUnique;
		this.model.hargaCodeTaxDis = this.hargaCodeTaxDis;
		this.model.ticketCode = 'CODE:' + "0" + '/' + this.venueId + '/' + this.id;;
		this.model.event = this.event;
		this.model.an = this.an;
		//		this.model.marchandise = this.tipeKaos + ',' + this.ukuranKaos;
		console.log("data yang di ambil: " + JSON.stringify(this.model.identityList));

		this.srvUtil.showDialogConfirm(
			'<strong>' + "Apakah pesanan anda benar? \n\n\
								Kesalahan pengisian data diri terutama Email akan menghambat proses order.\n\
								Tetap Lanjutkan? ",
			(answer) => {
				if (answer === 'yes') {
					this.prvTicketMaint.saveBooking(this.id, this.model).subscribe(
						(data: any) => {
							this.router.navigate(['/confirm/0/email@email.com']);
							this.srvUtil.showDialogInfo('Pemesanan SUKSES! periksa Email Anda untuk konfirmasi pembayaran.');
						},
						error => {
							this.saving = false;
							this.srvUtil.showDialogError('Oops! ' + error.error.parameters[0]);
							this.router.navigateByUrl('/quantityForm/' + this.id);
						}
					)
				}
				this.saving = false;
			});
	}

	changePayment(selected?) {
		if (this.ganti) {
			this.ganti = false;
		} else {
			this.ganti = true;
		}

		if (selected) {
			console.log("yang terpilih bank: " + selected.name);
			console.log("No rekening: " + selected.rek);
			this.payment = selected;
			this.rekTujuan = selected.name;
			this.noRek = selected.rek;
			this.label = selected.label;
			this.pict = selected.pict;
			this.an = selected.an;
		}
	}

	alert() {
		var x = document.getElementById("toggleAlert");
		if (x.style.display === "block") {
			x.style.display = "none";
		} else {
			x.style.display = "none";
		}
	}

	getDiscount() {
		this.prvTicketMaint.getDiscount(this.id, this.discountData).subscribe(
			data => {
				this.discountData = data;
				if (this.discountData) {
					this.discount = this.discountData.diskon;
					this.minDiskon = this.discountData.minOrder;
					this.hargaDiskon = (this.model.hargaPeriode1 / 100) * this.discount;
				} else {
					this.discount = 0;
					this.notifDscnt = "TIDAK ADA DISKON";
				}
			},
			error => {
				console.log("error");
			});
	}

	getCodeUniq() {
		this.getDiscount();
		this.prvTicketMaint.getCodeUniq().subscribe(
			data => {
				this.codeUnique = data;
				this.hargaCode = this.totalHarga + this.codeUnique + this.tax;
				if (this.discount != 0) {
					this.hargaCodeTaxDis = this.hargaCode - this.hargaDiskon;
				} else {
					this.hargaCodeTaxDis = this.hargaCode;
				}
				console.log("diskon : " + this.hargaCodeTaxDis);
				this.toggleProfilePanel();
			},
			error => {
				console.log("error");
			});
	}

	showProfilePanel = false;
	toggleProfilePanel() {
		this.showProfilePanel = true;
		var x = document.getElementById("toggle");
		if (x.style.display === "block") {
			x.style.display = "none";
		} else {
			x.style.display = "block";
		}

		var y = document.getElementById("toggleButton");
		if (y.style.display === "none") {
			y.style.display = "block";
		} else {
			y.style.display = "none";
		}
	}

	ngAfterViewChecked() {
		this.formChanged();
	}

	formChanged() {
		if (this.currentForm === this.tickForm) {return;}
		this.tickForm = this.currentForm;
		if (this.tickForm) {
			this.tickForm.valueChanges
				.subscribe(data => this.onValueChanged(data));
		}
	}

	onValueChanged(data?: any) {
		if (!this.tickForm) {return;}
		const form = this.tickForm.form;

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

	getProvinsi() {
		this.prvTicketMaint.getListProvinsi().subscribe(
			(data: any) => {
				this.lstProvinsi = data;
			}
		);
	}

	onChange(event) {
		this.provinsiId = event.target.value;
		this.getKota(this.provinsiId);
	}

	getKota(id) {
		this.prvTicketMaint.getListKota(id).subscribe(
			(data: any) => {
				this.lstKota = data;
			}
		);
	}

	public CalculateAge(): void {
		if (this.model.tanggalLahir) {
			var timeDiff = Math.abs(Date.now() - new Date(this.model.tanggalLahir).getTime());
			this.model.umur = Math.floor(timeDiff / (1000 * 3600 * 24) / 365.25);
			console.log(this.model.umur)
		}
	}

	formErrors = {
		'nama': '',
		'ktp': '',
	};

	validationMessages = {
		'nama': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '25'}},
			'minlength': {key: 'min.char', param: {par1: '3'}},
		},
		'ktp': {
			'required': {key: 'this.field.is.required'},
			'maxlength': {key: 'max.char', param: {par1: '25'}},
			'minlength': {key: 'min.char', param: {par1: '3'}},
		}
	};

}
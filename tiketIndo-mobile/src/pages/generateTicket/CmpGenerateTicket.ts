import {Component, OnInit, ViewChild} from '@angular/core';
import {NavController, Nav, AlertController, LoadingController} from 'ionic-angular';
import {PrvTicket} from '../../providers/PrvTicket';
import {TableQuery, TableCriteria, SortingInfo} from '../../shared/TabelQuery';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CmpLogin} from '../login/CmpLogin';
declare function require(name: string);
class Model {
	id: number;
	venueId: number;
	ticketId: number;
	resellerId: number;
	buyerName: string;
	buyerEmail: string;
	ticketCode: string;
	ticketToken: string;
	status: string;
	noKtp: number;
	phone: number;
	version: number;
}

@Component({
	selector: 'cmp-generateTicket',
	templateUrl: 'CmpGenerateTicket.html'
})
export class CmpGenerateTicket implements OnInit {
	@ViewChild(Nav) nav: Nav;
	lstVenue: any[];
	lstType: any[];
	sorts: any[];
	form: FormGroup;
	criteria: any = {
		venueId: "",
		ticketId: ""
	};
	//	loggedUser: any;
	model: Model;
	encodeData: any;
	scannedData: {};
	data: any;
	lstData: any[];
	jwt: any;
	secret: string;
	loading: any;
	saving = false;
	authInfo: any;
	reseller: boolean = false;
	koordinator: boolean = false;
	canGenerate = false;

	constructor(private navCtrl: NavController,
		private prvTicket: PrvTicket,
		private formBuilder: FormBuilder,
		private alertCtrl: AlertController,
		private loadingController: LoadingController) {
		this.form = formBuilder.group({
			resellerId: [''],
			resellerName: [''],
			venueId: [''],
			ticketId: [''],
			noKtp: [''],
			phone: [''],
			buyerName: [''],
			buyerEmail: [''],
			//			ticketAmount: [''],
		});
		this.authInfo = JSON.parse(localStorage.getItem('auth_info'));
		this.canGenerate = this.authInfo.role == 'reseller' || this.authInfo.role == 'koordinator' || this.authInfo.role == 'supertester';
		if (this.canGenerate) {
			this.initForm();
		}

	}

	initForm() {
		let EMAILPATTERN = /^[a-z0-9!#$%&'*+\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$/i;
		this.form = this.formBuilder.group({
			venueId: [
				'', Validators.compose([
					Validators.required
				])
			],
			ticketId: [
				'', Validators.compose([
					Validators.required
				])
			],
			noKtp: [
				'', Validators.compose([
					Validators.required,
					Validators.minLength(5),
				])
			],
			phone: [
				'', Validators.compose([
					Validators.required,
					Validators.minLength(11),
				])
			],
			buyerName: [
				'', Validators.compose([
					Validators.required
				])
			],
			buyerEmail: [
				'', Validators.compose([
					Validators.required,
					Validators.pattern(EMAILPATTERN)
				])
			],

		});
	}
	ngOnInit() {
		this.model = new Model()
		if (localStorage.getItem('venueId')) {
			this.model.venueId = +localStorage.getItem('venueId');
			this.form.value.venueId = this.model.venueId;
			this.getTicketType();
		}
		this.getVenue();
	}
	getVenue() {
		let query = this.getQueryObject();
		query.tableCriteria.push(new TableCriteria("nama", "Festival Lampion Jogja 2020-2021"))
		this.prvTicket.findVenue(0, 1000, query).subscribe(
			(data: any) => {
				this.lstVenue = data.rows;
			}, error => {
				if (error.error.code = 'ER0401') {
					this.navCtrl.setRoot(CmpLogin);

				}
			}
		)
	}
	getTicketType() {
		if (!localStorage.getItem('venueId')) {
			localStorage.setItem('venueId', this.form.value.venueId);
		}
		this.prvTicket.findTicketByVenueId(+this.form.value.venueId).subscribe(
			(data: any) => {
				this.lstType = data;
			}
		);
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
	generate() {
		if (this.saving) return;
		this.saving = true;
		this.form.value.resellerId = this.authInfo.id;
		this.form.value.resellerName = this.authInfo.username;

		let toSave = new Model;
		toSave.id = 0;
		toSave.resellerId = this.form.value.resellerId;
		toSave.venueId = this.form.value.venueId;
		toSave.ticketId = this.form.value.ticketId;
		toSave.ticketCode = 'CODE:' + toSave.resellerId + '/' + toSave.venueId + '/' + toSave.ticketId;

		toSave.buyerName = this.form.value.buyerName;
		toSave.buyerEmail = this.form.value.buyerEmail;
		toSave.noKtp = this.form.value.noKtp;
		toSave.phone = this.form.value.phone;
		toSave.status = 'AVAILABLE';
		let alert;
		let loader = this.loadingController.create({
			content: 'Generating, please wait...'
		});
		loader.present();
		this.prvTicket.generate(toSave).subscribe(
			(data: any) => {
				loader.dismiss();
				alert = this.alertCtrl.create({
					title: 'Berhasil',
					message: 'E-Ticket berhasil dikirimkan ke email anda. Silahkan cek email masuk/spam untuk mendapatkan tiket.',
					buttons: [
						{
							text: 'Ok',
							handler: () => {
								this.model = new Model;
								this.form.reset();
								this.saving = false;
							}
						}
					],
					cssClass: 'alertCustomCss'
				});
				alert.present();
			},
			error => {
				//				alert(JSON.stringify(error));
				loader.dismiss();
				alert = this.alertCtrl.create({
					title: 'Oops!',
					message: error.error.parameters[0],
					buttons: [
						{
							text: 'Ok'
						}
					],
					cssClass: 'alertCustomCss'
				});
				alert.present();
				this.saving = false;
			}
		)
		console.log('generated');
	}
}



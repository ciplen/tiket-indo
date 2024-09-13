import {Component, OnInit, ViewChild} from '@angular/core';
import {NavController, Nav, AlertController} from 'ionic-angular';
import {TableQuery, TableCriteria, SortingInfo} from '../../shared/TabelQuery';
import {PrvTicket} from '../../providers/PrvTicket';
import {FormBuilder, FormGroup} from '@angular/forms';
import {CmpLogin} from '../login/CmpLogin';
import {CmpGenerateTicket} from '../generateTicket/CmpGenerateTicket';
import {CmpScanner} from '../scanner/CmpScanner';

@Component({
	selector: 'page-home',
	templateUrl: 'home.html'
})
export class HomePage implements OnInit {
	@ViewChild(Nav) nav: Nav;
	lstVenue: any[];
	sorts: any[];
	form: FormGroup;
	criteria: any = {
		venueId: "",
	};
	loggedUser: any;
	lstCount: any;
	koordinator = false;
	total = 0;
	constructor(private prvTicket: PrvTicket,
		formBuilder: FormBuilder,
		private alertCtrl: AlertController,
		private navCtrl: NavController) {
		this.form = formBuilder.group({
			venueId: [''],
		});
		this.loggedUser = JSON.parse(localStorage.getItem("auth_info"));
		this.koordinator = this.loggedUser.role == 'koordinator';
	}
	ngOnInit() {
		this.getVenue();
	}
	getVenue() {
		let query = this.getQueryObject();
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
	getPenjualan() {
		this.prvTicket.countSellingListByKoorId(+this.form.value.venueId, this.loggedUser.username).subscribe(
			(data: any) => {
				this.lstCount = data;
				for (let i in this.lstCount) {
					this.total = this.total + this.lstCount[i].totalCount;
				}
			});
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
	doRefresh(refresher) {
		refresher.complete();
		if (this.koordinator) {
			if (this.form.value.venueId && this.loggedUser.username) {
				this.total = 0;
				this.getPenjualan();
			} else {
				let alert = this.alertCtrl.create({
					title: 'Oops!',
					message: 'Silahkan Pilih Event',
					buttons: [
						{
							text: 'Ok'
						}
					],
					cssClass: 'alertCustomCss'
				});
				alert.present();
			}
		}
	}
	generate(){
		this.navCtrl.push(CmpGenerateTicket);
	}
	scan(){
		this.navCtrl.push(CmpScanner);
	}
}

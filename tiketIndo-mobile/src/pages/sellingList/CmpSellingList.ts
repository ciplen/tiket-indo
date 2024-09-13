import {Component, OnInit, ViewChild} from '@angular/core';
import {PrvTicket} from '../../providers/PrvTicket';
import {FormBuilder, FormGroup} from '@angular/forms';
import {TableQuery, TableCriteria, SortingInfo} from '../../shared/TabelQuery';
import {NavController, Nav} from 'ionic-angular';
import {CmpLogin} from '../login/CmpLogin';

@Component({
	selector: 'cmp-sellingList',
	templateUrl: 'CmpSellingList.html'
})

export class CmpSellingList implements OnInit {
	@ViewChild(Nav) nav: Nav;
	lstVenue: any[];
	lstType: any[];
	sorts: any[];
	form: FormGroup;
	criteria: any = {
		venueId: "",
	};
	loggedUser: any;
	lstCount: any;

	constructor(private prvTicket: PrvTicket,
		formBuilder: FormBuilder,
		private navCtrl: NavController) {
		this.form = formBuilder.group({
			venueId: [''],
		});
	}
	ngOnInit() {
		this.loggedUser = JSON.parse(localStorage.getItem("auth_info"));
		this.getVenue();
	}

	countSellingList() {
		this.prvTicket.countSellingList(+this.form.value.venueId, this.loggedUser.id).subscribe(
			(data: any) => {
				this.lstCount = data;
			});
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

	getTicketType() {
		this.prvTicket.findTicketByVenueId(+this.form.value.venueId).subscribe(
			(data: any) => {
				this.lstType = data;
			}
		);
		this.countSellingList();
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
}
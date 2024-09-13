import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {PrvTicketDtl} from './PrvTicketDtl';
import {TranslateService} from '@ngx-translate/core';
import {BaseListComponent} from '../shared/BaseListComponent';
import {SrvUtil} from 'app/shared/SrvUtil';
import {SrvMasterData} from '../shared/SrvMasterData';


@Component({
	templateUrl: './CmpTicketDtl.html',
})

export class CmpTicketDtl extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	canMaintain = false;
	isAdmin = false;
	penjualan: string;
	lstEvent: any;
	lstSeller: any;
	eventId: any;

	constructor(router: Router,
		route: ActivatedRoute,
		translate: TranslateService,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		private prvTicket: PrvTicketDtl) {
		super('CmpTicketDtl', translate, route, router);
		this.canMaintain = this.srvMasterData.hasRole('gate_keeper') || this.srvMasterData.hasRole('approver');
		this.isAdmin = this.srvMasterData.isAdmin();
		this.criteria = {
			buyerName: {value: '', label: 'Nama'},
			noKtp: {value: '', label: 'Nomor ID'},
			venueId: {value: '', label: 'Nama Event/Venue'},
			resellerId: {value: '', label: 'Nama Reseller'},
		};
	}
	ngOnInit() {
		this.getLstVenue();
		this.getLstReseller();
		this.tableHeight = (window.innerHeight - 225) + 'px';
		super.defaultOnInit();
	}

	getData(query: TableQuery) {
		return this.prvTicket.find(this.paginationObject.startIndex, this.paginationObject.maxRows, this.penjualan, query);
	}
	getRowClass(row) {
		return {
			'status-is-approved': (row.status == 'VERIFIED')
		};
	}

	verify(status) {
		if (this.selected && this.selected.length > 0) {
			if (this.selected[0].status == 'VERIFIED') {
				if (status != 'VERIFIED') {
					this.srvUtil.showDialogConfirm(
						'<strong>' + this.translate.instant('Cancel verifikasi data tiket terpilih?') + '</strong><br><br>' +
						'Nama: <strong>' + this.selected[0].buyerName + '</strong><br>' +
						'Nomor ID: <strong>' + this.selected[0].noKtp
						,
						(answer) => {
							if (answer === 'yes') {
								this.PanictUtil.showRequestIndicator();
								this.prvTicket.verify(this.selected[0].id, 'CANCEL').subscribe(
									data => this.reload(),
									error => {
										this.handleError(error, this.router);
									});
							}
						});
				} else {
					this.srvUtil.showDialogError('Data tiket yang anda pilih sudah terpakai (VERIFIED).');
				}
				this.PanictUtil.hideRequestIndicator();
			} else {
				if (status == 'VERIFIED') {
					this.srvUtil.showDialogConfirm(
						'<strong>' + this.translate.instant('Verifikasi data tiket terpilih?') + '</strong><br><br>' +
						'Nama: <strong>' + this.selected[0].buyerName + '</strong><br>' +
						'Nomor ID: <strong>' + this.selected[0].noKtp
						,
						(answer) => {
							if (answer === 'yes') {
								this.PanictUtil.showRequestIndicator();
								console.log(this.selected[0].id);
								this.prvTicket.verify(this.selected[0].id, 'VERIFIED').subscribe(
									data => this.reload(),
									error => {
										this.handleError(error, this.router);
									});
							}
						});
				} else {
					this.srvUtil.showDialogError('Data yang bisa dicancel adalah data yang sudah "VERIFIED".');
				}
			}
		}
	}

	exportData() {
		let query = this.getQueryObject();
		this.prvTicket.exportData(this.paginationObject.startIndex, this.paginationObject.maxRows, this.penjualan, query).subscribe((res) => {
			window['saveAs'](res, 'ticket_detail.xlsx');
		}, error => {
			this.handleError(error, this.router);
		});
	}
	exportAllData() {
		let query = this.getQueryObject();
		this.prvTicket.exportAllData(this.penjualan, query).subscribe((res) => {
			window['saveAs'](res, 'ticket_detail.xlsx');
		}, error => {
			this.handleError(error, this.router);
		});
	}
	getLstVenue() {
		return this.prvTicket.findLstVenue().subscribe(
			data => {
				this.lstEvent = data;
			}
		);
	}
	getLstReseller() {
		return this.prvTicket.findLstSeller().subscribe(
			data => {
				this.lstSeller = data;
			}
		);
	}
	change() {
		this.criteria.resellerId.value = null;
	}
}
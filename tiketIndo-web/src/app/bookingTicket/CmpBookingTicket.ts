import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery, SortingInfo, TableCriteria} from '../shared/TabelQuery';
import {PrvBookingTicket} from './PrvBookingTicket';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';
import {TranslateService} from '@ngx-translate/core';
import {BaseListComponent} from '../shared/BaseListComponent';
import {SrvUtil} from 'app/shared/SrvUtil';
import {SrvMasterData} from '../shared/SrvMasterData';
import {MatDialog} from '@angular/material';
import {Config} from '../shared/index';
import {CmpBuktiTrans} from '../bookingTicket/CmpBuktiTrans';
import {throwError} from 'rxjs';


class Model {
	id: number;
	venueId: number;
	ticketId: number;
	resellerId: number;
	nama: string;
	buyerEmail: string;
	ticketCode: string;
	ticketToken: string;
	status: string;
	rekTujuan: string;
	noKtp: number;
	version: number;
	codeUnique: string;
	modifiedDate: string;
}

@Component({
	templateUrl: './CmpBookingTicket.html',
	styleUrls: ['./CmpBooking.scss'],
	encapsulation: ViewEncapsulation.None,
})

export class CmpBookingTicket extends BaseListComponent implements OnInit {
	selected = [];
	apiUrl = Config.API;
	model: Model;
	tableHeight: string;
	canMaintain = false;
	url: any;
	canApprove = false;
	rows = [];
	saving = false;
	bookingData: any;
	insertMode = false;
	isApprover = false;
	strDate: string;
	endDate: string;
	lstEvent: any;
	lstCounter: any;
	countApproved: number;
	countCanceled: number;
	countConfirm: number;
	countPending: number;
	savedVenue: any;
	constructor(router: Router,
		route: ActivatedRoute,
		translate: TranslateService,
		private dialog: MatDialog,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		private prvBooking: PrvBookingTicket,
		private prvTicket: PrvTicketMaint) {
		super('CmpBookingTicket', translate, route, router);
		this.canMaintain = this.srvMasterData.isAdmin();
		this.isApprover = this.srvMasterData.hasRole('approver');

		this.criteria = {
			venueId: {value: '', label: 'Event'},
			status: {value: '', label: 'Status'},
			noKtp: {value: '', label: 'No Identitas'},
			noRek: {value: '', label: 'No Rekening'},
			nama: {value: '', label: 'Nama Pembeli'},
			kodeUnik: {value: '', label: 'Code Unique'},
			rekTujuan: {value: '', label: 'Nama Bank'},
			email: {value: '', label: 'Email'},
			typeTicket: {value: '', label: 'Type Ticket'},
			createdDate: {value: '', label: 'Start / End Date'},
			modifiedBy: {value: '', label: 'Mdified By'},
		};
	}
	ngOnInit() {
		let loc = localStorage.getItem("venueId");
		if (!loc && this.criteria.venueId != '') {
			localStorage.setItem("venueId", this.criteria.venueId);
		} else {
			this.criteria.venueId.value = loc;
		}
		this.tableHeight = (window.innerHeight - 225) + 'px';
		this.getLstVenue();
		this.getCounterByStatus();
		super.defaultOnInit();
	}

	getLstVenue() {
		return this.prvTicket.findLstVenue().subscribe(
			data => {
				this.lstEvent = data;
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
		this.searchLabel = '';

		this.criteria.createdDate.value = '';
		if (this.strDate) {
			let createdDate = this.strDate;
			if (this.endDate) {
				createdDate = createdDate + '..' + this.endDate;
			}
			this.criteria.createdDate.value = createdDate;
		} else if (this.endDate) {
			this.srvUtil.showDialogError('Jika Start Date diisi, End Date juga harus diisi!');
		}

		for (var k in this.criteria) {
			if (this.criteria[k].value) {
				query.tableCriteria.push(new TableCriteria(k, this.criteria[k].value));
				this.searchLabel = this.searchLabel + ' [' + this.translate.instant(this.criteria[k].label) + ' : ' + this.criteria[k].value + ']';
			}
		}
		return query;
	}

	getData(query: TableQuery) {
		return this.prvBooking.find(this.paginationObject.startIndex, this.paginationObject.maxRows, query);
	}

	reload() {
		if (!this.strDate && this.endDate) {
			this.srvUtil.showDialogError('Jika Start Date diisi, End Date juga harus diisi!');
		} else {
			super.reload();
		}
	}

	edit() {
		if (this.selected && this.selected.length > 0) {
			this.router.navigate(['../' + this.selected[0].id], {relativeTo: this.route});
		}
	}

	approve() {
		if (this.selected[0].status == 'APPROVED') {
			this.srvUtil.showDialogError('Data yang anda pilih sudah di Approve!');
		} else if (this.selected[0].status == 'CANCELLED') {
			this.srvUtil.showDialogError('Data yang anda pilih sudah di Cancel oleh sistem!');
		} else if (this.selected && this.selected.length > 0) {
			this.srvUtil.showDialogConfirm(
				'<strong>' + "Dengan klik Approve, sistem akan mengirim tiket kepada pembeli. Approve?",
				(answer) => {
					if (answer === 'yes') {
						this.model = this.selected[0];
						this.saving = true;
						this.PanictUtil.showRequestIndicator();
						this.prvBooking.approve(this.model.id).subscribe(
							(data: any) => {
								this.PanictUtil.hideRequestIndicator();
								this.PanictUtil.showAlertSuccess(this.translate.instant('data.saved'));
								this.reloadFirstPage();
								this.saving = false;
								this.insertMode = false;
							},
							error => {
								this.srvUtil.showDialogInfo('Oops! ' + error.error.parameters[0]);
								this.saving = false;
							}
						)
					}
				});
		}
	}

	getBookingData() {
		this.prvBooking.findById(this.selected[0].id).subscribe(
			data => {
				data;
				this.bookingData = data;
				this.PanictUtil.hideRequestIndicator();
			},
			error => {
				this.handleError(error, this.router);
			}
		);
	}

	getRowClass(row) {
		return {
			'status-is-approved': (row.status == 'APPROVED'),
			'status-is-canclled': (row.status == 'CANCELLED'),
			'status-is-confirm': (row.status == 'CONFIRM')
		};
	}

	buktiTrans(idBooking) {
		this.PanictUtil.showRequestIndicator();
		this.prvBooking.findById(this.selected[0].id).subscribe(
			data => {
				data;
				this.bookingData = data;
				console.log("data booking by id: " + this.bookingData);
				this.dialog.open(CmpBuktiTrans, {
					data: {idBooking: this.selected[0].id},
					width: '600px',
					disableClose: true
				});
				this.PanictUtil.hideRequestIndicator();
			},
			error => {
				this.handleError(error, this.router);
				//				this.dialogClosed();
			}
		);
	}

	exportData() {
		let query = this.getQueryObject();
		this.prvBooking.exportData(this.paginationObject.startIndex, this.paginationObject.maxRows, query).subscribe((res) => {
			window['saveAs'](res, 'laporan_penjualan.xlsx');
		}, error => {
			this.handleError(error, this.router);
		});
	}
	exportAllData() {
		let query = this.getQueryObject();
		this.prvBooking.exportAllData(query).subscribe((res) => {
			window['saveAs'](res, 'laporan_penjualan.xlsx');
		}, error => {
			this.handleError(error, this.router);
		});
	}

	getCounterByStatus() {
		// Get Approved Count
		this.prvBooking.countBookingStatusAll('APPROVED').subscribe(
			(data: any) => {
				this.countApproved = data;
			}
		);
		// Get Cancelled Count
		this.prvBooking.countBookingStatusAll('CANCELLED').subscribe(
			(data: any) => {
				this.countCanceled = data;
			}
		);
		// Get Confirm Count
		this.prvBooking.countBookingStatusAll('CONFIRM').subscribe(
			(data: any) => {
				this.countConfirm = data;
			}
		);
		// Get Pending Count
		this.prvBooking.countBookingStatusAll('PENDING').subscribe(
			(data: any) => {
				this.countPending = data;
			}
		);
	}

	public onChange(event): void {
		const venueId = event.target.value;
		this.prvBooking.countBookingStatus(venueId,'APPROVED').subscribe(
			(data:any) => {
				this.countApproved = data;
			}
		);
		this.prvBooking.countBookingStatus(venueId,'CANCELLED').subscribe(
			(data:any) => {
				this.countCanceled = data;
			}
		);
		this.prvBooking.countBookingStatus(venueId,'CONFIRM').subscribe(
			(data:any) => {
				this.countConfirm = data;
			}
		);
		this.prvBooking.countBookingStatus(venueId,'PENDING').subscribe(
			(data:any) => {
				this.countPending = data;
			}
		);
		super.reload();
	}
}
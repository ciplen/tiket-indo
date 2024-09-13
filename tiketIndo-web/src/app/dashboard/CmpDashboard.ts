import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';
import {PrvBookingTicket} from '../bookingTicket/PrvBookingTicket';
import {ChartType, ChartOptions} from 'chart.js';
import {SingleDataSet, Label} from 'ng2-charts';
//import * as pluginLabels from 'chartjs-plugin-labels';
import * as pluginLabels from 'chartjs-plugin-piechart-outlabels';

@Component({
	selector: 'dashboard',
	templateUrl: 'CmpDashboard.html'
})

export class CmpDashboard extends BaseComponent implements OnInit {
	prefix = 'CmpDashboard';
	parameter: any;
	canvas: any;
	ctx: any;
	countApproved: any;
	countCanceled: any;
	countInProgress: any;
	countPending: any;
	countConfirm: any;
	listCountAge = [];
	listAge = [];
	listCountKota = [];
	listKota = [];
	sumTotal: any;
	chart: any;
	lstEvent: any;
	myChart: any;
	myChart2: any;
	venueId: any;
	pieChartOptions: ChartOptions;
	pieChartLabels: Label[];
	pieChartData: SingleDataSet;
	pieChartType: ChartType;
	pieChartType2: Chart
	pieChartLegend: boolean;
	pieChartPlugins = [];

	@Output() reload: EventEmitter<any> = new EventEmitter();
	constructor(
		private router: Router,
		private prvTicket: PrvTicketMaint,
		private prvBooking: PrvBookingTicket,
		translate: TranslateService) {
		super(translate);
	}

	ngOnInit() {
		this.pieChartOptions = this.createOptions();
		this.pieChartLabels = ['Approved', 'Canceled', 'In Progress'];
		this.pieChartData = [0, 0, 0];
		//this.listCountAge = [];
		//this.listCountKota = [];
//		this.pieChartType = 'outlabeledPie';
		//this.pieChartType2 = 'pie';
		//this.pieChartLegend = true;
		this.pieChartPlugins = [pluginLabels];
		this.getLstVenue();
	}

	getLstVenue() {
		return this.prvTicket.findLstVenue().subscribe(
			data => {
				this.lstEvent = data;
			}
		);
	}

	async onChange(event) {
		this.venueId = event.target.value;
		this.countInProgress = (this.countConfirm + this.countPending);

		// PieChart Status Penjualan
		await this.fetchData(this.venueId);
		this.countInProgress = (+this.countConfirm + +this.countPending);
		this.pieChartData = [this.countApproved, this.countCanceled, this.countInProgress]

		// PieChart Penjualan By Age
		await this.fetchDataByAge(this.venueId);
		//this.dummy();
		// PieChart Penjualan By Kota
		this.fetchDataByKota(this.venueId);

	}

	chartPenjualanLoad(approved: number, canceled: number, inprogress: number, init: boolean) {
		this.pieChartData = [approved, canceled, inprogress];
	}

	private async fetchData(venueId) {
		const approved = await this.prvBooking.countBookingStatus(venueId, 'APPROVED').toPromise();
		const canceled = await this.prvBooking.countBookingStatus(venueId, 'CANCELLED').toPromise();
		const confirm = await this.prvBooking.countBookingStatus(venueId, 'CONFIRM').toPromise();
		const pending = await this.prvBooking.countBookingStatus(venueId, 'PENDING').toPromise();
		this.countApproved = JSON.stringify(approved);
		this.countCanceled = JSON.stringify(canceled);
		this.countConfirm = JSON.stringify(confirm);
		this.countPending = JSON.stringify(pending);
	}

	private fetchDataByAge(venueId) {
		this.prvBooking.countBookingByAge(venueId).subscribe(
			(data: any) => {
				let umur = data.map(function (item) {
					return item.umur;
				});
				let count = data.map(function (item) {
					return item.count;
				});
				// Label Umur
				var listUmur = [];
				for (var i = 0; i < umur.length; i++) {
					if (i <= 4) {
						listUmur.push(umur[i] + ' Tahun');
					}
				}
				listUmur.push('Other');
				this.listAge = listUmur;

				// Count Umur
				var listSumUmur = [];
				var nilai = 0;
				for (var i = 0; i < count.length; i++) {
					if (i > 4) {
						nilai = (nilai + +count[i])
					} else {
						listSumUmur.push(count[i]);
					}
				}
				listSumUmur.push(nilai);
				this.listCountAge = listSumUmur;
			}
		);
	}

	private fetchDataByKota(venueId) {
		this.prvBooking.countBookingByKota(venueId).subscribe(
			(data: any) => {
				let kota = data.map(function (item) {
					return item.kotaAsal;
				});
				let count = data.map(function (item) {
					return item.count;
				});

				// Label Kota
				var listKota = [];
				for (var i = 0; i < kota.length; i++) {
					if (i <= 4) {
						listKota.push(kota[i]);
					}
				}
				listKota.push('Other');
				this.listKota = listKota;

				// Count Umur
				var listSumKota = [];
				var nilai = 0;
				for (var i = 0; i < count.length; i++) {
					if (i > 4) {
						nilai = (nilai + +count[i])
					} else {
						listSumKota.push(count[i]);
					}
				}
				listSumKota.push(nilai);
				this.listCountKota = listSumKota;
			}
		);
	}

	pieColors = [
		{
			backgroundColor: ['#00cc66', '#ff6666', '#99ccff']
		}
	];

	pieColors2 = [
		{
			backgroundColor: ['#00cc66', '#ff6666', '#99ccff', '#ffff4d', '#b3b300', '#344ceb']
		}
	];

	private createOptions(): ChartOptions {
		return {
			responsive: true,
			maintainAspectRatio: true,
			legend: {
				position: 'bottom'
			},
			plugins: {
				// labels: {
				// 	render: 'percentage',
				// 	fontColor: ['black', 'black', 'black'],
				// 	precision: 2,
				// 	position: 'outside',
				// 	arc: true,
				// },
				zoomOutPercentage: 55,
				legend: false,
				outlabels: {
					text: '%l %p',
					color: 'black',
					stretch: 30,
					backgroundColor: 'white',
					font: {
						resizable: true,
						minSize: 12,
						maxSize: 18
					}
				}
			},
		};
	}
}

import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {PrvTicketMaint} from '../../ticketMaint/PrvTicketMaint'
import {TableQuery} from '../../shared/TabelQuery';
import {SrvUtil} from 'app/shared/SrvUtil';
import {Config} from '../../shared/index';

class Model {
	id: number;
	venueId: number;
	type: string;
	amount: number;
	jumlahTerjual: number;
	tanggalPeriode1: string;
	discountPrice: string;
	hargaPeriode1: string;
}

@Component({
	selector: 'buyTic',
	templateUrl: 'CmpBuyTicket.html',
	styleUrls: ['./CmpBuyTicket.scss'],
})

export class CmpBuyTicket implements OnInit {
	model: Model;
	lstData: any[] = [];
	page: number = 0;
	allData: any;
	count: number;
	id: number;
	type: string;
	tgl: string;
	tem: string;
	lok: string;
	pict: string;
	pictBg: string;
	getSmallBannerUrl = Config.API + 'vm/pict/big/';
	getBigBannerUrl = Config.API + 'vm/pict/big/';
	disabled = false;

	namaEvent: any;
	tempat: any;
	tanggalAwal: any;

	constructor(private prvTicket: PrvTicketMaint,
		private srvUtil: SrvUtil,
		private router: Router,
		private route: ActivatedRoute,
		private prvTicketMaint: PrvTicketMaint,
		//		route: ActivatedRoute, translate: TranslateService,
		titleService: Title) {
	}

	checkAvailability(id: any) {
		this.prvTicketMaint.checkAvailableTicket(id).subscribe(
			data => {
				this.router.navigateByUrl('/quantityForm/' + id);
			}, error => {
				this.srvUtil.showDialogError('Oops! ' + error.error.parameters[0]);
			}
		)
	}

	ngOnInit() {
		this.id = this.route.snapshot.params['id'];
		console.log("========vanue id========" + this.id);
		//		this.getData();
		this.getSmallBannerUrl = Config.API + 'vm/pict/small/' + this.id;
		this.getBigBannerUrl = Config.API + 'vm/pict/big/' + this.id;
		this.selectData();
	}

	showModelPanel = false;
	toggleModel() {
		this.showModelPanel = !this.showModelPanel;
		var modal = document.getElementById("myModal");
		if (modal.style.display === "block") {
			modal.style.display = "none";
		} else {
			modal.style.display = "block";
		}
	}

	selectData() {
		this.prvTicketMaint.findTicketByVenueId(this.id).subscribe(
			data => {
				this.allData = data;
			},
			error => {
				console.log("error")
			})
	}

	alert() {
		var x = document.getElementById("toggle");
		if (x.style.display === "block") {
			x.style.display = "none";
		} else {
			x.style.display = "none";
		}
	}

	getData() {
		let query = new TableQuery;
		this.prvTicket.find(0, 20, query).subscribe(
			(data: any) => {
				this.lstData = data.rows;
				for (let k of this.lstData) {
					if (k.venue.id == this.id) {
						this.allData.push(k);
						this.type = k.type;
						this.tgl = k.tanggalPeriode5;
						console.log("data yang di inginkan : " + this.allData)
					}
				}
				this.count = this.lstData.length;
			},
			error => {
				console.log("error")
			})
	}
}
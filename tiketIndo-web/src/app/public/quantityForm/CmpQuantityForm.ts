import {Component, OnInit, ElementRef, HostListener, EventEmitter, Output} from '@angular/core';
import {SrvMasterData} from '../../shared/SrvMasterData';
import {BaseComponent} from '../../shared/base.component';
import {TranslateService} from '@ngx-translate/core';
import {UserService} from '../../shared/user.service';
import {Router, ActivatedRoute} from '@angular/router';
import {trigger, state, transition, style, animate} from '@angular/animations';
import {Title} from '@angular/platform-browser';
import {Config} from '../../shared/index';
import {PrvTicketMaint} from '../../ticketMaint/PrvTicketMaint'
import {PrvVenueMaint} from '../../venueMaint/PrvVenueMaint'
import {NgForm} from '@angular/forms';
import {SrvUtil} from 'app/shared/SrvUtil';

class Model {
	id: number;
	venueId: number;
	maintId: number;
	type: string;
	description: string;
	amount: number;
	total: number;
	value: number;
	jumlahTerjual: number;
	tanggalPeriode1: string;
	hargaPeriode1: string;
	discountPrice: string;
	tanggalAwal: any;
}
@Component({
	selector: 'quantityForm',
	templateUrl: 'CmpQuantityForm.html',
	styleUrls: ['./CmpQuantityForm.scss'],
})

export class CmpQuantityForm implements OnInit {
	myForm: NgForm;
	id: number;
	idVenue: number;
	discountData: any;
	minDiskon: number;
	notifDscnt: string;
	description: string;
	model: Model;
	qty = '1';
	nama: any;
	tempat: any;
	typeVenue: any;
	bolTypeVenue: boolean = false;
	venueData: any;
	save: any;
	total: any;
	discount: number;
	type: any;
	hargaPeriode1: any;
	discountPrice: any;
	disable: any;
	tgl: any;
	pict: string;

	constructor(public el: ElementRef,
		private route: ActivatedRoute,
		private prvTicket: PrvTicketMaint,
		private prvVenueMaint: PrvVenueMaint,
		private prvTicketMaint: PrvTicketMaint,
		private router: Router,
		private srvUtil: SrvUtil,
		translate: TranslateService) {
	}

	ngOnInit() {
		this.id = this.route.snapshot.params['id'];
		this.getData();
		this.pict = null;
		this.getDiscount();
	}

	getDiscount() {
		this.prvTicketMaint.getDiscount(this.id, this.discountData).subscribe(
			data => {
				this.discountData = data;
				if (this.discountData) {
					this.discount = this.discountData.diskon;
					this.minDiskon = this.discountData.minOrder;
				} else {
					this.discount = 0;
					this.notifDscnt = "TIDAK ADA DISKON";
				}
			},
			error => {
				console.log("error");
			});
	}

	getData() {
		this.prvTicket.findById(this.id).subscribe(
			(data: Model) => {
				this.model = data;
				this.total = this.model.hargaPeriode1;
				this.tgl = this.model.tanggalAwal;
				this.type = this.model.type;
				this.description = this.model.description;
				this.idVenue = this.model.venueId;
				this.hargaPeriode1 = this.model.hargaPeriode1;
				this.getVenueMain(this.idVenue);
			},
			error => {
				console.log("error");
			});
	}

	checkAvailability() {
		this.prvTicketMaint.checkAvailability(this.model.type, +this.qty).subscribe(
			data => {
				this.router.navigateByUrl('/ticketForm/' + this.model.id + '/' + this.qty);
			}, error => {
				this.srvUtil.showDialogError('Oops!' + error.error.parameters[0]);
			}
		)
	}
	alert() {
		var x = document.getElementById("toggle");
		if (x.style.display === "block") {
			x.style.display = "none";
		} else {
			x.style.display = "none";
		}
	}
	submit() {
		this.checkAvailability();
	}

	getVenueMain(id: any) {
		this.prvVenueMaint.findById(this.idVenue).subscribe(
			(data) => {
				this.venueData = data;
				this.nama = this.venueData.nama;
				this.tempat = this.venueData.tempat;
				this.typeVenue = this.venueData.typeVenue;
				if (this.typeVenue == 'Wisata') {
					this.bolTypeVenue = true;
				}
				this.hargaPeriode1 = this.model.hargaPeriode1;
				//				this.discountPrice = this.model.discountPrice;
				console.log("data dari venue id : " + this.venueData);
			},
			error => {
				console.log("error")
			});
	}

	onInputChange($event) {
		var harga = parseInt(this.model.hargaPeriode1);
		this.total = +this.qty * harga;
	}

	click() {
		var x = document.getElementById("toggleAlert");
		if (x.style.display === "none") {
			x.style.display = "block";
		} else {
			x.style.display = "none";
		}

		var a = document.getElementById("togglePolice");
		if (a.style.display === "none") {
			a.style.display = "block";
		} else {
			a.style.display = "none";
		}
	}

	//	privacy() {	
	//		var x = document.getElementById("privacy");	
	//		if (x.style.display === "none") {	
	//			x.style.display = "block";	
	//		} else {	
	//			x.style.display = "none";	
	//		}	
	//	}

}

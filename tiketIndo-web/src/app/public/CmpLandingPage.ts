import {Component, ElementRef, HostListener, OnInit} from '@angular/core';
import {PrvVenueMaint} from '../../app/venueMaint/PrvVenueMaint'
import {Router, ActivatedRoute} from '@angular/router';
import {SrvUtil} from 'app/shared/SrvUtil';
import {PrvTicketMaint} from '../../app/ticketMaint/PrvTicketMaint';
import {ScrollToService, ScrollToConfigOptions} from '@nicky-lenaers/ngx-scroll-to';
import {Config} from '../shared/index';

class Model {
	id: number;
	nama: string;
	tempat: string;
	tanggalAwal: string;
	tanggalAkhir: string;
}

@Component({
	selector: 'landingPage',
	templateUrl: 'CmpLandingPage.html',
	styleUrls: ['./style.css'],
})

export class CmpLandingPage implements OnInit {
	state = 'hide';
	left = 'left';
	second: number;
	model: Model;
	minute: number;
	hour: number;
	today: any;
	idVenue: any;
	upComingVenue: any;
	venue: any;
	lstVenueData: any;
	lstValidateVenue: any;
	nama: any;
	emptyEvent: boolean = false;
	emptyWisata: boolean = false;
	idVen: any;
	upComingEvent: any;
	typeVenue: any;
	tempat: any;
	tanggal: any;
	jump: any;
	day: number;
	automaticSlideTimeout;
	slideTime = 5000;
	getSmallBannerUrl = Config.API + 'vm/pict/small/';
	getBigBannerUrl = Config.API + 'vm/pict/big/';

	constructor(public el: ElementRef,
		private router: Router,
		private _scrollToService: ScrollToService,
		private srvUtil: SrvUtil,
		private prvTicketMaint: PrvTicketMaint,
		private prvVenueMaint: PrvVenueMaint) {}

	ngOnInit() {
		this.today = Date.now();
		this.getUpComingEvent();
		this.lstVenue();
		this.validatingVenue();
	}

	public triggerScrollTo() {
		const config: ScrollToConfigOptions = {
			target: 'destination'
		};

		this._scrollToService.scrollTo(config);
	}

	getUpComingEvent() {
		this.prvVenueMaint.findUpComingVenue(this.upComingEvent).subscribe(
			(data: any) => {
				this.upComingVenue = data;
				if (this.upComingVenue == "") {
					this.emptyEvent = true;
				}
			},
			error => {
				console.log("error")
			});
	}

	lstVenue() {
		this.prvVenueMaint.getDataVenue(this.model).subscribe(
			(data) => {
				this.lstVenueData = data;
			},
			error => {
				console.log("error")
			});
	}

	validatingVenue() {
		this.prvVenueMaint.getValidateVenue(this.model).subscribe(
			(data) => {
				this.lstValidateVenue = data;
				if (this.lstValidateVenue == "") {
					this.emptyWisata = true;
				}
			},
			error => {
				console.log("error")
			});
	}

	checkWisataTicket(id: any) {
		this.prvTicketMaint.findTicketByVenueId(id).subscribe(
			data => {
				this.venue = data;
				if (this.venue.tanggalAwal < Date.now()) {
					console.log("Data yang di minta kosong");
				}
				this.router.navigateByUrl('/buyTic/' + id);
			}, error => {
				this.srvUtil.showDialogError('Oops! ' + error.error.parameters[0]);
			}
		)
	}

	checkAvailability(id: any) {
		this.prvTicketMaint.findTicketByVenueId(id).subscribe(
			data => {
				this.venue = data;
				if (this.venue.tanggalAwal < Date.now()) {
					console.log("Data yang di minta kosong");
				}
				this.router.navigateByUrl('/buyTic/' + id);
			}, error => {
				this.srvUtil.showDialogError('Oops! ' + error.error.parameters[0]);
			}
		)
	}

	@HostListener('window:scroll', ['$event'])
	onWindowScroll(e) {
		if (window.pageYOffset > 130) {
			let element = document.getElementById('Scroll');
			element.classList.add('sticky');
		} else {
			let element = document.getElementById('Scroll');
			element.classList.remove('sticky');
		}
		const componentPosition = this.el.nativeElement.offsetTop
		const scrollPosition = window.pageYOffset

		if (scrollPosition >= componentPosition) {
			this.state = 'show'
			this.left = 'left'
		} else {
			this.state = 'hide'
		}
	}

	showProfilePanel = false;
	toggleProfilePanel() {
		this.showProfilePanel = !this.showProfilePanel;
		var x = document.getElementById("myLinks");
		if (x.style.display === "block") {
			x.style.display = "none";
		} else {
			x.style.display = "block";
		}
	}

	showClosedEvent = false;
	toggleModel() {
		this.showClosedEvent = !this.showClosedEvent;
		var modal = document.getElementById("myModal");
		if (modal.style.display === "block") {
			modal.style.display = "none";
		} else {
			modal.style.display = "block";
		}
	}

	toggleModel2() {
		this.showClosedEvent = !this.showClosedEvent;
		var modal = document.getElementById("myModal2");
		if (modal.style.display === "block") {
			modal.style.display = "none";
		} else {
			modal.style.display = "block";
		}
	}

	getCurrentSlideIndex() {
		let slides = document.getElementsByClassName("mySlides") as HTMLCollectionOf<HTMLElement>;
		let currentIndex = 0;

		for (let i = 0; i < slides.length; i++) {
			let element = slides[i];
			if (element.classList.contains('slider-slide-in')) {
				currentIndex = i;
				break;
			}
		}
		return currentIndex;
	}


	showClosed = false;
	toggleClosedEvent() {
		this.showClosed = !this.showClosed;
		var modal = document.getElementById("closedEvent");
		if (modal.style.display === "block") {
			modal.style.display = "none";
		} else {
			modal.style.display = "block";
		}
	}

	nextSlide() {
		let currentIndex = this.getCurrentSlideIndex();
		this.showSlides(currentIndex + 1, currentIndex);
	}

	prevSlide() {
		let currentIndex = this.getCurrentSlideIndex();
		this.showSlides(currentIndex - 1, currentIndex);
	}

	showSlides(slideIndex, currentIndex) {
		clearTimeout(this.automaticSlideTimeout);
		console.log('slide index ' + slideIndex);
		var i;
		var slides = document.getElementsByClassName("mySlides") as HTMLCollectionOf<HTMLElement>;
		if (slideIndex >= slides.length) {slideIndex = 0}
		if (slideIndex < 0) {slideIndex = slides.length - 1}
		for (i = 0; i < slides.length; i++) {
		}
		for (i = 0; i < slides.length; i++) {
			slides[i].className = slides[i].className.replace(" slider-slide-out", "");
		}
		slides[currentIndex].className = slides[currentIndex].className.replace("slider-slide-in", "slider-slide-out");
		slides[slideIndex].className += " slider-slide-in";
		this.automaticSlideTimeout = setTimeout(() => this.nextSlide(), this.slideTime);
	}

}

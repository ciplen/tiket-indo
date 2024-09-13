import {Component, OnInit, AfterViewInit} from '@angular/core';
import {PanictUtil} from '../shared/panict.util';
import {Alert} from '../shared/alert.component';
import {Router} from '@angular/router';
import {UserService} from '../shared/user.service';
import {SrvMasterData} from '../shared/SrvMasterData';
import {BaseComponent} from '../shared/base.component';
import {Config} from '../shared/index';
import {TranslateService} from '@ngx-translate/core';

declare var jQuery: any;

@Component({
	selector: 'cmp-home',
	templateUrl: 'CmpHome.html',
	styleUrls: ['./CmpHome.scss']
})

export class CmpHome extends BaseComponent implements OnInit, AfterViewInit {
	apiUrl = Config.API;
	minified = false;
	username: string;
	role: string;
	nama: string;
	balance: any;
	userId: number;
	message: Alert;
	requesting: boolean;
	alertTimeout: any;
	saldo: number;
	testMode = false;
	loadingProgress = 15;
	lang: string;
	authInfo: any;

	canReadUser = false;
	canReadRole = false;
	canReadCountry = false;
	canReadCity = false;
	canMaintainCountry = false
	canMaintainCity = false
	prefix = 'CmpHome';
	isAdmin = false;
	isOwner = false;
	isApprover = false;
	canVerify=false;
	constructor(private userService: UserService, private router: Router,
		private srvMasterData: SrvMasterData, translate: TranslateService) {
		super(translate);
		this.authInfo = JSON.parse(localStorage.getItem('auth_info'));
		this.isAdmin = this.srvMasterData.isAdmin();
		this.isOwner = this.srvMasterData.hasRole('owner');
		this.canReadUser = this.srvMasterData.isAdmin()
			|| this.srvMasterData.hasPermission('user:read')
			|| this.srvMasterData.hasPermission('user:write');
		this.canReadRole = this.srvMasterData.isAdmin()
			|| this.srvMasterData.hasPermission('role:read')
			|| this.srvMasterData.hasPermission('role:write');
		this.canReadCountry = this.srvMasterData.hasPermission('country:read');
		this.canReadCity = this.srvMasterData.hasPermission('city:read');
		this.canMaintainCountry = this.srvMasterData.hasPermission('country:write');
		this.canMaintainCity = this.srvMasterData.hasPermission('city:write');
		this.isApprover = this.srvMasterData.hasRole('approver');
		this.canVerify = this.srvMasterData.hasRole('gate_keeper') || this.srvMasterData.hasRole('approver')|| this.srvMasterData.hasRole('admin');
		this.srvMasterData.checkAuth().subscribe(
			data => {},
			error => this.handleError(error, this.router)
		)
	}

	ngOnInit() {
		this.lang = this.translate.getDefaultLang();
		this.username = this.srvMasterData.getUsername();
		this.role = this.srvMasterData.getRole();
		this.nama = this.srvMasterData.getName();
		this.userId = this.srvMasterData.getUserId();
		this.testMode = this.srvMasterData.isTestMode();
		this.minified = this.srvMasterData.getDefaultParameter(this.prefix, null, 'minified') === 'true';

		PanictUtil.getAlertObservable().subscribe(data => {
			this.message = data;
			if (this.alertTimeout) {
				clearTimeout(this.alertTimeout);
			}
			this.alertTimeout = undefined;
			if (this.message && (this.message.type === 'success' || this.message.type === 'info')) {
				this.alertTimeout = setTimeout(() => PanictUtil.hideAlert(), 5000);
			}
		});

		PanictUtil.getRequestObservable().subscribe(
			data => setTimeout(() => {
				if (this.requesting === data) {
					if (this.requesting) {
						this.start();
					}
				} else {

					this.requesting = data;

					if (this.requesting) this.start(); else this.complete();
				}
			}
				, 0));

	}


	alertClosed() {
		PanictUtil.hideAlert();
	}

	ngAfterViewInit() {
		var bodyHeight = window.innerHeight - 120;
		var bodyWidth = window.innerWidth;
		let x = document.getElementById('side-menu');
		let pageWrapper = document.getElementById('page-wrapper');
		if (x && bodyWidth >= 768) {
			x.style.height = bodyHeight + 'px';
		}
		pageWrapper.style.minHeight = (window.innerHeight - 55) + 'px';
	}


	toggleMenu() {
		this.minified = !this.minified;
		this.srvMasterData.saveDefaultParameter(this.prefix, 'minified', this.minified + '');

		jQuery('.sidebar .nav-item.dropdown').children('ul').collapse('hide');
	}

	menuClicked($event: MouseEvent) {
		if (!this.minified) {
			jQuery($event.currentTarget).siblings('ul').collapse('toggle');
			jQuery($event.currentTarget).toggleClass('collapsed');
		}
		$event.preventDefault();
	}

	logout() {
		this.userService.logout();
		this.router.navigateByUrl('/login');
	}

	private _intervalCounterId: any = 0;
	public interval: number = 500; // in milliseconds
	show = false;
	start() {
		// Stop current timer
		this.stop();
		// Make it visible for sure
		this.loadingProgress = 15;
		this.show = true;
		// Run the timer with milliseconds iterval
		this._intervalCounterId = setInterval(() => {
			// Increment the progress and update view component
			this.loadingProgress = this.loadingProgress + 3;
			// If the progress is 100% - call complete
			if (this.loadingProgress > 90) this.loadingProgress = 90;
		}, this.interval);
	}

	stop() {
		if (this._intervalCounterId) {
			clearInterval(this._intervalCounterId);
			this._intervalCounterId = null;
		}
	}

	complete() {
		this.loadingProgress = 100;
		this.stop();
		setTimeout(() => {
			this.show = false;
			setTimeout(() => {
				// Drop to 0
				this.loadingProgress = 15;
			}, 250);
		}, 250);
	}

	changeLanguage(val: string) {
		localStorage.setItem('lang', val);
		//		this.router.navigateByUrl('/');
		window.location.replace(window.location.href);
	}
}

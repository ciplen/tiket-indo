import {Component, ViewChild} from '@angular/core';
import {Nav, Platform} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';
import {HomePage} from '../pages/home/home';
import {CmpLogin} from '../pages/login/CmpLogin';
import {CmpGenerateTicket} from '../pages/generateTicket/CmpGenerateTicket';
import {CmpScanner} from '../pages/scanner/CmpScanner';
import {CmpSellingList} from '../pages/sellingList/CmpSellingList';

@Component({
	templateUrl: 'app.html'
})
export class MyApp {
	@ViewChild(Nav) nav: Nav;
	rootPage: any = CmpLogin;
	pages: Array<{title: string, component: any, icon: string}>;

	constructor(public platform: Platform, public statusBar: StatusBar, public splashScreen: SplashScreen) {
		this.initializeApp();
		this.pages = [
			{title: 'Generate Ticket', component: CmpGenerateTicket, icon: 'md-create'},
			{title: 'Selling List', component: CmpSellingList, icon: 'md-list'},
			{title: 'Scanner', component: CmpScanner, icon: 'qr-scanner'},
		];
		if (localStorage.getItem('auth_info')) {
			this.rootPage = HomePage;
		}
	}

	initializeApp() {
		this.platform.ready().then(() => {
			// Okay, so the platform is ready and our plugins are available.
			// Here you can do any higher level native things you might need.
			this.statusBar.backgroundColorByHexString('#002752');
			this.splashScreen.hide();
		});
	}

	openPage(page) {
		// Reset the content nav to have just this page
		// we wouldn't want the back button to show in this scenario
		this.nav.push(page.component);
	}
	logout() {
		localStorage.clear();
		location.reload();
	}
}

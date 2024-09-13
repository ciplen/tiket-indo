import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {IonicApp, IonicErrorHandler, IonicModule} from 'ionic-angular';
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClient} from '@angular/common/http';
import {MyApp} from './app.component';
import {HomePage} from '../pages/home/home';
import {CmpLogin} from '../pages/login/CmpLogin';
import {ListPage} from '../pages/list/list';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';
import {PrvAuth} from '../providers/PrvAuth';
import {PrvTicket} from '../providers/PrvTicket';
import {MdlGenerateTicket} from '../pages/generateTicket/MdlGenerateTicket';
import {MdlSellingList} from '../pages/sellingList/MdlSellingList';
export function createTranslateLoader(http: HttpClient) {}
import {DefaultHttpInterceptor} from '../providers/DefaultHttpInterceptor';
import {BarcodeScanner} from '@ionic-native/barcode-scanner';
import {NgxQRCodeModule} from 'ngx-qrcode2';
import {MdlScanner} from '../pages/scanner/MdlScanner';
@NgModule({
	declarations: [
		MyApp,
		HomePage,
		ListPage,
		CmpLogin
	],
	imports: [
		BrowserModule,
		NgxQRCodeModule,
		HttpClientModule,
		IonicModule.forRoot(MyApp),
		MdlGenerateTicket,
		MdlScanner,
		MdlSellingList
	],
	bootstrap: [IonicApp],
	entryComponents: [
		MyApp,
		HomePage,
		ListPage,
		CmpLogin
	],
	providers: [
		StatusBar,
		SplashScreen,
		BarcodeScanner,
		PrvAuth,
		{provide: ErrorHandler, useClass: IonicErrorHandler},
		{provide: HTTP_INTERCEPTORS, useClass: DefaultHttpInterceptor, multi: true},
		PrvTicket
	]
})
export class AppModule {}

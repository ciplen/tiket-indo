import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

/* App Root */
import {AppComponent} from './app.component';

/* Routing Module */
import {AppRoutingModule} from './app-routing.module';
import {MdlHome} from './home/MdlHome';
import {MdlDashboard} from './dashboard/MdlDashboard';
import {MdlShared} from './shared/MdlShared';
import {MdlPublic} from './public/MdlPublic';
import {MdlVisitor} from './visitor/MdlVisitor';
import {LoginRoutingModule} from './login/login-routing.module';
import {LoginComponent} from './login/login.component';
import {PageNotFoundComponent} from './not-found.component';
import {HttpClientModule, HttpClient} from '@angular/common/http';
import {NgxCaptchaModule} from 'ngx-captcha';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

export function HttpLoaderFactory(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
	imports: [TranslateModule.forRoot({
		loader: {
			provide: TranslateLoader,
			useFactory: HttpLoaderFactory,
			deps: [HttpClient]
		}
	}), BrowserModule, AppRoutingModule, NgxCaptchaModule, BrowserAnimationsModule,
		HttpClientModule, AppRoutingModule, LoginRoutingModule,MdlVisitor,  MdlPublic, MdlHome, MdlDashboard, MdlShared.forRoot()
	],
	declarations: [AppComponent, PageNotFoundComponent, LoginComponent],
	bootstrap: [AppComponent]
})
export class AppModule {
	constructor() {
		console.log('href: ' + location.href);
	}
}

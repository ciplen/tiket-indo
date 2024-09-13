import {Component, OnInit} from '@angular/core';
import {Config} from './shared/index';
import {TranslateService} from '@ngx-translate/core';
/**
 * This class represents the main application component.
 */
@Component({
	moduleId: module.id,
	selector: 'sd-app',
	templateUrl: 'app.component.html'
})

export class AppComponent {
	constructor(translate: TranslateService) {

		// this language will be used as a fallback when a translation isn't found in the current language
		let lang = localStorage.getItem('lang');
		if (lang !== 'id') {
			lang = 'en';
		}
		translate.setDefaultLang(lang);

		// the lang to use, if the lang isn't available, it will use the current loader to get them
		translate.use(lang);
	}

}

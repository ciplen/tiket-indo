import {Component} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {TranslateService} from '@ngx-translate/core';

@Component({
	templateUrl: './CmpHelp.html',
})

export class CmpHelp extends BaseComponent {
	constructor(translate: TranslateService) {
		super(translate);
	}

}
import {Component, OnInit, ElementRef, HostListener} from '@angular/core';
import {SrvMasterData} from '../shared/SrvMasterData';
import {BaseComponent} from '../shared/base.component';
import {TranslateService} from '@ngx-translate/core';
import {UserService} from '../shared/user.service';
import {Router} from '@angular/router';
import {trigger, state, transition, style, animate} from '@angular/animations';
import {Title} from '@angular/platform-browser';
import {Config} from '../shared/index';

@Component({
	selector: 'cmp-footer',
	templateUrl: 'CmpFooter.html',
	styleUrls: ['./style.css'],
})

export class CmpFooter implements OnInit {
	today: any;

	constructor(public el: ElementRef) {}

	ngOnInit() {
		this.today = Date.now();
	}

}
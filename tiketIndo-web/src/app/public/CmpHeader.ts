import { Component, OnInit, ElementRef, HostListener } from '@angular/core';
import { SrvMasterData } from '../shared/SrvMasterData';
import { BaseComponent } from '../shared/base.component';
import { TranslateService } from '@ngx-translate/core';
import { UserService } from '../shared/user.service';
import { Router } from '@angular/router';
import { trigger, state, transition, style, animate } from '@angular/animations';
import { Title } from '@angular/platform-browser';
import { Config } from '../shared/index';

@Component({
	selector: 'cmp-header',
	templateUrl: 'CmpHeader.html',
	styleUrls: ['./CmpHeader.scss'],
})

export class CmpHeader implements OnInit {
	state = 'hide';
	left = 'left';
	constructor(public el: ElementRef) { }


	ngOnInit() { }

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

}
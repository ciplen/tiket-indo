import {Component} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {UserService} from '../shared/user.service';
import {TranslateService} from '@ngx-translate/core';
@Component({
	moduleId: module.id,
	selector: 'login',
	templateUrl: 'login.component.html',
	styleUrls: ['login.css']
})

export class LoginComponent {
	//	protected aFormGroup: FormGroup;
	errormsg: string;
	message: string;
	requesting = false;
	origin: string;
	constructor(
		private userService: UserService,
		private router: Router,
		private activatedRoute: ActivatedRoute,
		private translate: TranslateService
	) {}

	ngOnInit() {
		this.origin = this.activatedRoute.snapshot.params['origin'];
		if (!this.origin) {
			this.origin = '/home';
		} else {
			this.origin = decodeURIComponent(this.origin);
		}
	}

	handleExpire() {
		//		console.log('Captcha expired');
	}

	handleLoad() {
		//		console.log('handle load');
	}

	handleSuccess($event) {
		//		console.log(`Resolved captcha with response ${$event}:`);
	}

	login(event, username, password) {
		event.preventDefault();
		//		console.log('recaptch: ' + recaptcha);
		this.message = 'Trying to log in ...';
		this.errormsg = '';
		this.requesting = true;
		this.userService.login(username, password).subscribe((result) => {
			if (result) {
				this.requesting = false;
				let redirect = this.origin ? this.origin : '/home';
				if (redirect.startsWith('/login')) {
					redirect = '/home';
				}
				this.router.navigateByUrl(redirect);
			}
		}, error => {
			this.requesting = false;
			if (error.status == 401) {
				this.errormsg = this.translate.instant('incorrect.login');
			} else {
				if (error.text) {
					this.errormsg = error.text();
				} else {
					this.errormsg = error.error;
				}
			}
		});
	}
}
import {Component, OnInit, ViewChild, HostListener} from '@angular/core';
import {SrvMasterData} from '../../shared/SrvMasterData';
import {NgForm} from '@angular/forms';
import {BaseComponent} from '../../shared/base.component';
import {TranslateService} from '@ngx-translate/core';
import {UserService} from '../../shared/user.service';
import {Router, ActivatedRoute} from '@angular/router';
import {trigger, state, transition, style, animate, query} from '@angular/animations';
import {Title} from '@angular/platform-browser';
import {Config} from '../../shared/index';
import {PrvBookingTicket} from '../../bookingTicket/PrvBookingTicket'
import {BaseListComponent} from 'app/shared/BaseListComponent';
import {TableQuery, TableCriteria} from '../../shared/TabelQuery';
import {SrvUtil} from 'app/shared/SrvUtil';

class Model {
	codeUnique: number;
	idBooking: number;
	email: string;
	text: string;
	path_picture: any;
	file: any;
}

@Component({
	selector: 'confirm',
	templateUrl: 'CmpConfirm.html',
	styleUrls: ['./CmpConfirm.scss'],
})

export class CmpConfirm extends BaseComponent implements OnInit {
	@ViewChild('dashForm') currentForm: NgForm;
	apiUrl = Config.API;
	model: Model;
	email: string;
	path_picture: any;
	file: any;
	idBooking: number;

	saving = false;
	emptyImg = true;
	errormsg: string;
	imageEndPoint = '';
	url: any;
	@ViewChild('imgConfirm') imgConfirm: any;

	constructor(translate: TranslateService,
		private prvBooking: PrvBookingTicket,
		private srvUtil: SrvUtil,
		private router: Router,
		private route: ActivatedRoute) {
		super(translate);
	}

	ngOnInit() {
		this.idBooking = +this.route.snapshot.params['id'];
		this.email = this.route.snapshot.params['email'];
		this.url = this.apiUrl + 'confirm/pict/' + this.idBooking;
	}

	confirm() {
		if (this.saving) return;
		this.saving = true;
		console.log("kode unik: " + this.idBooking)
		console.log("email: " + this.email)

		if (this.idBooking == 0) {
			this.srvUtil.showDialogInfo('Silahkan inputkan Booking ID yang sudah dikirim ke Email Anda');
		} else if (this.email == 'email@email.com' || this.email == '') {
			this.srvUtil.showDialogError('Silahkan inputkan Email Anda');
		} else {
			this.prvBooking.getEmailAndId(this.idBooking, this.email).subscribe(
				(data: any) => {
					this.uploadbktTrans();
				}, error => {
					this.saving = false;
					this.srvUtil.showDialogInfo('Anda sudah melakukan konfirmasi. Tiket Anda sedang dalam proses pengecekan.');
				});
		}

	}

	onSelectFile(event) {
		this.emptyImg = true;
		if (event.target.files && event.target.files[0]) {
			var reader = new FileReader();

			reader.readAsDataURL(event.target.files[0]); // read file as data url
			this.path_picture = event.target.files[0];
			this.emptyImg = false;
			reader.onload = (event: any) => { // called once readAsDataURL is completed
				this.url = event.target.result;
			}
		}
	}

	uploadbktTrans() {
		this.prvBooking.uploadBktTrnfr(this.path_picture, this.idBooking).subscribe(
			data => {
				this.saving = false;
				this.srvUtil.showDialogInfo('Konfirmasi Anda sudah kami terima dan segera kami proses', (answer) => {
					console.log('navigate ke /');
					this.router.navigate(['/']);
				});
			}, error => {
				console.error(error);
				this.srvUtil.showDialogError(JSON.stringify(error));
			},
		);
	}


}
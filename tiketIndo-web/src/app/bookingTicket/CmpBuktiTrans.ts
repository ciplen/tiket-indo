import {Component, OnInit, ViewChild, Inject} from '@angular/core';
import {BaseComponent} from '../shared/base.component';
import {NgForm} from '@angular/forms';
import {Router, ActivatedRoute} from '@angular/router';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {TranslateService} from '@ngx-translate/core';
import {Config} from '../shared/index';
import {PrvBookingTicket} from '../bookingTicket/PrvBookingTicket'

class Model {
	id: number;
	namaPemesan: string;
	noKTP: string;
	path_picture: string;
	idBooking: number;
}

@Component({
	selector: 'cmp-buktiTrans',
	templateUrl: './CmpBuktiTrans.html',
})

export class CmpBuktiTrans extends BaseComponent implements OnInit {
	@ViewChild('dashForm') currentForm: NgForm;
	apiUrl = Config.API;
	saving = false;
	insertMode = false;
	model: Model;
	idBooking: any;
	id: number;
	dataBooking: any;
	idUnit: number;
	idReserv: number;
	countDay: any;
	url: any;
	noKtp: string;
	email: string;
	nama: string;
	status = '';
	expDay: any;
	//	@ViewChild('imgReserved') imgReserved: any;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private router: Router,
		private prvBooking: PrvBookingTicket,
		private route: ActivatedRoute,
		private dialogRef: MatDialogRef<CmpBuktiTrans>,
		translate: TranslateService) {
		super(translate);
		this.model = new Model;
	}
	ngOnInit(): void {
		this.idBooking = this.data.idBooking;
		this.id = this.route.snapshot.params['id'];
		this.getData();
	}


	close() {
		this.dialogRef.close();
	}

	transfer() {

	}

	onSubmit() {
	}

	getData() {
		this.prvBooking.getIdBooking(this.idBooking).subscribe(
			(data) => {
				this.dataBooking = data;
				this.idBooking = this.dataBooking.id;
				this.noKtp = this.dataBooking.noKtp;
				this.email = this.dataBooking.email;
				this.nama = this.dataBooking.nama;
				this.url = this.apiUrl + 'confirm/pict/' + this.dataBooking.id;
			},
			error => {
				console.log("error")
			});

	}

	onSelectFile(event) {
		if (event.target.files && event.target.files[0]) {
			var reader = new FileReader();

			reader.readAsDataURL(event.target.files[0]); // read file as data url
			this.model.path_picture = event.target.files[0];

			reader.onload = (event: any) => { // called once readAsDataURL is completed
				this.url = event.target.result;
			}
		}
	}

}
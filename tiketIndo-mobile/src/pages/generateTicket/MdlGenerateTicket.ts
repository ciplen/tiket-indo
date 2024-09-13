import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {CmpGenerateTicket} from './CmpGenerateTicket';
import {NgxQRCodeModule} from 'ngx-qrcode2';

@NgModule({
	declarations: [
		CmpGenerateTicket
	],
	imports: [
		NgxQRCodeModule,
		IonicPageModule.forChild(CmpGenerateTicket),
	],
	exports: [
		CmpGenerateTicket
	]
})

export class MdlGenerateTicket {}
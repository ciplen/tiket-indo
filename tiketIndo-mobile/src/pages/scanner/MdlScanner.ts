import { NgModule } from '@angular/core';
import { CmpScanner } from './CmpScanner';
import { IonicPageModule } from 'ionic-angular';


@NgModule({
	imports: [IonicPageModule.forChild(CmpScanner),],
	exports: [CmpScanner],
	declarations: [CmpScanner],
	providers: [],
})
export class MdlScanner { }

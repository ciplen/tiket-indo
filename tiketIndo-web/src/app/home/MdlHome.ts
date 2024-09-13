import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CmpHome} from './CmpHome';
import {CmpHelp} from './CmpHelp';
import {RoutHome} from './RoutHome';
import {MdlShared} from '../shared/MdlShared';

@NgModule({
	imports: [CommonModule, RoutHome, MdlShared],
	declarations: [CmpHome, CmpHelp],
	exports: [CmpHome],
	providers: []
})
export class MdlHome {}

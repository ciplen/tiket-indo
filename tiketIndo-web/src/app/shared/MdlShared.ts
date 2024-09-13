import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CmpDynabox } from './CmpDynabox';
import { CmpTypeahead } from './CmpTypeahead';
import { AlertComponent } from './alert.component';
import { UserService } from './user.service';
import { SrvMasterData } from './SrvMasterData';
import { CalendarComponent } from './calendar.component';
import { DefaultHttpInterceptor } from './DefaultHttpInterceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { CmpTableHelper, CmpTableHelperSaveDialog } from './CmpTableHelper';
import { CmpPagination } from './CmpPagination';
import { TranslateModule } from '@ngx-translate/core';
import { UppercaseDirective } from './UppercaseDirective';
import { MatDialogModule, MatDatepickerModule, MatNativeDateModule } from '@angular/material';
import { SrvUtil } from './SrvUtil';
import { CmpMessageDialog } from './CmpMessageDialog';

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
	imports: [NgxDatatableModule,
		CommonModule,
		RouterModule,
		FormsModule,
		TranslateModule,
		MatDialogModule,
		MatDatepickerModule,
		MatNativeDateModule],
	declarations: [CmpTableHelper, CmpPagination, AlertComponent, CmpDynabox, CmpTypeahead, CalendarComponent, UppercaseDirective,
		CmpMessageDialog, CmpTableHelperSaveDialog],
	exports: [NgxDatatableModule, CmpTableHelper, CmpPagination, CommonModule, FormsModule, RouterModule, AlertComponent,
		CmpDynabox, CmpTypeahead, CalendarComponent, TranslateModule, UppercaseDirective, MatDialogModule, MatDatepickerModule],
	entryComponents: [CmpMessageDialog, CmpTableHelperSaveDialog]
}
)
export class MdlShared {

	static forRoot(): ModuleWithProviders {
		return {
			ngModule: MdlShared,
			providers: [UserService, SrvMasterData,
				{
					provide: HTTP_INTERCEPTORS,
					useClass: DefaultHttpInterceptor,
					multi: true,
				},
//				{ provide: DateAdapter, useClass: AppDateAdapter },
//				{ provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS },
				SrvUtil]
		};
	}
}

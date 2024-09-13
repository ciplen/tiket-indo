import {Component, Input, AfterViewInit, ElementRef, forwardRef, ViewChild} from '@angular/core';
import {NG_VALUE_ACCESSOR, ControlValueAccessor} from '@angular/forms';

export const CALENDAR_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => CalendarComponent),
	multi: true
};
declare var jQuery: any;

@Component({
	moduleId: module.id,
	selector: 'p-calendar',
	template: `<input #panemuCalendar  type="text" [placeholder]="placeholder">`,
	providers: [CALENDAR_VALUE_ACCESSOR],
	styleUrls: ['calendar.scss']
})

export class CalendarComponent implements AfterViewInit, ControlValueAccessor {
	@ViewChild('panemuCalendar') myDate: any;
	private objRef: any;
	value: string;
	onModelChange: Function = () => {};
	onModelTouched: Function = () => {};
	@Input() disabled: any;
	@Input() minDate = '';
	@Input() placeholder = "";
	inputFieldValue: string;
	@Input() dateFormat: string = 'dd-mm-yy';
	filled: boolean;

	constructor(private elRef: ElementRef) {
	}

	ngOnInit() {
		this.myDate.nativeElement.className = this.elRef.nativeElement.className;
		this.elRef.nativeElement.className = '';
	}

	ngAfterViewInit() {
		this.objRef = jQuery(this.myDate.nativeElement);

		this.objRef.datepicker({
			showOtherMonths: true,
			selectOtherMonths: true,
			changeMonth: true,
			changeYear: true,
			dateFormat: this.dateFormat,
			minDate: this.minDate,
			onSelect: (d, i) => {
				console.log('on select: ' + this.objRef.val());
				if (d !== i.lastVal) {
					let selectedDate = this.toISOString(this.objRef.datepicker("getDate"));
					if (!this.objRef.val()) {
						selectedDate = null;
					}
					this.value = this.toISOString(this.objRef.datepicker("getDate"));
					this.onModelChange(this.value);
				}
			}
		});

		this.objRef.change(() => {
			let selectedDate: Date = this.objRef.datepicker("getDate");
			this.value = this.toISOString(selectedDate);
			this.onModelChange(this.value);
			this.objRef.val(this.formatToLocalDate(selectedDate));
		});

		this.objRef.keydown((e) => {
			this.objRef.datepicker("hide");
		});
	};

	toISOString(selectedDate: Date): string {
		if (selectedDate) {
			let dd = ("0" + selectedDate.getDate()).slice(-2);
			let mm = ("0" + (selectedDate.getMonth() + 1)).slice(-2);
			let yy = selectedDate.getFullYear();
			return yy + '-' + mm + '-' + dd;
		} else {
			return '';
		}
	}

	formatToLocalDate(selectedDate: Date): string {
		if (selectedDate) {
			let dd = ("0" + selectedDate.getDate()).slice(-2);
			let mm = ("0" + (selectedDate.getMonth() + 1)).slice(-2);
			let yy = selectedDate.getFullYear();
			return dd + '-' + mm + '-' + yy;
		} else {
			return '';
		}
	}

	writeValue(value: any): void {
		this.value = value;
		if (this.objRef) {
			if (value) {
				this.objRef.datepicker("setDate", new Date(this.value));
			} else {
				this.objRef.datepicker("setDate", null);
			}
		}
	}

	registerOnChange(fn: Function): void {
		this.onModelChange = fn;
	}

	registerOnTouched(fn: Function): void {
		this.onModelTouched = fn;
	}

	setDisabledState(val: boolean): void {
		this.disabled = val;
	}
}
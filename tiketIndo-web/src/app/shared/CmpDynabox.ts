import {Component, Input, AfterViewInit, Output, EventEmitter, forwardRef, ViewChild} from '@angular/core';
import {DynaboxDatasource} from './DynaboxDatasource';
import {NG_VALUE_ACCESSOR, ControlValueAccessor} from '@angular/forms';

export const DYNABOX_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => CmpDynabox),
	multi: true
};

declare var jQuery: any;

@Component({
	moduleId: module.id,
	selector: 'p-dynabox',
	template: `<select #dynabox style="width: 100%" >
						<option *ngIf="preSelected" value={{preSelected.id}}>{{preSelected.text}}</option>
				</select>`,
	providers: [DYNABOX_VALUE_ACCESSOR]
})

export class CmpDynabox implements AfterViewInit, ControlValueAccessor {
	@ViewChild('dynabox') dynabox: any;
	private objRef: any;
	@Input() datasource: DynaboxDatasource;
	@Input() preSelected: any;
	value: string;
	onModelChange: Function = () => {};
	onModelTouched: Function = () => {};
	@Input() disabled: any;

	ngAfterViewInit() {
		this.objRef = jQuery(this.dynabox.nativeElement);
		this.objRef.select2({
			ajax: {
				url: this.datasource.url,
				dataType: this.datasource.dataType,
				delay: this.datasource.delay,
				xhrFields: {
					withCredentials: true
				},
				data: this.datasource.data,
				processResults: (data, params) => {
					params.page = params.page || 1;
					let list = [];
					for (let i = 0; i < data.rows.length; i++) {
						let item = data.rows[i];
						list.push(this.datasource.createItem(item));
					}
					let obj = {
						results: list,
						pagination: {
							more: (params.page * this.datasource.recordsPerPage) < data.totalRows
						}
					};
					return obj;
				},
				cache: this.datasource.cache,
			},
			minimumInputLength: 1,
			allowClear: true,
			placeholder: "",
			disabled: this.disabled
		});


		this.objRef.on("select2:select", (e) => {
			this.onModelChange(this.objRef.val());
		});
		this.objRef.on("select2:unselect", (e) => {
			this.onModelChange('');
		});
		this.objRef.on("select2:close", (e) => {
			this.dynabox.nativeElement.focus();
		});
	}

	writeValue(value: any): void {
		this.value = value;
		if (this.objRef) {
			this.objRef.val(value).trigger("change");
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
		this.objRef.prop("disabled", val);
	}
}
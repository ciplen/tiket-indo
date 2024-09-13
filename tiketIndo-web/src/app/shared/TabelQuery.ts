export type SortingDirection = 'asc' | 'desc';

export class SortingInfo {
	attributeName: string;
	direction: SortingDirection;
	constructor(attributeName: string, direction: SortingDirection ='asc') {
		this.attributeName = attributeName;
		this.direction = direction;
	}
}

export class TableCriteria {
	attributeName: string;
	value: any;
	constructor(attributeName: string, value:any) {
		this.attributeName = attributeName;
		this.value = value;
	}
}

export class TableQuery {
	tableCriteria: TableCriteria[] = [];
	sortingInfos: SortingInfo[] = [];
	utcMinuteOffset = (new Date).getTimezoneOffset();
}
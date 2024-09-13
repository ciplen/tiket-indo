export class DynaboxDatasource {
	/**
	 * Url of the web service. These REST api should return com.panemu.panict.common.TableData
	 */
	url: string;
	dataType = 'json';
	delay = 500;
	recordsPerPage = 20;
	data = (params) => {
		return {
			q: params.term, // search term
			page: params.page,
			max: this.recordsPerPage
		};
	};
	
	cache: true;
	
	/**
	 * Item parameter is the complex object to be displayed as a selectable item. This parameter should be converted
	 * into {id,text} json object.
	 * return {id:"", text:""} object. This method should be overriden
	 */
	createItem(item) {
		return {id: item.id, text: item.text};
	}
}
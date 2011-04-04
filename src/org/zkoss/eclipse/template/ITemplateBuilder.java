package org.zkoss.eclipse.template;

import java.util.HashMap;


public interface ITemplateBuilder {

	/**
	 * get template file name. (usually accroding to file name and path.)
	 *
	 * @return
	 */
	public String getTemplateName();

	public HashMap<String,Object> getPreparedVariables();


	/**
	 * To build with template , get a string with build result..
	 * @param args
	 * @return
	 */
	public String build();

}

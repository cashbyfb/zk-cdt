package org.zkoss.eclipse.template;

import java.util.HashMap;

import org.zkoss.eclipse.template.model.TemplateModel;

public class ModelTemplate extends AbstractTemplate implements
		ITemplateBuilder {

	TemplateModel model;
	String name;


	public ModelTemplate(String name, TemplateModel model) {
		this.model = model;
		this.name = name;
	}

	public String getTemplateName() {
		return name;
	}

	public HashMap<String, Object> getPreparedVariables() {
		if (model != null)
			return model.getPreparedVariables();
		return new HashMap<String, Object>();
	}

}

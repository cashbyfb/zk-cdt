package org.zkoss.eclipse.template.model;

import java.util.HashMap;

import org.zkoss.eclipse.componentwizard.ZKComponentWizardActivator;

/**
 * I found that we could always use same model when new a component project, so
 * I build a model to make it easier , but still support complex / hardcode
 * template config when you wrote your own TemplateBuilder.
 *
 * @author TonyQ
 *
 */
public class NewComponentProjectModel implements TemplateModel {
	private String addonName;
	private String version = "0.8.0";
	private String zkVersionSince = "5.0.0";
	private String zkVersion = "5.0.6";

	private NewComponentModel newComponentModel;

	public NewComponentProjectModel(NewComponentModel componentModel) {
		newComponentModel = componentModel;
	}

	public String getAddonName() {
		return addonName == null ? newComponentModel.getComponentName()
				: addonName;
	}

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getZkVersionSince() {
		return zkVersionSince;
	}

	public void setZkVersionSince(String zkVersionSince) {
		this.zkVersionSince = zkVersionSince;
	}

	public HashMap<String, Object> getPreparedVariables() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		putWithoutNull(map, "addon-name", getAddonName());
		putWithoutNull(map, "version", getVersion());
		putWithoutNull(map, "zk-version-since", getZkVersionSince());

		putWithoutNull(map, "zk-version", getZkVersion());

		HashMap<String, Object> compMap = newComponentModel
				.getPreparedVariables();

		/**
		 * Check for conflict to make sure it's not a hidden issue.
		 */
		for (String key : compMap.keySet()) {
			if (map.containsKey(key))
				ZKComponentWizardActivator.logWarning(key
						+ " is conflict with project variable.");

		}
		map.putAll(newComponentModel.getPreparedVariables());
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putWithoutNull(HashMap map, String key, Object value) {
		if (value != null)
			map.put(key, value);
	}

	public String getWidgetPackagePath() {
		return newComponentModel.getWidgetPackagePath();
	}

	public String getComponentPackagePath() {
		return newComponentModel.getComponentPackagePath();
	}

	public String getComponentClass() {
		return newComponentModel.getComponentClass();
	}

	public String getWidgetName() {
		return newComponentModel.getWidgetName();
	}


	public String getWidgetNameLowerCase() {
		return newComponentModel.getWidgetNameLowerCase();
	}

	public String getZkVersion() {
		return zkVersion;
	}

	public void setZkVersion(String zkVersion) {
		this.zkVersion = zkVersion;
	}

	public NewComponentModel getComponentModel() {
		return newComponentModel;
	}

	public void setComponentModel(NewComponentModel newComponentModel) {
		this.newComponentModel = newComponentModel;
	}

}

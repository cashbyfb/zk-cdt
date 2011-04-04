package org.zkoss.eclipse.template.model;

import java.util.HashMap;

/**
 * I found that we could always use same model when new a component project, so I build a
 * model to make it easier , but still support complex / hardcode template
 * config when you wrote your own TemplateBuilder.
 *
 * @author TonyQ
 *
 */
public class NewComponentModel implements TemplateModel {
	private String addonName;
	private String version = "0.8.0";
	private String tagName;

	private String componentPackage;
	private String componentName;

	private String widgetPackage;

	private String widgetName;

	private String zkVersionSince = "5.0.5";

	public String getAddonName() {
		return addonName == null ? tagName : addonName;
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

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getComponentPackage() {
		return componentPackage;
	}

	public String getComponentPackageNoDot() {

		if(componentPackage!=null && componentPackage.endsWith("."))
			return componentPackage.substring(0,componentPackage.length()-1);

		return componentPackage;
	}

	public void setComponentPackage(String componentPackage) {
		this.componentPackage = componentPackage;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getWidgetPackage() {
		return widgetPackage;
	}

	public void setWidgetPackage(String widgetPackage) {
		this.widgetPackage = widgetPackage;
	}

	public String getWidgetName() {
		return widgetName;
	}

	public String getWidgetNameLowerCase() {
		return widgetName == null ? "" : widgetName.toLowerCase();
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
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
		putWithoutNull(map, "tag-name", getTagName());

		putWithoutNull(map, "component-package-no-dot", getComponentPackageNoDot());
		putWithoutNull(map, "component-package", getComponentPackage());
		putWithoutNull(map, "component-name", getComponentName());

		putWithoutNull(map, "widget-package", getWidgetPackage());
		putWithoutNull(map, "widget-name", getWidgetName());
		putWithoutNull(map, "widget-name-lowercase", getWidgetNameLowerCase());

		putWithoutNull(map, "zk-version-since", getZkVersionSince());

		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putWithoutNull(HashMap map, String key, Object value) {
		if (value != null)
			map.put(key, value);
	}

}

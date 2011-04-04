package org.zkoss.eclipse.template;

import java.util.HashMap;

import org.zkoss.eclipse.template.AbstractTemplate;

/**
 * ${addon-name} ${version} def => 0.8.0 ${tag-name} ${component-package}
 * ${component-name} ${widget-package} ${widget-name} ${widget-name-lowercase}
 * ${zk-version-since}
 *
 * @author cwang4
 *
 */
public class LangAddonTemplate extends AbstractTemplate {

	private String addonName;
	private String version = "0.8.0";
	private String tagName;

	private String componentPackage;
	private String componentName;

	private String widgetPackage;

	private String widgetName;

	private String zkVersionSince ="5.0.5";

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

	private String getWidgetNameLowerCase() {
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

	@Override
	public String getTemplateName() {
		return "templates/lang-addon.vtl";
	}


	public HashMap<String, Object> getPreparedVariables() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		putWithoutNull(map, "addon-name", getAddonName());
		putWithoutNull(map, "version", getVersion());
		putWithoutNull(map, "tag-name", getTagName());

		putWithoutNull(map, "component-package", getComponentPackage());
		putWithoutNull(map, "component-name", getComponentName());

		putWithoutNull(map, "widget-package", getWidgetPackage());
		putWithoutNull(map, "widget-name", getWidgetName());
		putWithoutNull(map, "widget-name-lowercase", getWidgetNameLowerCase());

		putWithoutNull(map, "zk-version-since", getZkVersionSince());

		return map;
	}

}

package org.zkoss.eclipse.template.model;

import java.util.HashMap;

/**
 * I found that we could always use same model when new a component project, so
 * I build a model to make it easier , but still support complex / hardcode
 * template config when you wrote your own TemplateBuilder.
 *
 * @author TonyQ
 *
 */
public class NewComponentModel implements TemplateModel {
	private String componentName;

	private String componentPackage;
	private String componentClass;

	private String widgetPackage;

	private String widgetName;

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String tagName) {
		this.componentName = tagName;
	}

	public String getComponentPackage() {
		return componentPackage;
	}

	public String getComponentPackagePath() {

		if (componentPackage == null)
			throw new IllegalStateException(
					"you have to specify component package first , component package is null!");

		return "src/" + componentPackage.replaceAll("\\.", "/");
	}

	public String getComponentPackageNoDot() {

		if (componentPackage != null && componentPackage.endsWith("."))
			return componentPackage.substring(0, componentPackage.length() - 1);

		return componentPackage;
	}

	public void setComponentPackage(String componentPackage) {
		this.componentPackage = componentPackage;
	}

	public String getComponentClass() {
		return componentClass;
	}

	public void setComponentClass(String componentName) {
		this.componentClass = componentName;
	}

	public String getWidgetPackage() {
		return widgetPackage;
	}

	public String getWidgetPackagePath() {

		if (widgetPackage == null)
			throw new IllegalStateException(
					"you have to specify widget package first , widget package is null!");

		return "resources/web/js/" + widgetPackage.replaceAll("\\.", "/");
	}

	public String getWidgetPackageNoDot() {

		if (widgetPackage != null && widgetPackage.endsWith("."))
			return widgetPackage.substring(0, widgetPackage.length() - 1);

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


	public HashMap<String, Object> getPreparedVariables() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		putWithoutNull(map, "component-name", getComponentName());

		putWithoutNull(map, "component-package", getComponentPackage());
		putWithoutNull(map, "component-package-no-dot",
				getComponentPackageNoDot());
		putWithoutNull(map, "component-class", getComponentClass());

		putWithoutNull(map, "widget-package", getWidgetPackage());
		putWithoutNull(map, "widget-package-no-dot", getWidgetPackageNoDot());
		putWithoutNull(map, "widget-name", getWidgetName());
		putWithoutNull(map, "widget-name-lowercase", getWidgetNameLowerCase());

		/**
		 * TODO configurable
		 */
		putWithoutNull(map, "widget-parent", "zk.Widget");


		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putWithoutNull(HashMap map, String key, Object value) {
		if (value != null)
			map.put(key, value);
	}

}

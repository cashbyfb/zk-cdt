package org.zkoss.eclipse.componentwizard.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.zkoss.eclipse.template.ITemplateBuilder;
import org.zkoss.eclipse.template.ModelTemplate;
import org.zkoss.eclipse.template.model.NewComponentModel;
import org.zkoss.eclipse.template.model.NewComponentProjectModel;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "xml". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class NewMavenComponentWizard extends NewComponentWizard {

	/**
	 * Constructor for NewMavenComponentWizard.
	 */
	public NewMavenComponentWizard() {
		super();
	}

	protected HashMap<String, ITemplateBuilder> prepareFileBuilders(
			NewComponentProjectModel compProjectModel) {
		HashMap<String, ITemplateBuilder> builders = super
				.prepareFileBuilders(compProjectModel);
		builders.put("pom.xml", new ModelTemplate("templates/xml_pom.vtl",
				compProjectModel));
		return builders;
	}

	public void addPages() {
		page = new NewMavenComponentWizardPage(selection);
		addPage(page);
	}

	protected NewComponentProjectModel prepareNewComponentModel() {
		NewComponentProjectModel projModel = super.prepareNewComponentModel();

		NewMavenComponentWizardPage newpage = (NewMavenComponentWizardPage) page;
		projModel.setZkVersion(newpage.getZKVersion());

		return projModel;
	}

	protected List<IClasspathEntry> prepareSpecficClassPath(IJavaProject jproj) {
		return new ArrayList<IClasspathEntry>();
	}
	/*
	 * TODO review this not working now
	 */
	// protected void addNuewNature(IProject project, IProgressMonitor monitor,
	// List<String> currentNature) {
	// super.addNuewNature(project, monitor, currentNature);
	// currentNature.add("org.maven.ide.eclipse.maven2Nature");
	// }

}
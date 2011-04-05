package org.zkoss.eclipse.componentwizard.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.zkoss.eclipse.componentwizard.ZKComponentWizardActivator;
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

public class NewComponentWizard extends Wizard implements INewWizard {
	private NewComponentWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewComponentWizard.
	 */
	public NewComponentWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewComponentWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		final String projectName = page.getProjectName();
		final HashMap<String, ITemplateBuilder> builders = prepareFileBuilders();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {

				IProject project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(projectName);

				URI projectLoc = null;// use default
				try {
					JavaCapabilityConfigurationPage.createProject(project,
							projectLoc, monitor);
					addJavaNature(project, monitor);

					buildDirs(project, monitor);

					IJavaProject javaProj = JavaCore.create(project);
					javaProj.setRawClasspath(getDefaultClassPath(javaProj),
							monitor);

					buildTempates(project, builders, monitor);

					// javaProj.setRawClasspath(null, monitor);
					// doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (Error e) {
					e.printStackTrace();
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}
		return true;
	}

	private HashMap<String, ITemplateBuilder> prepareFileBuilders() {
		NewComponentModel compModel = new NewComponentModel();
		compModel.setComponentClass(page.getComponentClass());
		compModel.setComponentPackage(page.getComponentPackage());
		compModel.setWidgetPackage(page.getWidgetPackage());
		compModel.setWidgetName(page.getWidgetName());
		compModel.setComponentName(page.getComponentName());

		NewComponentProjectModel compProjectModel = new NewComponentProjectModel(compModel);

		HashMap<String, ITemplateBuilder> builders = new HashMap<String, ITemplateBuilder>();

		builders.put("resources/metainfo/zk/lang-addon.xml",
				new ModelTemplate("templates/xml_lang-addon.vtl",compProjectModel));

		builders.put(compProjectModel.getComponentPackagePath() +"Version.java",
				new ModelTemplate("templates/class_version.vtl",compProjectModel));

		builders.put(compProjectModel.getComponentPackagePath() + compProjectModel.getComponentClass()+".java",
				new ModelTemplate("templates/class_component.vtl",compProjectModel));

		builders.put(compProjectModel.getWidgetPackagePath()+"zk.wpd",
				new ModelTemplate("templates/xml_zk_wpd.vtl",compProjectModel));

		builders.put(compProjectModel.getWidgetPackagePath()+compProjectModel.getWidgetName()+".js",
				new ModelTemplate("templates/js_widget.vtl",compProjectModel));

		builders.put(compProjectModel.getWidgetPackagePath()+"mold/"+compProjectModel.getWidgetNameLowerCase()+".js",
				new ModelTemplate("templates/js_mold.vtl",compProjectModel));

		builders.put(compProjectModel.getWidgetPackagePath()+"css/"+compProjectModel.getWidgetNameLowerCase()+".css.dsp",
				new ModelTemplate("templates/dsp_css.vtl",compProjectModel));

		return builders;
	}

	private void createDirs(IProject project,String path) throws CoreException{

		String[] pathtoken = path.split("/");
		IFolder folder = project.getFolder(pathtoken[0]);
		if(!folder.exists()) folder.create(true, true, null);

		for(int i = 1 ; i < pathtoken.length ;++i){
			folder = folder.getFolder(pathtoken[i]);
			if(!folder.exists()) folder.create(true, true, null);
		}

	}

	private void buildDirs(IProject project, IProgressMonitor monitor)
			throws CoreException {
		createDirs(project,"src");
		createDirs(project,"resources/metainfo/zk");
		createDirs(project,"resources/web");
	}

	private void buildTempates(IProject project,
			Map<String, ITemplateBuilder> builders, IProgressMonitor monitor)
			throws CoreException {

		for (String path : builders.keySet()) {
			if (builders.get(path) == null) {
				throw new IllegalArgumentException("builder couldn't be null");
			}
			String container = "./";
			String fileName = path;
			if (path.lastIndexOf("/") != -1) {
				container = path.substring(0, path.lastIndexOf("/"));
				fileName = path.substring(path.lastIndexOf("/") + 1);

				//make sure the folder exist
				createDirs(project,container);
			}
			createFile(project.getFolder(container), fileName,
					builders.get(path), monitor);
		}

	}

	private IClasspathEntry[] getDefaultClassPath(IJavaProject jproj) {
		List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();

		list.add(JavaCore.newSourceEntry(jproj.getProject().getFolder("src")
				.getFullPath()));
		list.add(JavaCore.newSourceEntry(jproj.getProject()
				.getFolder("resources").getFullPath()));

		list.add(JavaCore.newContainerEntry(new Path(ZKComponentWizardActivator.CONTAINER_ZK)));

		IClasspathEntry[] jreEntries = PreferenceConstants
				.getDefaultJRELibrary();

		for (IClasspathEntry ice : jreEntries) {
			list.add(ice);
		}
		return list.toArray(new IClasspathEntry[0]);
	}

	public static void addJavaNature(IProject project, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null && monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		if (!project.hasNature(JavaCore.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = JavaCore.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, monitor);
		} else {
			if (monitor != null) {
				monitor.worked(1);
			}
		}
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void createFile(IResource resource, String fileName,
			ITemplateBuilder template, IProgressMonitor monitor)
			throws CoreException {
		// create a sample file

		if (resource == null) {
			throwCoreException("Container is not given.");
		}

		monitor.beginTask("Creating " + fileName, 2);
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + resource.getName()
					+ "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream(template.build());
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);

	}

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream(String contents) {
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "ZKComponentWizard",
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 *
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
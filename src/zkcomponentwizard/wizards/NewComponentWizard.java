package zkcomponentwizard.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
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
		final String componentName = page.getComponentName();

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
					IJavaProject javaProj = JavaCore.create(project);
					project.getFolder("src").create(true,true,monitor);
					project.getFolder("resources").create(true,true,monitor);
					project.getFolder("resources/metainfo").create(true,true,monitor);
					project.getFolder("resources/metainfo/zk").create(true,true,monitor);
					project.getFolder("resources/web").create(true,true,monitor);
					javaProj.setRawClasspath(getDefaultClassPath(javaProj), monitor);
//					javaProj.setRawClasspath(null, monitor);
					// doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
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
	private IClasspathEntry[] getDefaultClassPath(IJavaProject jproj) {
		List<IClasspathEntry> list= new ArrayList<IClasspathEntry>();

		list.add(JavaCore.newSourceEntry(jproj.getProject().getFolder("src").getFullPath()));
		list.add(JavaCore.newSourceEntry(jproj.getProject().getFolder("resources").getFullPath()));


		IClasspathEntry[] jreEntries= PreferenceConstants.getDefaultJRELibrary();

		for(IClasspathEntry ice:jreEntries){
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

	private void doFinish(String containerName, String fileName,
			IProgressMonitor monitor) throws CoreException {
		// // create a sample file
		// monitor.beginTask("Creating " + fileName, 2);
		// IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// IResource resource = root.findMember(new Path(containerName));
		// if (!resource.exists() || !(resource instanceof IContainer)) {
		// throwCoreException("Container \"" + containerName
		// + "\" does not exist.");
		// }
		// IContainer container = (IContainer) resource;
		// final IFile file = container.getFile(new Path(fileName));
		// try {
		// InputStream stream = openContentStream();
		// if (file.exists()) {
		// file.setContents(stream, true, true, monitor);
		// } else {
		// file.create(stream, true, monitor);
		// }
		// stream.close();
		// } catch (IOException e) {
		// }
		// monitor.worked(1);
		// monitor.setTaskName("Opening file for editing...");
		// getShell().getDisplay().asyncExec(new Runnable() {
		// public void run() {
		// IWorkbenchPage page = PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// try {
		// IDE.openEditor(page, file, true);
		// } catch (PartInitException e) {
		// }
		// }
		// });
		// monitor.worked(1);
	}

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents = "This is the initial file contents for *.xml file that should be word-sorted in the Preview page of the multi-page editor";
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
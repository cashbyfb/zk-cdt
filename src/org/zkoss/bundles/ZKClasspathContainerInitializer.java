package org.zkoss.bundles;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.zkoss.eclipse.componentwizard.ZKComponentWizardActivator;

public class ZKClasspathContainerInitializer extends ClasspathContainerInitializer{
	private static final IStatus NOT_SUPPORTED= new Status(IStatus.ERROR, ZKComponentWizardActivator.PLUGIN_ID, ClasspathContainerInitializer.ATTRIBUTE_NOT_SUPPORTED, new String(), null);
	private static final IStatus READ_ONLY= new Status(IStatus.ERROR, ZKComponentWizardActivator.PLUGIN_ID, ClasspathContainerInitializer.ATTRIBUTE_READ_ONLY, new String(), null);

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {

		if(ZKComponentWizardActivator.CONTAINER_ZK.equals(containerPath.toString())){
			JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project },
					new IClasspathContainer[] { new ZKClasspathContainer506() }, null);
		}

	}

	public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
		return true;
	}

	public IStatus getAccessRulesStatus(IPath containerPath, IJavaProject project) {
		return NOT_SUPPORTED;
	}

	public IStatus getSourceAttachmentStatus(IPath containerPath, IJavaProject project) {
		return READ_ONLY;
	}

	public IStatus getAttributeStatus(IPath containerPath, IJavaProject project, String attributeKey) {
		if (attributeKey.equals(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME)) {
			return Status.OK_STATUS;
		}
		return NOT_SUPPORTED;
	}


}

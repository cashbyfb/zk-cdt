package org.zkoss.bundles;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.Bundle;
import org.zkoss.eclipse.componentwizard.ZKComponentWizardActivator;

public class ZKClasspathContainer506 implements IClasspathContainer {
	/**
	 * TODO review this class
	 *
	 * @return
	 */

	private IClasspathEntry[] getZKBundle() {

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		Bundle bundle = ZKComponentWizardActivator.getDefault().getBundle();
		URL installUrl = bundle.getEntry("/");

		addRelativeArchiveEntry(entries, installUrl, "breeze.jar");
		addRelativeArchiveEntry(entries, installUrl, "bsh.jar");
		addRelativeArchiveEntry(entries, installUrl, "zcommons-el.jar");
		addRelativeArchiveEntry(entries, installUrl, "zcommon.jar");
		addRelativeArchiveEntry(entries, installUrl, "zhtml.jar");
		addRelativeArchiveEntry(entries, installUrl, "zk.jar");
		addRelativeArchiveEntry(entries, installUrl, "zkplus.jar");
		addRelativeArchiveEntry(entries, installUrl, "zul.jar");
		addRelativeArchiveEntry(entries, installUrl, "zweb.jar");

		return entries.toArray(new IClasspathEntry[0]);
	}

	private void addRelativeArchiveEntry(List<IClasspathEntry> entries,
			URL installUrl, String libJarName) {

		try {
			String relativePath = "lib/zk/" + libJarName;
			URL bundleUrl = new URL(installUrl, relativePath);
			addArchiveEntry(entries, bundleUrl);
			return;
		} catch (MalformedURLException e) {
			ZKComponentWizardActivator.logError(e);
			return;
		}
	}

	private void addArchiveEntry(List<IClasspathEntry> entries, URL bundleUrl) {

		try {
			URL fileUrl = FileLocator.toFileURL(bundleUrl);

			entries.add(JavaCore.newLibraryEntry(new Path(fileUrl.getPath()),
					null, null));

			return;
		} catch (IOException e) {
			ZKComponentWizardActivator.logError(e);
			return;
		}
	}

	public IClasspathEntry[] getClasspathEntries() {
		return getZKBundle();
	}

	public String getDescription() {
		return "ZK container 5.0.6";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return new Path(ZKComponentWizardActivator.CONTAINER_ZK);
	}

}

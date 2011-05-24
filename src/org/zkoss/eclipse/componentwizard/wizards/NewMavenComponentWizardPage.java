package org.zkoss.eclipse.componentwizard.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (xml).
 */

public class NewMavenComponentWizardPage extends NewComponentWizardPage {

	private Text zkVersion;

	public static final String PAGE_NAME = " ZKMAvenComponentWizardPage";
	/**
	 * Constructor for SampleNewWizardPage.
	 *
	 * @param pageName
	 */
	public NewMavenComponentWizardPage(ISelection selection) {
		super(PAGE_NAME,selection);
		setTitle("ZK Component Wizard");
		setDescription("The wizard create a ZK maven Component skeleton.");
	}

	@Override
	protected void createSubControls(Composite container) {

		super.createSubControls(container);

		Label label = new Label(container, SWT.NULL);
		label.setText("&ZK Version:");

		zkVersion = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		zkVersion.setLayoutData(gd);
		zkVersion.setText("5.0.7");

	}

	public String getZKVersion() {
		return zkVersion.getText();
	}
}
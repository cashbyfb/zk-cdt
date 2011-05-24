package org.zkoss.eclipse.componentwizard.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (xml).
 */

public class NewComponentWizardPage extends WizardPage {

	protected Text projectNameText;
	protected Text componentNameText;
	protected Text componentClassText;
	protected Text widgetNameText;


	protected ISelection selection;

	public static final String PAGE_NAME = " ZKComponentWizardPage";

	/**
	 * Constructor for SampleNewWizardPage.
	 *
	 * @param pageName
	 */
	public NewComponentWizardPage(ISelection selection) {
		super(PAGE_NAME);
		setTitle("ZK Component Wizard");
		setDescription("The wizard create a ZK Component skeleton.");
		this.selection = selection;
	}
	public NewComponentWizardPage(String pageName,ISelection selection) {
		super(pageName);
		setTitle("ZK Component Wizard");
		setDescription("The wizard create a ZK Component skeleton.");
		this.selection = selection;
	}

	protected void createSubControls(Composite container){

		Label label = new Label(container, SWT.NULL);
		label.setText("&ProjectName:");

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectNameText.setLayoutData(gd);
		projectNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateProject();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Component name:");

		componentNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		componentNameText.setLayoutData(gd);
		componentNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Component class name:");

		componentClassText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		componentClassText.setLayoutData(gd);
		componentClassText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Widget name:");

		widgetNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		widgetNameText.setLayoutData(gd);
		widgetNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
			}
		});

		projectNameText.setText("test");
		componentClassText.setText("org.test.Mylabel");
		componentNameText.setText("mylabel");
		widgetNameText.setText("test.Mylabel");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		/**
		 * TODO add workspace location
		 */

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		createSubControls(container);

		initialize();
		setControl(container);
	}

	public boolean validateProject() {
		String projectName = projectNameText.getText();

		if ("".equals(projectName) || projectName == null) {
			showError("You have to enter a project name.");
			return false;

		}
		if (ResourcesPlugin.getWorkspace().getRoot().findMember(projectName) != null) {
			showError("project is already exist");
			return false;
		}
		showPass();
		return true;
	}

	public void showPass() {
		setErrorMessage(null);
		setMessage(null);
		setPageComplete(true);
	}

	public void showError(String message) {
		setErrorMessage(message);
		setMessage("......");
		setPageComplete(false);
	}

	public void validate() {

		setErrorMessage(null);
		setMessage("......");
		setPageComplete(false);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				projectNameText.setText(container.getFullPath().toString());
			}
		}
	}

	public String getProjectName() {
		return projectNameText.getText();
	}

	public String getComponentPackage() {
		String val =  componentClassText.getText();
		if(val.lastIndexOf(".")!= -1 )
			return val.substring(0,val.lastIndexOf(".") +1 );
		else
			return "";
	}

	public String getComponentClass() {
		String val =  componentClassText.getText();
		if(val.lastIndexOf(".") != -1 )
			return val.substring(val.lastIndexOf(".")+1);
		else
			return val;
	}

	public String getWidgetPackage() {
		String val =  widgetNameText.getText();
		if(val.lastIndexOf(".")!= -1 )
			return val.substring(0,val.lastIndexOf(".") +1 );
		else
			return "";
	}

	public String getWidgetName() {
		String val =  widgetNameText.getText();
		if(val.lastIndexOf(".") != -1 )
			return val.substring(val.lastIndexOf(".")+1);
		else
			return val;
	}

	public String getComponentName() {
		return componentNameText.getText();
	}

}
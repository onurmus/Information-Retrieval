import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * user interface
 * @author OnurM
 *
 */
public class AppUI {

	protected Shell shell;
	private Text txtSearch;
	List listResults; 
	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(480, 481);
		shell.setText("Onuur's simple search engine");
		shell.setLayout(null);
		
		listResults = new List(shell, SWT.V_SCROLL);
		listResults.setBounds(10, 70, 356, 272);
		
		txtSearch = new Text(shell, SWT.BORDER);
		txtSearch.setBounds(10, 22, 356, 27);
		
		Button btnSearch = new Button(shell, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	//when user clicks search button
				String searchItem = txtSearch.getText();
				if(!searchItem.equals("")){
					try {
						Set<Integer> results = new TreeSet<Integer>();
						results = MainClass.getTheResult(searchItem);
						
						listResults.removeAll();
						if(results != null){
							for(Integer result: results){
								listResults.add(result+"");
							}
						}else{
							listResults.add("I could not find any match");
						}
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSearch.setBounds(387, 20, 75, 25);
		btnSearch.setText("Search");
		
		
		
		Button btnRecorpusData = new Button(shell, SWT.NONE);
		btnRecorpusData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	//when user want to re take data and corpus it
				ReadData rd = new ReadData();
				try {
					rd.run();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRecorpusData.setBounds(263, 379, 110, 25);
		btnRecorpusData.setText("Re-corpus Data");
		
		Button btnRetakeCorpus = new Button(shell, SWT.NONE);
		btnRetakeCorpus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	//when user not sure if it reads corpus it calls that
				try {
					MainClass.readCorpus();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRetakeCorpus.setBounds(166, 379, 91, 25);
		btnRetakeCorpus.setText("Re-take corpus");

	}
}

package in.siws.MRKprint;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//import CheckBox.CheckBoxHeader;

public class MRKprint extends JFrame {

	public  ArrayList<String> pathArray = new ArrayList<String>(); //array containing full paths
	public  ArrayList<String> nameArray = new ArrayList<String>(); //array containing file name
	public  ArrayList<String> selectedList = new ArrayList<String>(); //array containing full paths of selected lists
	int TotalMarklists;
    private static final long serialVersionUID = 1L;
    private JTable table;
    private JLabel prname;
    String PrinterName="No Printer Selected";

    
    public void show(String msg) ///for debugging
	{JOptionPane.showMessageDialog(null, msg);}
    
    public void show(int msg)
	{JOptionPane.showMessageDialog(null, msg);}

    
    
    public MRKprint() {
        
    	final PrintMarklist pm=new PrintMarklist();    	
    	prname=new JLabel(PrinterName);
    	
    	
    	Object[] columnNames = {"Files To Print", "Select"};
        Object[][] data = {
            {"No MRK Files in JAR Folder", new Boolean(true)}
             };
        
        LoadPreferences();
        prname.setText("  "+PrinterName);
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            /*@Override
            public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
            }*/
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    
                    default:
                        return Boolean.class;
                }
            }
        };
        
        
        table.getColumnModel().getColumn(1).setMinWidth(100);
        
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        
        JTableHeader header = table.getTableHeader();
        
        header.setPreferredSize(new Dimension(100,25));
        // table.setIntercellSpacing(new Dimension(2, 2));
      //  table.setRowHeight(32);
        
        TableColumn tc = table.getColumnModel().getColumn(1);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener()));

        JButton browseButton=new JButton("Browse");
        browseButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showOpenDialog(getContentPane());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File yourFolder = fc.getSelectedFile();
                }
                
             
               
            }
        });

        
        
        
        
        JButton printButton=new JButton("Print");
        printButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            { Boolean temp;
                pm.fileArray.removeAll(pm.fileArray);
          //    show(selectedList.size());
            	for(int x = 0, y = table.getRowCount(); x < y; x++)
                {
            		temp=(Boolean) GetData(x,1);
            		
            		if(temp) pm.fileArray.add(pathArray.get(x));
                    
                }
            	
           // 	 show(selectedList.size());
             if(pm.fileArray.size()>0)
            	 
             {
              pm.PrintAllMarklists(PrinterName);
            	
             
             
             }
            }
        });
        
        
        JButton setPrinterButton=new JButton("Set Printer");
        setPrinterButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            {
             SetPrinter sp=new SetPrinter();
             PrinterName=sp.SelectPrinter();
             
              prname.setText("  "+PrinterName);
              SavePreferences();
          //  Dimension d = label.getPreferredSize();
            
            //label.setPreferredSize(new Dimension(d.width+30,d.height));
         //   label.setBorder(border);
               
            }
        });

        
        
        JPanel TopPanel = new JPanel();
        TopPanel.setLayout(new GridLayout(2,3));
        TopPanel.setSize(50,50);
        TopPanel.add(browseButton);
        TopPanel.add(printButton);
        TopPanel.add(setPrinterButton);
        TopPanel.add(prname);
        
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);
       
       // getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(TopPanel,BorderLayout.PAGE_START);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
       // getContentPane().add(TopPanel);
       // getContentPane().add(scrollPane);
        
        File f = new File(System.getProperty("java.class.path"));
    	File dir = f.getAbsoluteFile().getParentFile();
    	String path = dir.toString();

    	listfiles(path);   ///collect all mrk filenames with path in file array
    	String frametitle=String.format("Total Marklists : %d", TotalMarklists);
    	setTitle(frametitle);
     //   show(path);
        
    }

    class MyItemListener implements ItemListener
    {
      public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source instanceof AbstractButton == false) return;
        boolean checked = e.getStateChange() == ItemEvent.SELECTED;
        for(int x = 0, y = table.getRowCount(); x < y; x++)
        {
          table.setValueAt(new Boolean(checked),x,1);
        }
      }
    }
 
    
    public static void main(String[] args) 
    {
       SwingUtilities.invokeLater(new Runnable() {

           @Override
           public void run() {
               MRKprint frame = new MRKprint();
               frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
               //frame.pack();
               frame.setSize(400,400);
               frame.setLocationRelativeTo(null);// setLocation(150, 150);
               frame.setVisible(true);
               
           }
       });
    }
    
    
    
    
    
    class CheckBoxHeader extends JCheckBox
    implements TableCellRenderer, MouseListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected CheckBoxHeader rendererComponent;
  protected int column;
  protected boolean mousePressed = false;
  public CheckBoxHeader(ItemListener itemListener) {
    rendererComponent = this;
    rendererComponent.addItemListener(itemListener);
    rendererComponent.setSelected(true);
  }
  public Component getTableCellRendererComponent(
      JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (table != null) {
      JTableHeader header = table.getTableHeader();
      if (header != null) {
        rendererComponent.setForeground(header.getForeground());
        rendererComponent.setBackground(header.getBackground());
        rendererComponent.setFont(header.getFont());
        header.addMouseListener(rendererComponent);
      }
    }
    setColumn(column);
    rendererComponent.setText("Check All");
   
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    return rendererComponent;
  }
  protected void setColumn(int column) {
    this.column = column;
  }
  public int getColumn() {
    return column;
  }
  protected void handleClickEvent(MouseEvent e) {
    if (mousePressed) {
      mousePressed=false;
      JTableHeader header = (JTableHeader)(e.getSource());
      JTable tableView = header.getTable();
      TableColumnModel columnModel = tableView.getColumnModel();
      int viewColumn = columnModel.getColumnIndexAtX(e.getX());
      int column = tableView.convertColumnIndexToModel(viewColumn);
  
      if (viewColumn == this.column && e.getClickCount() == 1 && column != -1) {
        doClick();
      }
    }
  }
  public void mouseClicked(MouseEvent e) {
    handleClickEvent(e);
    ((JTableHeader)e.getSource()).repaint();
  }
  public void mousePressed(MouseEvent e) {
    mousePressed = true;
  }
  public void mouseReleased(MouseEvent e) {
  }
  public void mouseEntered(MouseEvent e) {
  }
  public void mouseExited(MouseEvent e) {
  }
    
 }


	public  void LoadPreferences()
	{Preferences prefs = Preferences.userNodeForPackage(in.siws.MRKprint.SetPrinter.class);

	// Preference key name
	final String PREF_NAME = "ResultViewPref";
	PrinterName= prefs.get(PREF_NAME,PrinterName); // "a string"
	
	}
	
    public void SavePreferences()
	{Preferences prefs = Preferences.userNodeForPackage(in.siws.MRKprint.SetPrinter.class);

	// Preference key name
	final String PREF_NAME = "ResultViewPref";
	// Set the value of the preference
	prefs.put(PREF_NAME, PrinterName);
		
	}

    int listfiles(String path)
    { 
  	  FilenameFilter mrkFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".mrk")) {
					return true;
				} else {
					return false;
				}
			}
		};
  	  
  	  pathArray.removeAll(pathArray);
  	  nameArray.removeAll(nameArray);
  	  File folder = new File(path);
  	  File[] listOfFiles = folder.listFiles(mrkFilter);
  	      for (int i = 0; i < listOfFiles.length; i++) {
  	        if (listOfFiles[i].isFile()) 
  	        {  
  	           pathArray.add(listOfFiles[i].getAbsolutePath());
  	           nameArray.add(listOfFiles[i].getName());
  	           
  	         } 
  	      }
  	      
  	   SortArrays();
  	  TotalMarklists=pathArray.size();
  	  //show(TotalMarklists);
       for(int i=0;i<TotalMarklists;i++)
         
         {if(i>0) AddRow(); // since one row is already there
          SetData(" "+nameArray.get(i),i,0);}
  	   
  	      
  	   
  	    return TotalMarklists;
    }
    
    public void SortArrays()
    {String temp;
     int i,j;
     int arrsize=nameArray.size();
     for(i=0;i<arrsize-1;i++)
    	 for(j=i+1;j<arrsize;j++)
    	 { if(nameArray.get(i).compareTo(nameArray.get(j)) > 0)
    	     { temp=nameArray.get(i);nameArray.set(i,nameArray.get(j));nameArray.set(j,temp);
    	       temp=pathArray.get(i);pathArray.set(i,pathArray.get(j));pathArray.set(j,temp);
    	     
    	     }
    	 }
    	
    	
    	
    }
 	
    public void SetData(Object obj, int row_index, int col_index)
    {  table.getModel().setValueAt(obj,row_index,col_index);  }

   public void AddRow()
   {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.addRow(new Object[]{"", new Boolean(true)});
   }
    
    public Object GetData(int row_index, int col_index)
	{ return table.getModel().getValueAt(row_index, col_index); }  


}
package in.siws.MRKprint;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.print.PrintService;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
    
    public class MRKprint {
    	 
        static String PrinterName="";
        
        public static void main(String[] args) {
            JFrame frame = new JFrame("MRKprint Application");
        	final PrintMarklist pp=new PrintMarklist();
            frame.setSize(200,200);
            
            JButton SetPrinterButton=new JButton("    Click Here To Save Preferred Printer    ");
            ButtonGroup group = new ButtonGroup();
            final ArrayList<String> PrinterNames = new ArrayList<String>(); 
        	
        	for (PrintService service : PrinterJob.lookupPrintServices())
            {
                PrinterNames.add(service.getName());
                   
            }	    	
        	final JRadioButton buttons[] = new JRadioButton[PrinterNames.size()];
            
        	GridLayout Grid = new GridLayout(PrinterNames.size()+1,1); // Create a layout manager
            Container content = frame.getContentPane(); // Get the content pane
            content.setLayout(Grid); // Set the container layout mgr
            
        	content.add(SetPrinterButton);
            
        	
        	for (int i = 0; i < buttons.length; ++i)
        	{
        		buttons[i] = new JRadioButton(PrinterNames.get(i));
        	    // btn.addActionListener(this);
        	    group.add(buttons[i]);
        	    content.add(buttons[i]);
        	    //buttons[i] = btn;
        	}
            
        	SetPrinterButton.addActionListener(new ActionListener()
            {

    			public void actionPerformed(ActionEvent arg0) 
    			{for(int i=0;i<PrinterNames.size();i++) 
              	  if(buttons[i].isSelected()) 
              	  {PrinterName=PrinterNames.get(i);
                   SavePreferences();
                  
                  }
    			pp.PrintAllMarklists(PrinterName);
    				
    			}
            });
            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); ////display frame in Center
            frame.pack();
            frame.setVisible(true);
            LoadPreferences();
          
            for(int i=0;i<PrinterNames.size();i++) 
             	 
        	  { if(PrinterName.length()==0) break;
            	if(PrinterNames.get(i).contains(PrinterName))
        		  { buttons[i].setSelected(true); break;}
        	  
             }
            File f = new File(System.getProperty("java.class.path"));
        	File dir = f.getAbsoluteFile().getParentFile();
        	String path = dir.toString();

        	 int TotalMarklistsToPrint=pp.listfiles(path);   ///collect all mrk filenames with path in file array
        	 String DisplayMessage=String.format("MRKprint : Printing %d Marklists ...",TotalMarklistsToPrint);
        	 if(TotalMarklistsToPrint>0) { pp.PrintAllMarklists(PrinterName); 
        	                               frame.setTitle(DisplayMessage);
        	                              }
        	 else frame.setTitle("MRKprint : No Marklists to print !");
        	// Reminder Re=new Reminder(10);
        }
        

    	public static  void LoadPreferences()
    	{Preferences prefs = Preferences.userNodeForPackage(in.siws.MRKprint.MRKprint.class);

    	// Preference key name
    	final String PREF_NAME = "MRKprinterPref";
    	PrinterName= prefs.get(PREF_NAME,PrinterName); // "a string"
    	
    	}
    	
        public static void SavePreferences()
    	{Preferences prefs = Preferences.userNodeForPackage(in.siws.MRKprint.MRKprint.class);

    	// Preference key name
    	final String PREF_NAME = "MRKprinterPref";
    	// Set the value of the preference
    	prefs.put(PREF_NAME, PrinterName);
    		
    	}
       	 
      }
   
      
     
    
  
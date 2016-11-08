package in.siws.MRKprint;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class PrintMarklist implements Printable
{	
	public  ArrayList<String> fileArray = new ArrayList<String>();
	public  ArrayList<String> strArray = new ArrayList<String>();
	public  ArrayList<String> rollArray = new ArrayList<String>();
	public  ArrayList<String> markArray = new ArrayList<String>();
	
	
	int TotalMarklists=0;
	int CurrentMarklist=0;
	String CollegeName1;
	String CollegeName2;
	String CollegeName3;
    String Clas;
	String Division;// = new String[TL];
	String Stream;// = new String[TL];
	String Subject;// = new String[TL];
	String Examination;// = new String[TL];
	String Examiner;
	String MaxMarks;// = new String[TL];
	String Date;// = new String[TL];
	String FilePath;//= new String[TL];
	String FileTitle;// = new String[TL];
	int TotalSets;
	
	boolean bModified;
	boolean bSaveDirect;
	boolean bFileLoaded;
	String FirstRoll;
	String LastRoll;
	String[] Set = new String[100];
	String[] Key = new String[100];
	
	
	
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
  	  
  	  fileArray.removeAll(fileArray);
  	  File folder = new File(path);
  	  File[] listOfFiles = folder.listFiles(mrkFilter);
  	      for (int i = 0; i < listOfFiles.length; i++) {
  	        if (listOfFiles[i].isFile()) 
  	        {  fileArray.add(listOfFiles[i].getAbsolutePath());
  	         } 
  	      }
  	    TotalMarklists=fileArray.size();
  	    return TotalMarklists;
    }
 	
	
	public  void ReadFromDisk(String fnem)
    {   strArray.removeAll(strArray);
    	BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader(fnem));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 				
		String line = null;
    	try { while ((line = reader.readLine()) != null) 
			{
			 
			 strArray.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
     }
	
	
	
	///// Here the whole  JAVA Printing Mechanism Starts 
	/////  Note 'implements Printable above', It includes the Print Mechanism to our Program
	/////
	  public void PrintAllMarklists(String printername)
              {
		  
			 
		  PrintService ps = findPrintService(printername);
		  if(ps==null) ps = PrintServiceLookup.lookupDefaultPrintService(); 
		  if(ps==null) return;
		   
		   if(TotalMarklists==0)
		    CurrentMarklist=0;
		   	  
	        	  
		  
		  
	         PrinterJob job = PrinterJob.getPrinterJob();
	         try {
				job.setPrintService(ps);
			} catch (PrinterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         job.setPrintable(this);
	        
	         ////Widening the print AREA.
	         //Java Keeps preset Margins of about 1 inch on all corners
                 //Top Left Corner is  (0,0), then width and height
	         HashPrintRequestAttributeSet pattribs=new HashPrintRequestAttributeSet();
	         pattribs.add(new MediaPrintableArea(0, 0, 210, 297, MediaPrintableArea.MM));
	         // 210 x 297  A4 size paper in Millimiters.
	         
	  
	         boolean ok = true; ///job.printDialog();
	         if (ok) 
	             try {
	                  job.print(pattribs);
	             } catch (PrinterException ex) {}
	             
	          
              
	         }
	
	public int print(Graphics pg, PageFormat pf, int pageno)
			throws PrinterException
	{
		//int tlx=(int) pf.getImageableX()+10,tly=(int) pf.getImageableY()+10;
		
		//int w=(int) pf.getImageableWidth()-20,h=(int)pf.getImageableHeight()-20;
		
		 if (pageno>TotalMarklists-1)             // We have only one page, and 'page no' is zero-based
		    {  return NO_SUCH_PAGE;  // After NO_SUCH_PAGE, printer will stop printing.
	        }
		 
		 
		 Font MyFont = new Font("Liberation Serif", Font.PLAIN,9);
		 pg.setFont(MyFont); 
         
		 ReadFromDisk(fileArray.get(pageno));
		 Fillrollmarks();
		 
		 
         int tlx=70,tly=100,cellheight=16,cellwidth=60,colcount=10;//,rowcount=31;
       //  int celltextshiftx=5,celltextshifty=12;

         
         Centre(CollegeName1,450,tlx,tly-72,pg);
         pg.drawString("Class & Div : "+Clas+" "+Division,tlx, tly-38);
         pg.drawString("Examination : "+Examination,tlx, tly-24);
         pg.drawString("Subject :"+Subject,tlx+200, tly-38);
         pg.drawString("Total Marks : "+MaxMarks,tlx+200, tly-24);
        
         pg.drawString("Examiner : "+(Examiner.length()>9 ? Examiner.substring(0, 8) : Examiner),tlx+400, tly-38);
         pg.drawString("Date : "+Date,tlx+400, tly-24);
         
         PrintGrid(tlx,tly,cellheight,cellwidth,10,5,pg);   
         CurrentMarklist++;
		return PAGE_EXISTS;
	 }
///// Here the JAVA Printing Mechanism Ends
/////////////////////////////////////////////////
///////////////////////////////////////////////
	
	
	/////Supporting Functions for Prining //////////////////
	
	
	public PrintService findPrintService(String printerName)
    {
        for (PrintService service : PrinterJob.lookupPrintServices())
        {
            if (service.getName().equalsIgnoreCase(printerName))
                return service;
        }

        return null;
    }

	
	
	
	
	 private void Centre(String s, int width, int XPos, int YPos,Graphics g2d){  
	        int stringLen = (int)  g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();  
	        int start = width/2 - stringLen/2;  
	        g2d.drawString(s, start + XPos, YPos);  
	 }  
	    
	    private void RightJustify(String s, int xPos, int YPos,Graphics g2d){  
	        int stringLen = (int)  g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();  
	        g2d.drawString(s, xPos-stringLen, YPos);  
	 }  
	    
	    private int atoi(String number)
	    {  String input = number.replaceAll("\\D+","");
	    
	    	if(input.length()==0) return 0;
	    	  try {
	    	    return Integer.parseInt(input.replaceAll("[^0-9.]",""));
	    	  } catch (NumberFormatException e)
	    	  {
	    	    return 0;
	    	  }
	    }

	void Fillrollmarks()  ///fill roll and marks in separate rollArray and markArray 
	                      /// Also fill appropriate Header Fields 
	{   rollArray.removeAll(rollArray);
	    markArray.removeAll(markArray);
		CollegeName1=strArray.get(1);    // System.out.println(CollegeName1);
    	CollegeName2=strArray.get(2);
    	CollegeName3=strArray.get(3);
    	String temp[],stemp;
    	stemp=strArray.get(7);
    	temp=stemp.split(":");
    	
    	TotalSets=Integer.parseInt(temp[1].replaceAll("[^0-9.]",""));
    	
        for(int i=0;i<TotalSets;i++) 
        {   stemp=strArray.get(9+3*i);temp=stemp.split(":");
        	Set[i]=temp[1].trim();
        	stemp=strArray.get(9+3*i+1);temp=stemp.split(":");
        	Key[i]=temp[1].trim();
        }
        //show(Set[CurSet]);
        stemp=strArray.get(11+3*TotalSets); temp=stemp.split(":");
    	FirstRoll=temp[1].trim();
    
    	stemp=strArray.get(12+3*TotalSets); temp=stemp.split(":");
    	LastRoll=temp[1].trim();
    	stemp=strArray.get(13+3*TotalSets); temp=stemp.split(":");
    	Examiner=temp[1].trim();
    	stemp=strArray.get(14+3*TotalSets); temp=stemp.split(":");
    	Clas=temp[1].trim();
    	stemp=strArray.get(15+3*TotalSets); temp=stemp.split(":");
    	Division=temp[1].trim();
    	stemp=strArray.get(16+3*TotalSets); temp=stemp.split(":");
    	Stream=temp[1].trim();
    	stemp=strArray.get(17+3*TotalSets); temp=stemp.split(":");
    	Subject=temp[1].trim();
    	stemp=strArray.get(18+3*TotalSets); temp=stemp.split(":");
    	Examination=temp[1].trim();
    	stemp=strArray.get(19+3*TotalSets); temp=stemp.split(":");
    	MaxMarks=temp[1].trim();
    	stemp=strArray.get(20+3*TotalSets); temp=stemp.split(":");
    	Date=temp[1].trim();

		for(int i=28+3*TotalSets;i<strArray.size();i++) 
		{
		stemp=strArray.get(i); temp=stemp.split(":");
    	rollArray.add(temp[0].trim());markArray.add(temp[1].trim());
		
		}
	}
	
	
	public void PrintGrid(int px,int py, int height, int width,int rows,int cols,Graphics gr)
	 {  
		
		
		 int rc=rollArray.size();
	     int cc=2;  
	     int Sections=4;
	     //if(cc==4) Sections=4;
	     int RollsPerSection=40;
	     
	     int Gap=5; 
	     int MrkCellWidth=30;
	     //int celltextshiftx=5,
	     int celltextshifty=12;
		 int rollindex=0;
		 String tempmark1="";
		 long pagetotal=0;
	     int tempc=0;
	     for(int s=0;s<Sections;s++)
	     {int shift=(width+MrkCellWidth*(cc-1)+Gap)*s;
	       ///col title
	          int i=0,j=0;
	         gr.drawRect(px+shift,py+i*height,width, height);
	         Centre("ROLL",width,px+shift,py+i*height+celltextshifty,gr);
	         for(j=0;j<cc-1;j++)
	         {
	        	 gr.drawRect(px+width+j*MrkCellWidth+shift,py+i*height,MrkCellWidth, height);
	        	// String str3=(Subject.length()>3 ? Subject.substring(0,3):Subject);
	        	 
	             Centre("MRK",MrkCellWidth,px+width+j*MrkCellWidth+shift,py+i*height+celltextshifty,gr);
	         }
		     for(i=1;i<=RollsPerSection;i++)
		      {  
			     gr.drawRect(px+shift,py+i*height,width, height);
			     if(rollindex<rc) { String temproll=rollArray.get(rollindex);
			     					
			                        Centre(temproll,width,px+shift,py+i*height+celltextshifty,gr);
			                        
			     				  }
		         for(j=0;j<cc-1;j++)
		         {  if(rollindex<rc){	 tempmark1=markArray.get(rollindex);
		                             Centre(tempmark1,MrkCellWidth,px+width+j*MrkCellWidth+shift,py+i*height+celltextshifty,gr);
		         				}
		         /* String tempmark2=(String) GetData(table,rollindex,2);
		          String tempmark3=(String) GetData(table,rollindex,3);
		         */
		    	 gr.drawRect(px+width+j*MrkCellWidth+shift,py+i*height,MrkCellWidth, height);
		    	 
		         }
		         tempc=atoi(tempmark1);
		    	 pagetotal=pagetotal+tempc;
		     rollindex++;
		   }
		     

	     }
	     String pt=String.format("Page Total : %d", pagetotal);
	     gr.drawString(pt,px, py+700);
	     RightJustify("Examiner's Sign : ____________",px+500, py+700,gr);
	     
	 }
	    
	 
	
	
	
	
}
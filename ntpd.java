import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
class notepad extends JFrame
{
 JTextArea textbox;
 JMenuBar menu;
 JMenu File,Edit,Format,View,Help;
 JMenuItem New,NewWindow,Open,Save,SaveAs,Exit;
 JScrollPane jscrollpane;
 
 String title;
 boolean saved;
 File currentFile;

 String generateTitle()
 {
  String s="Untitled";
  String ss=s;
  int i=1;
  File file;
  while(true)
  {
    file=new File(ss+".txt");

    if(!file.exists())
    break;

    ss=s+"("+i+")";
    i++;
  }
  return ss;
 }

 void updateTitle()
 {
  String s=title;
  if(!saved)
  s="*"+s;
  setTitle(s+" - Notepad");
 }

 boolean write(File file)
 {
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   raf.setLength(0);
   raf.writeBytes(textbox.getText());
  }
  catch(IOException ioe)
  {
   JOptionPane.showMessageDialog(this,"Could not write to file.","Error",JOptionPane.ERROR_MESSAGE);
   return false;
  }
  return true;
 }

 void New()
 {
  if(!saved)
  {
   int selected=JOptionPane.showConfirmDialog(this,"Do you want to save changes to "+title+"?");
   if(selected==JOptionPane.CANCEL_OPTION)
   return;
   if(selected==-1)
   return;
   if(selected==JOptionPane.YES_OPTION)
   {
     if(!Save());
     return;
   }
  }

  title=generateTitle();
  currentFile=new File(title+".txt");
  saved=true;
  updateTitle();
  textbox.setText("");
 }

 void Open()
 {
  if(!saved)
  {
   int selected=JOptionPane.showConfirmDialog(this,"Do you want to save changes to "+title+"?");
   if(selected==JOptionPane.CANCEL_OPTION)
   return;
   if(selected==-1)
   return;
   if(selected==JOptionPane.YES_OPTION)
   {
     if(!Save());
     return;
   }
  }

  JFileChooser jfc= new JFileChooser();
  jfc.setCurrentDirectory(currentFile);
  int selected=jfc.showOpenDialog(this);

  if(selected!=jfc.APPROVE_OPTION)
  return;

  File file=jfc.getSelectedFile();


  String s="";
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
    while(raf.getFilePointer()<raf.length())
    {
     s+=raf.readLine()+"\n";
    }
  }
  catch(IOException ioe)
  {
   JOptionPane.showMessageDialog(this,"Could not read selected file.","Error",JOptionPane.ERROR_MESSAGE);
   return;
  }
  
  textbox.setText(s);
  title=file.getName();
  currentFile=file;
  saved=true;
 }

 boolean Save()
 {
  if(currentFile.exists())
  {
    return write(currentFile);
  }
  else
  {
    return SaveAs();
  }
 }

 boolean SaveAs()
 {
  JFileChooser jfc= new JFileChooser();
  jfc.setCurrentDirectory(new File(""));
  jfc.setSelectedFile(currentFile);
  int selected=jfc.showSaveDialog(this);

  if(selected!=jfc.APPROVE_OPTION)
  return false;

  File file=jfc.getSelectedFile();

  if(file.exists())
  {
   selected=JOptionPane.showConfirmDialog(this,"Do you want overwrite "+file.getName()+"?");
   if(selected!=JOptionPane.OK_OPTION)
   return false; 
  }

  title=file.getName();
  currentFile=file;
  return write(file);
 }

 void Exit()
 {
  if(!saved)
  {
   int selected=JOptionPane.showConfirmDialog(this,"Do you want to save changes to "+title+"?");
   if(selected==JOptionPane.CANCEL_OPTION)
   return;
   if(selected==-1)
   return;
   if(selected==JOptionPane.YES_OPTION)
   {
     if(!Save());
     return;
   }
  }

  System.exit(0);
 }
 
 notepad()
 {
  saved=true;
  title=generateTitle();
  currentFile=new File(title+".txt");

  try {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }catch(Exception ex) {
  ex.printStackTrace();
  }

  menu= new JMenuBar();
  File= new JMenu("File");
  Edit= new JMenu("Edit");
  Format= new JMenu("Format");
  View= new JMenu("View");
  Help= new JMenu("Help");  
  New= new JMenuItem("New                                   Ctrl+N");
  NewWindow= new JMenuItem("New Window          Ctrl+Shift+N");
  Open= new JMenuItem("Open...                               Ctrl+O");
  Save= new JMenuItem("Save                                   Ctrl+S");
  SaveAs= new JMenuItem("Save as...                  Ctrl+Shift+S");
  Exit= new JMenuItem("Exit");
  File.add(New);
  File.add(NewWindow);
  File.add(Open);
  File.add(Save);
  File.add(SaveAs);
  File.add(Exit);
  menu.add(File);
  menu.add(Edit);
  menu.add(Format);
  menu.add(View);
  menu.add(Help);
  New.setPreferredSize(new Dimension(210,19));
  NewWindow.setPreferredSize(new Dimension(210,19));
  Open.setPreferredSize(new Dimension(210,19));
  Save.setPreferredSize(new Dimension(210,19));
  SaveAs.setPreferredSize(new Dimension(210,19));
  Exit.setPreferredSize(new Dimension(210,19));


  New.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     New();
    }
  });
  NewWindow.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     notepad a= new notepad();
    }
  });
  Open.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     Open();
     updateTitle();
    }
  });
  Save.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     saved=Save();
     updateTitle();
    }
  });
  SaveAs.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     saved=SaveAs();
     updateTitle();
    }
  });
  Exit.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     Exit();
    }
  });
  addWindowListener(new WindowAdapter(){
    public void windowClosing(WindowEvent e)
    {
     Exit();
    }
  });
  
  textbox= new JTextArea();
  textbox.setFont(new Font("Consolas",Font.PLAIN,25));
  textbox.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
  
  textbox.addKeyListener(new KeyListener(){
    public void keyPressed(KeyEvent e)
    {
     if(e.getKeyCode()==78&&e.isControlDown()&&e.isShiftDown())
     {
      notepad a= new notepad();
     }
     else if(e.getKeyCode()==78&&e.isControlDown())
     {
      New();
     }
     else if(e.getKeyCode()==83&&e.isControlDown()&&e.isShiftDown())
     {
      saved=SaveAs();
      updateTitle();
     }
     else if(e.getKeyCode()==83&&e.isControlDown())
     {
      saved=Save();
      updateTitle();
     }
     else if(e.getKeyCode()==79&&e.isControlDown())
     {
      Open();
      updateTitle();
     }
     else if(e.getKeyCode()==107&&e.isControlDown())
     {
      int x= textbox.getFont().getSize();
      textbox.setFont(new Font("Consolas",Font.PLAIN,x+1));
     }
     else if(e.getKeyCode()==109&&e.isControlDown())
     {
      int x= textbox.getFont().getSize();
      textbox.setFont(new Font("Consolas",Font.PLAIN,x-1));
     }
    }
    public void keyTyped(KeyEvent e)
    {}
    public void keyReleased(KeyEvent e)
    {}
  });


  textbox.getDocument().addDocumentListener(new DocumentListener(){
    public void changedUpdate(DocumentEvent ev)
    {
     saved=false;
     updateTitle();
    }
    public void removeUpdate(DocumentEvent ev)
    {
     saved=false;
     updateTitle();
    }
    public void insertUpdate(DocumentEvent ev)
    {
     saved=false;
     updateTitle();
    }
  });


  jscrollpane= new JScrollPane(textbox,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
  jscrollpane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

  add(jscrollpane); 
  setJMenuBar(menu);
  setVisible(true);
  setSize(500,500);
  setLocation(100,100);
  setIconImage(Toolkit.getDefaultToolkit().getImage("Notepad.png"));
  updateTitle();
  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
 }
}
class run
{
 public static void main(String[] args)
 {
  notepad a= new notepad();
 }
}
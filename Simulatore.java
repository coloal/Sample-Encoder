import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;        
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.LayoutStyle.ComponentPlacement.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.lang.Math;
import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.concurrent.*;
import java.util.Random;
import java.math.BigInteger;

public class Simulatore implements ActionListener,ItemListener{
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */

    static JFrame main_frame;
    JFrame[] frames_menu1,frames_submenu_menu1,frames_menu2;
    JFrame menu;

    JPanel main_pane;
    JTextArea writeMainPane,readMainPane;
    JScrollPane scrollPane,scrollPane2;
    JTextField fieldSemilla;
    BigInteger seed = new BigInteger("3");
    
   // double x_;
//https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/MenuLookDemoProject/src/components/MenuLookDemo.java
   /*Simulatore(){
   	x_=0;
   }*/

  	public void actionPerformed(ActionEvent e){
      if("semilla".equals(e.getActionCommand()))
        seed=new BigInteger(fieldSemilla.getText());
    }


  	public void itemStateChanged(ItemEvent e){
  		if(e.getStateChange() == ItemEvent.SELECTED)
  		{
  			menu = new JFrame("hey");
  		menu.setPreferredSize(new Dimension(400,40));
  		menu.pack();
  		menu.setVisible(true);
  		}
  		else{
  			menu.setVisible(false);
  		}
  	}

    public JMenuBar createMenuBar_MainFrame(){
    	//Crea la barra de menú
        JMenuBar BarraMenu = new JMenuBar();
        BarraMenu.setOpaque(true);
        BarraMenu.setBackground(new Color(51,134,202));
        BarraMenu.setPreferredSize(new Dimension(480,20));

        //Crea el menú1 en sí
        JMenu menu = new JMenu("menu1");
        JMenu menu2 = new JMenu("menu2");
        menu.setMnemonic(KeyEvent.VK_A);//'alt + A' y se despliega el menu1
        
        //Creamos un item y el Accelerator crea un shortcut para pulsar alt+B y queda especificada la combinación de letras en la GUI para activar el ItemAccelerator
        //el accelerator solo se puede usar con items con JMenuItems no se puede, habría que usar setMnemonic
        JMenuItem item_altB = new JMenuItem("ItemAccelerator");//,KeyEvent.VK_B);
        item_altB.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.ALT_MASK));
        item_altB.addActionListener(this);//Gestiona eventos
        item_altB.setActionCommand("ItemAccelerator");
        menu.add(item_altB);

        menu.addSeparator();//----------------------------------------

        JMenuItem menuItem; 
        //menu item_i
        for( int i=1 ; i<=3 ; ++i)
        {
        menuItem = new JMenuItem("item"+i);
        menuItem.addActionListener(this);
        menuItem.setActionCommand("item"+i);
        menu.add(menuItem);
        }

       

        String[] color_pelo = {"Rubio","Moreno","Pelirrojo","Castano"};
        ButtonGroup group = new ButtonGroup();
        JRadioButton menu_item_pelo = new JRadioButton(color_pelo[0]);
        for( int i = 0 ; i <= color_pelo.length-1 ; ++i )
        {
        	menu_item_pelo = new JRadioButton(color_pelo[i]);
        	menu_item_pelo.addActionListener(this);
        	menu_item_pelo.setActionCommand(color_pelo[i]);
        	group.add(menu_item_pelo);
        	menu2.add(menu_item_pelo);
        }  
        menu_item_pelo.setSelected(true);
       

        menu.addSeparator();//-----------------------------------------------
        
        //Creamos submenu dentro de menu
        JMenu submenu = new JMenu("Submenu");
        
        String[] items_submenu = {"item1_submenu","item2_submenu","item3_submenu"};
        JMenuItem item_submenu;
        for(int i = 0 ; i<=items_submenu.length-1 ; ++i)
        {
        	item_submenu = new JMenuItem(items_submenu[i]);
        	item_submenu.addActionListener(this);
        	item_submenu.setActionCommand(items_submenu[i]);
        	submenu.add(item_submenu);
        }
        menu.add(submenu);
        
        //añade menu a barra de menu
        BarraMenu.add(menu);
        BarraMenu.add(menu2);
        return BarraMenu;
    }

    public Container createContentPane_MainFrame(){
      main_pane = new JPanel();

      fieldSemilla = new JTextField("3");
      fieldSemilla.addActionListener(this);
      fieldSemilla.setActionCommand("semilla");

      writeMainPane = new JTextArea(13,50);
      writeMainPane.setEditable(true);
      scrollPane = new JScrollPane(writeMainPane);
     
      int condition = writeMainPane.WHEN_FOCUSED;
      InputMap inputMap = writeMainPane.getInputMap(condition);
      ActionMap actionMap = writeMainPane.getActionMap();
      KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true);
      inputMap.put(enterKey,enterKey.toString());
      actionMap.put(enterKey.toString(),new AbstractAction(){
        @Override
        public void actionPerformed(ActionEvent e){
          
          String cadena=new String(writeMainPane.getText());
          String[] bins=new String[cadena.length()];
          
          for(int i=0;i<cadena.length();++i)
          {
            char c=cadena.charAt(i);
            bins[i]=Integer.toBinaryString(c);
            while(bins[i].length()<8)
            {
              bins[i]="0"+bins[i];
            }
          }

          int[] firstGen=new int[100];
          randomGenerator rand=new randomGenerator();
          BigInteger dos=new BigInteger("2"),semilla=seed;

          for(int i=0;i<100;++i)
          {
            semilla=rand.fishman_moore1(semilla);
            BigInteger s=semilla.mod(dos);
            firstGen[i]=s.intValue();
          }

          ca1DSim cellAutom = new ca1DSim(100,firstGen,1,2,true,90);
          char[] character = new char[8];
          String[] cifrante = new String[cadena.length()];
          

          for(int i=0;i<cadena.length()*8; ++i)
          {
            character[i%8]=(char)(cellAutom.primeraGen[7]+48);

            if(i%8 == 7)
            {
              cifrante[(int)(i/8)]=new String(character);
            }
            
            cellAutom.nextGen();
          }

          String[] cifrada=new String[cadena.length()];
          char[] aux=new char[8];
          for(int i=0;i<cadena.length();++i)
          {
            for(int j=0;j<8;++j)
            {
              if(bins[i].charAt(j)!=cifrante[i].charAt(j))
                aux[j]=(char)1+48;
              else
                aux[j]=(char)0+48;
            }
            cifrada[i]=new String(aux);
            
          }
          
          char[] aux2=new char[cadena.length()];
          
          for(int i=0 ; i<cadena.length() ;++i)
          {
            aux2[i]+=(char)Integer.parseInt(cifrada[i],2);
            
          }
          String encrypted=new String(aux2);
          readMainPane.setText(encrypted);
              }
            });


            main_pane.add(scrollPane);
            
            
            readMainPane = new JTextArea(13,50);
            scrollPane2 = new JScrollPane(readMainPane);
            readMainPane.setEditable(false);
            main_pane.add(scrollPane2);
           
            return main_pane;
  
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        main_frame = new JFrame("Simulatore");//Creamos un JFrame que es un Top-level Container(contenedor de alto nivel,viene a ser la raíz de una jerarquía de contención)
        										//otro Top-level Container puede ser un JDialog(en este caso la raíz de la jerarquía sería el jDialog)todo contenedor de alto  
        										//nivel contiene a su vez un content pane,una barra de menus opcional,un layered pane y un glass pane.A su vez la layered pane
        										//contiene la barra de menú y la content pane permitiendo la ordenación-Z de
        										//otros componentes.La glass pane se usa habitualmente para interceptar input
        										//events que ocurren por encima del contenedor de alto nivel.
        /*MAIN FRAME*/
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setPreferredSize(new Dimension(640,500));
        //colocar main_frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dim.getWidth()-main_frame.getWidth())/4);
        int y = (int) ((dim.getHeight()-main_frame.getHeight())/4);
        main_frame.setLocation(x,y);

       	Simulatore sim = new Simulatore();

       	main_frame.setContentPane(sim.createContentPane_MainFrame());
        main_frame.setJMenuBar(sim.createMenuBar_MainFrame());
        main_frame.add(sim.fieldSemilla);
        //Display the window.
        main_frame.pack();
        main_frame.setVisible(true);
        
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}



class SliderListener implements ChangeListener{
	double valor;
	JFrame fr;

	public double valor(){return valor;}

	public void stateChanged(ChangeEvent e){
		JSlider source = (JSlider)e.getSource();
		if(!source.getValueIsAdjusting()){
			valor = (double)source.getValue();//valor guarda el valor del dato introducido en el slider
			fr = new JFrame(" "+valor);
 			fr.setPreferredSize(new Dimension(400,40));
  			fr.pack();
  			fr.setVisible(true);
		}
	}

}

class SpinnerListener implements ChangeListener{
	Object lastValue;
	JFrame fr;
	//public double valor(){return valor;}


	public void stateChanged(ChangeEvent e){
		JSpinner source = (JSpinner)e.getSource();
		
		if(lastValue != null && !source.getValue().equals(lastValue))
		{
			fr = new JFrame(" "+lastValue);
 			fr.setPreferredSize(new Dimension(400,40));
  			fr.pack();
  			fr.setVisible(true);
		}
		lastValue = source.getValue();
    

	}

}


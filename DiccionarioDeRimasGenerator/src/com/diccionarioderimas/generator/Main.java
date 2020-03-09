/***************************************************************************
 *   Copyright (C) 2009 by Eduardo Rodriguez Lorenzo                       *
 *   edu.tabula@gmail.com                                                  *
 *   http://www.cronopista.com                                             *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
package com.diccionarioderimas.generator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

/**
 * 
 * Lee resbc.me, toma cada palabra y la pasa al objeto tipo Word adecuado, al
 * que luego le pide que conjugue o derive, según sea el caso.
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class Main extends JPanel implements WindowListener {

	private static final long serialVersionUID = -7341294015432813282L;
	private String basePath;
	private JFrame frame;
	private static Main thisInstance;
	private Image background = new ImageIcon(Main.class
			.getResource("background.png")).getImage();
	private JProgressBar progressBar;
	private static int LIMIT=100000;
	private JLabel cwordLabel;
	private int countProgress;
	
	private static String[] padding=new String[30];
	
	static{
		String t="";
		for(int i=0;i<30;i++){
			padding[i]=t;
			t+=" ";
		}
		
	}

	public Main(String[] args, String baseDir, boolean exit) throws Exception{
	
		thisInstance=this;
		UIManager.put("OptionPane.yesButtonText", "Sí");
		progressBar = new JProgressBar(0, 1708);
		cwordLabel = new JLabel("Borrando base de datos anterior. Por favor, espere");
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		setLayout(null);
		add(progressBar);
		progressBar.setBounds(20, 100, 460,
				progressBar.getPreferredSize().height);
		add(cwordLabel);
		cwordLabel.setBounds(20, 130, 460, 30);

		basePath = baseDir;
		frame = new JFrame("MERimas- Generador");

		frame.setIconImage(new ImageIcon(this.getClass().getResource(
				"logoSmall.png")).getImage());
		frame.setSize(new Dimension(500, 300));
		frame.setResizable(false);
		frame.setLocation(50, 50);
		frame.addWindowListener(this);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Dimension dim = getToolkit().getScreenSize();
		frame.setLocation(new Point((dim.width - 500) / 2,
				(dim.height - 300) / 2));
		frame.getContentPane().add(this);
		frame.setVisible(true);

		
			deleteDirectory(new File(basePath + "DDBB"));
			deleteDirectory(new File(basePath + "FDDBB"));
			deleteDirectory(new File(basePath + "WDDBB"));
			
			if (!new File(basePath + "resbc.me").exists()) {
				try{
					Utils.copyFile(this.getClass().getResourceAsStream("resbc.me"),
						new File(basePath + "resbc.me"));
				}catch (Exception e) {
					JOptionPane
					.showMessageDialog(
							frame,
							"El sistema no permite escribir en el directorio actual.\n" +
							"Se recomienda desinstalar el diccionario e instalarlo en un directorio\n" +
							"que no sea 'Archivos de Programa', como por ejemplo 'Mis documentos'.\n" +
							"Intentaremos resolver este problema en versiones futuras, perdón por las molestias",
							"Sin permiso para escribir", JOptionPane.ERROR_MESSAGE);
					
					System.exit(0);
				}
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(basePath
					+ "resbc.me"),"ISO-8859-1"));
			
			String line = "";
			WordHandler bh = null;
			if(args!=null && args.length>1)
				basePath=args[1];
			
			if (args == null || args.length == 0 || args[0].equals("rhyme"))
				bh = new RhymeWordHandler(basePath);
			else if (args[0].equals("web"))
				bh = new WebPageWordHandler(basePath);
			else if (args[0].equals("frequency"))
				bh = new FrequencyWordHandler(basePath);
			// skip comments
			while (!line.equals(">") && (line = in.readLine()) != null) {
			}
			
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					String[] split = line.split("\\|");
					bh.wordProduced(split[0], split[0], "O", 1);
					if (split[1].indexOf('T') != -1
							|| split[1].indexOf('I') != -1
							|| split[1].indexOf('R') != -1) {
						Verb m = new Verb(split[0], split[1], bh);
						m.conjugate();
					}
					if (split[1].indexOf('F') != -1) {
						NonVerb nv = new NonVerb(split[0], split[1], bh);
						nv.deriveGender();
					}
					if (split[1].indexOf('J') != -1
							|| split[1].indexOf('N') != -1) {
						NonVerb nv = new NonVerb(split[0], split[1], bh);
						nv.derivePlural();
					}
	
				}

			}
			thisInstance.cwordLabel.setText("<html><b>Guardando en archivos...</b></html>");
			bh.save();
			thisInstance.cwordLabel.setText("<html><b>Optimizando base de datos...</b></html>");
			in.close();
			if (args == null || args.length == 0 || args[0].equals("rhyme"))
				optimize();
			
			
			
			
			
		
			JOptionPane
					.showMessageDialog(
							frame,
							"La generación de la base de datos ha finalizado con éxito.",
							"Terminado", JOptionPane.INFORMATION_MESSAGE);
			
			System.out.println(bh.getCount());
			System.out.println(thisInstance.countProgress);
			
			if (exit)
				System.exit(0);
			else
				frame.setVisible(false);
		
		
		
	}

	private void optimize() throws Exception{
		BufferedReader in;
		String line;
		File[] files = new File(basePath+"DDBB").listFiles();
		int fileCount=0;
		
		for (int i = 0; i < files.length; i++) {
			if(files[i].length()>LIMIT){
				//read whole file. Sort. cut. create index file
				
				
					
					//read whole file
					Vector<String> allLines=new Vector<String>();
					in=new BufferedReader(new FileReader(files[i]));
					line=null;
					while((line=in.readLine())!=null){
						allLines.add(line);
					}
					in.close();
					
					//sort
					
					String[] toOrder=new String[allLines.size()];
					String[] toOrderRhyme=new String[allLines.size()];
					for(int j=0;j<toOrder.length;j++){
						line=allLines.get(j);
						char[] chars=line.toCharArray();
						int countT=0;
						int from=-1;
						int to=-1;
						for(int k=0;k<chars.length;k++){
							if(chars[k]=='\t')
								countT++;
							if(from==-1 && countT==3){
								from=k+1;
							}else if(to==-1 && countT==4)
								to=k;
						}
						
						String s=new String(chars,from,to-from);
						toOrderRhyme[j]=s;
						toOrder[j]=new StringBuffer(s).append(padding[30-s.length()]).append(j).toString();
					}
					
					Arrays.sort(toOrder);
					
					String[] ordered=new String[toOrder.length];
					String[] orderedRhyme=new String[allLines.size()];
					for(int j=0;j<toOrder.length;j++){
						String locS = toOrder[j].substring(30,toOrder[j].length());
						int loc = Integer.parseInt(locS);
						ordered[j]=allLines.get(loc);
						orderedRhyme[j]=toOrderRhyme[loc];
					}
				
					//cut (write)
					StringBuffer index=new StringBuffer();
					StringBuffer saveContent=new StringBuffer();
					int bytes=0;
					String firstRhyme=null;
					String lastRhyme="";
					int count=0;
					for(int j=0;j<ordered.length;j++){
						String s = orderedRhyme[j];
						if(firstRhyme==null)
							firstRhyme=s;
						if((bytes>LIMIT && !s.equals(lastRhyme))||j==ordered.length-1){
							//save file and add entry to index
							if(j==ordered.length-1)
								saveContent.append(ordered[j]).append('\n');
							String filename=files[i].getName().substring(0,files[i].getName().lastIndexOf('.'))+"_"+count+".txt";
							BufferedWriter out=new BufferedWriter(new FileWriter(basePath+"DDBB/"+filename));
							out.write(saveContent.toString());
							out.close();
							index.append(firstRhyme+"\t"+lastRhyme+"\n");
							bytes=0;
							saveContent=new StringBuffer();
							firstRhyme=null;
							lastRhyme="";
							count++;
							fileCount++;
						}
						saveContent.append(ordered[j]).append('\n');
						bytes+=ordered[j].length();
						
						
						lastRhyme=s;
					}
				
					//index file
					String filename=files[i].getName().substring(0,files[i].getName().lastIndexOf('.'))+"_index.txt";
					BufferedWriter out=new BufferedWriter(new FileWriter(basePath+"DDBB/"+filename));
					out.write(index.toString());
					out.close();
					files[i].delete();
					Main.event(8, "<html><b>Optimizando base de datos...</b></html>");
					
				
				

			}
		}
		
		System.out.println(fileCount);
		
	}

	
	public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public void windowClosing(WindowEvent e) {

		int res = JOptionPane
				.showConfirmDialog(
						frame,
						"Esta acción interrumpirá la generación de la base de datos.\n"
								+ "El diccionario no puede funcionar sin ella.\n"
								+ "¿Está seguro de que deseas interrumpir este proceso?",
						"Advertencia", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			JOptionPane
					.showMessageDialog(
							frame,
							"Para generar la base de datos en otro momento, ve al directorio\n"
									+ "en el que has instalado el programa o, en Windows, a\n"
									+ "Inicio->Programas->ME Diccionario de Rimas\n"
									+ "y ejecuta generador.jar", "Advertencia",
							JOptionPane.INFORMATION_MESSAGE);

			System.exit(0);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);

	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}
	
	public static void event(int progress, String message){
		
		thisInstance.countProgress+=progress;
		thisInstance.progressBar.setValue(thisInstance.countProgress);
		thisInstance.cwordLabel.setText(message);
	}

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception ex) {
			}
		}

		try{
			new Main(args, Utils.getApplicationPath(), true);
		}catch(Exception e){
			new ErrorDialog(null,e);
		}

	}
	
	

}

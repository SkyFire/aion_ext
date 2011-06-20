/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionengine.manager.ui;
import  com.aionengine.manager.ui.ServerButton;
import  com.aionengine.manager.OutputReader;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;

/**
 * @author vyaslav
 *
 */
public class ManagerMainView extends JFrame {
	JTabbedPane screenTabs=new JTabbedPane();
	JPanel screen=new JPanel();
	
	JTextArea outTextArea;
	JScrollPane outTextPane;
	
	String loginText;
	String gameText;
	String chatText;
	String title= "Aion Engine Server Manager";
	
	boolean started[]={false,false,false};//login/game/chat
	
	final ServerButton loginButton;
	final ServerButton gameButton;
	final ServerButton chatButton;
	
	final JButton btStart;
	final JButton btStop;
	
	private final static String loginButtonText="Login";
	private final static String gameButtonText="Game";
	private final static String chatButtonText="Chat";
	
	final Process procList[]={null,null,null};
	final OutputReader orList[]={null,null,null};
	
	
	final ButtonGroup bg;
	
	ActionListener bgLsnr=new ActionListener(){
		public void actionPerformed (ActionEvent e) {
	        if (bg.isSelected(loginButton.getModel())){
	        	outTextArea.setText(loginText); 
	        	btStart.setEnabled(!started[0]);
	        	btStop.setEnabled(started[0]);}
	        
	        else if (bg.isSelected(gameButton.getModel())){
	        	outTextArea.setText(gameText);
	        	btStart.setEnabled(!started[1]);
	        	btStop.setEnabled(started[1]);}
	        else {
	        	outTextArea.setText(chatText);
	        	btStart.setEnabled(!started[2]);
	        	btStop.setEnabled(started[2]);}
		}
	};
	

	/**
	 * @throws HeadlessException
	 */
	public ManagerMainView() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		Container pane=this.getContentPane();
		JPanel head = new JPanel();
		pane.add(head, BorderLayout.PAGE_START);
		
		head.add(new JPanel());
		bg=new ButtonGroup();
		
		loginButton = new ServerButton(loginButtonText,new CirclePanel(Color.RED));
		bg.add(loginButton);
		head.add(loginButton.getServerCircle());		
		head.add(loginButton);
		
		bg.setSelected(loginButton.getModel(), true);
		
		gameButton = new ServerButton(gameButtonText,new CirclePanel(Color.RED));
		
		bg.add(gameButton);
		head.add(gameButton.getServerCircle());
		head.add(gameButton);

		chatButton = new ServerButton(chatButtonText,new CirclePanel(Color.RED));
		bg.add(chatButton);
		head.add(chatButton.getServerCircle());
		head.add(chatButton);

		JPanel body = new JPanel();
		body.setPreferredSize(new Dimension(200, 100));

		pane.add(body, BorderLayout.CENTER);
		
		JPanel leftPanel=new JPanel();
		JPanel outPanel=new JPanel();
		
		body.setLayout(new BorderLayout());

		Dimension minimumSize = new Dimension(150, 100);
		leftPanel.setMinimumSize(minimumSize);
		leftPanel.setPreferredSize(new Dimension(150, 100));
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		outPanel.setMinimumSize(minimumSize);
		outPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		body.add(leftPanel, BorderLayout.LINE_START);		
		body.add(outPanel, BorderLayout.CENTER);
		
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
       
		btStart=new JButton("Start");
		
		btStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btStart.setMaximumSize(new Dimension(130, 30));
		
		leftPanel.add(btStart);
		btStop=new JButton("Stop");
		btStop.setAlignmentX(Component.CENTER_ALIGNMENT);
		btStop.setMaximumSize(new Dimension(130, 30));
		btStop.setEnabled(false);
					
		
		leftPanel.add(btStop);
		
		outPanel.setLayout(new BorderLayout());
		 
		outTextArea = new JTextArea();
		outTextPane = new JScrollPane(outTextArea);
		outPanel.add(outTextPane, BorderLayout.CENTER);
		
		final ManagerMainView thisMMV = this;
		
		loginButton.addActionListener(bgLsnr);
		gameButton.addActionListener(bgLsnr);
		chatButton.addActionListener(bgLsnr);
		
		btStart.addActionListener (new ActionListener() {
			  public void actionPerformed (ActionEvent e) {
					        String cmd="java";
							String arg;
					        String arg1;
					        String arg2="-ea";
					        String arg3="-cp";
					        String arg4;
					        String arg5;
					        String arg6;
					        
					        File dr;
					        
					        ServerButton sb;
					        int serverId;
					        
					        if (bg.isSelected(loginButton.getModel())){
					        	serverId=0;
					        	sb=loginButton;
								arg="-Xms8m";
								arg1="-Xmx32m";
						        arg4="./libs/*;ax-login-1.0.1.jar";
					        	arg5="-Xbootclasspath/p:./libs/jsr166.jar";
						        arg6="loginserver.LoginServer";
						        dr=new File("./loginserver/");
						        loginText="";
					        }
					        else if (bg.isSelected(gameButton.getModel())){
					        	serverId=1;
					        	sb=gameButton;
								arg="-Xms512m";
								arg1="-Xmx1536m";
						        arg4="./libs/*;ax-game-1.0.1.jar";
						        arg5="-Xbootclasspath/p:./libs/jsr166.jar";
						        arg6="gameserver.GameServer";
						        dr=new File("./gameserver/");
						        gameText="";
					        }
					        else {
					        	serverId=2;
					        	sb=chatButton;
								arg="-Xms8m";
								arg1="-Xmx32m";
						        arg4="./libs/*;ax-chat-1.0.1.jar";
						        arg5="com.aionengine.chatserver.ChatServer";
						        arg6="";
						        dr=new File("./chatserver/");
						        chatText="";
					        }
					        
					        sb.getServerCircle().setColor(Color.GREEN);
					        started[serverId]=true;
					        
		
					        try
					        {
					            ProcessBuilder pb=new ProcessBuilder(cmd,arg,arg1,arg2,arg3,arg4,arg5,arg6);
					            
					            pb.directory(dr);
					            procList[serverId] = pb.start();
					            
					            orList[serverId] = new OutputReader(procList[serverId].getInputStream(), thisMMV, serverId);
		
					            orList[serverId].start();
					        } catch (Throwable t)
					          {
					            t.printStackTrace();
					          }
					        btStop.setEnabled(true);
					        btStart.setEnabled(false);
					        
					        thisMMV.updateOutput(sb.getText() + " server has been started",serverId);
					    }

				  
				});

		btStop.addActionListener (new ActionListener() {
			  public void actionPerformed (ActionEvent e) {
					        
					        JButton but=(JButton)(e.getSource());
					        
					        ServerButton sb;
					        int serverId;
					        
					        if (bg.isSelected(loginButton.getModel())){
					        	serverId=0;
					        	sb=loginButton;
					        }
					        else if (bg.isSelected(gameButton.getModel())){
					        	serverId=1;
					        	sb=gameButton;
					        }
					        else {
					        	serverId=2;
					        	sb=chatButton;
					        }
					        
					        sb.getServerCircle().setColor(Color.RED);
					        started[serverId]=false;
					        
		
					        try
					        {
					            orList[serverId].interrupt();
					            orList[serverId]=null;
					            procList[serverId].destroy();
					            procList[serverId]=null;
					            
					        } catch (Throwable t)
					          {
					            t.printStackTrace();
					          }
					        btStop.setEnabled(false);
					        btStart.setEnabled(true);
					        
					        thisMMV.updateOutput(sb.getText() + " server has been killed",serverId);
					    }

				  
				});
	}
	
	public void updateOutput(String line,int serverId){
		String text;
		
		switch (serverId){
		case 0:loginText+=line+'\n';text=loginText;break;
		case 1:gameText+=line+'\n';text=gameText;break;
		case 2:chatText+=line+'\n';text=chatText;break;
			default: text="Process Input Stream ERROR";
		}
		
        if ((bg.isSelected(loginButton.getModel())   && serverId!=0)
        	|| (bg.isSelected(gameButton.getModel()) && serverId!=1)
        	|| (bg.isSelected(chatButton.getModel()) && serverId!=2))
        return;
        
		outTextArea.setText(text);
		int length = outTextArea.getDocument().getLength();
		outTextArea.setCaretPosition(length);
	}

}
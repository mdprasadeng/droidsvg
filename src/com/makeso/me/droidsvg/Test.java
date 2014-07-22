package com.makeso.me.droidsvg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.makeso.me.droidsvg.Rasterizer.DPI;

public class Test {

	private JFrame frame;
	private JTextField svgField;
	private JTextField pngField;
	private JFileChooser fileChooser;
	private JComboBox<DPI> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test window = new Test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Test() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Droid SVG");
		frame.setBounds(100, 100, 531, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JLabel lblSvgFolder = new JLabel("SVG Folder");
		springLayout.putConstraint(SpringLayout.NORTH, lblSvgFolder, 60, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblSvgFolder, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblSvgFolder);
		
		JLabel lblPngFolder = new JLabel("PNG Folder");
		springLayout.putConstraint(SpringLayout.EAST, lblPngFolder, 0, SpringLayout.EAST, lblSvgFolder);
		frame.getContentPane().add(lblPngFolder);
		
		svgField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, svgField, 33, SpringLayout.EAST, lblSvgFolder);
		springLayout.putConstraint(SpringLayout.EAST, svgField, -124, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(svgField);
		svgField.setColumns(10);
		
		pngField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, pngField, 33, SpringLayout.EAST, lblPngFolder);
		springLayout.putConstraint(SpringLayout.NORTH, lblPngFolder, 3, SpringLayout.NORTH, pngField);
		springLayout.putConstraint(SpringLayout.SOUTH, svgField, -19, SpringLayout.NORTH, pngField);
		springLayout.putConstraint(SpringLayout.NORTH, pngField, 93, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(pngField);
		pngField.setColumns(10);
		
		
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		JButton svgButton = new JButton("Open");
		svgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                svgField.setText(file.getAbsolutePath());
	            } else {
	            }
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, svgButton, 0, SpringLayout.SOUTH, lblSvgFolder);
		springLayout.putConstraint(SpringLayout.EAST, svgButton, -44, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(svgButton);
		
		JButton pngButton = new JButton("Open");
		pngButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                pngField.setText(file.getAbsolutePath());
	            } else {
	            	
	            }
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, pngButton, 15, SpringLayout.SOUTH, svgButton);
		springLayout.putConstraint(SpringLayout.EAST, pngField, -21, SpringLayout.WEST, pngButton);
		springLayout.putConstraint(SpringLayout.EAST, pngButton, 0, SpringLayout.EAST, svgButton);
		frame.getContentPane().add(pngButton);
		
		comboBox = new JComboBox<DPI>();
		springLayout.putConstraint(SpringLayout.EAST, comboBox, -323, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(comboBox);
		DPI[] dpis = Rasterizer.DPI.getAll();
		for(DPI dpi:dpis){
			comboBox.addItem(dpi);
		}
		
		
		JLabel lblDesignedAt = new JLabel("Designed For");
		springLayout.putConstraint(SpringLayout.NORTH, lblDesignedAt, 37, SpringLayout.SOUTH, lblPngFolder);
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 22, SpringLayout.EAST, lblDesignedAt);
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, -3, SpringLayout.NORTH, lblDesignedAt);
		springLayout.putConstraint(SpringLayout.WEST, lblDesignedAt, 0, SpringLayout.WEST, lblSvgFolder);
		frame.getContentPane().add(lblDesignedAt);
		
		JButton generate = new JButton("Generate");
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String svgDir = svgField.getText();
				String pngDir = pngField.getText();
				try{
					Rasterizer.converDir(svgDir, pngDir, DPI.getAll(), (DPI)comboBox.getSelectedItem());
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(frame,
						    ""+e.getLocalizedMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, generate, 169, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, generate, -36, SpringLayout.SOUTH, frame.getContentPane());
		frame.getContentPane().add(generate);
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame {

	// Create the panels for the top of the application. One panel for each
	// line and one to contain both of those panels.
	JPanel topPanel = new JPanel(new FlowLayout()), botPanel = new JPanel(new FlowLayout()),
			mainPanel = new JPanel(new GridLayout(2, 1, 0, 1));
	// create the widgets for the firstLine Panel.
	JButton undo = new JButton("Undo"), clear = new JButton("Clear"), firstColor = new JButton("1st Color"),
			secondColor = new JButton("2nd Color");
	String[] s = new String[] { "Line", "Oval", "Rectangle" };
	JComboBox<String> shapesOptions = new JComboBox<String>(s);
	JCheckBox filledCheckBox = new JCheckBox("Filled"), gradientCheckBox = new JCheckBox("Use Gradient"),
			dashedCheckBox = new JCheckBox("Dashed");
	// create the widgets for the secondLine Panel.
	JTextField lineWidth = new JTextField(3), dashLength = new JTextField(3);
	JLabel lineWidthLabel = new JLabel("Line Width:"), dashLengthLabel = new JLabel("Dash Length:"),
			shapesLabel = new JLabel("Shapes:");
	// Variables for drawPanel.
	DrawPanel drawPanel = new DrawPanel();
	private float width = 10, length = 10;
	private Color color_1, color_2;
	private String shape;
	private BasicStroke stroke;
	ArrayList<MyShapes> shapesList = new ArrayList<MyShapes>();
	// add status label
	JLabel mouseStatus = new JLabel();

	// Constructor for DrawingApplicationFrame
	public DrawingApplicationFrame() {
		// add widgets to panels
		// firstLine widgets
		topPanel.add(undo);
		topPanel.add(clear);
		topPanel.add(shapesLabel);
		topPanel.add(shapesOptions);
		topPanel.add(filledCheckBox);
		// secondLine widgets
		botPanel.add(gradientCheckBox);
		botPanel.add(firstColor);
		botPanel.add(secondColor);
		botPanel.add(lineWidthLabel);
		botPanel.add(lineWidth);
		botPanel.add(dashLengthLabel);
		botPanel.add(dashLength);
		botPanel.add(dashedCheckBox);
		// add top panel of two panels
		mainPanel.add(topPanel);
		mainPanel.add(botPanel);
		// add topPanel to North, drawPanel to Center, and statusLabel to South
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(drawPanel, BorderLayout.CENTER);
		this.add(mouseStatus, BorderLayout.SOUTH);
		// add listeners and event handlers
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (shapesList.size() > 0) {
					shapesList.remove(shapesList.size() - 1);
				}
			}
		});
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (shapesList.size() > 1) {
					shapesList.clear();
					;
				}
			}
		});
		firstColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				color_1 = JColorChooser.showDialog(new JFrame(), "Choose a Color", Color.BLACK);
			}
		});
		secondColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				color_2 = JColorChooser.showDialog(new JFrame(), "Choose a Color", Color.BLACK);
				System.out.println(shapesList.size());
			}
		});

	}

	// Create event handlers, if needed

	// Create a private inner class for the DrawPanel.
	private class DrawPanel extends JPanel {
		MouseHandler m = new MouseHandler();
		JPanel p = new JPanel();

		public DrawPanel() {
			p.addMouseMotionListener(new MouseHandler());
			this.add(p);
			this.addMouseMotionListener(m);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			this.setBackground(Color.WHITE);

			// loop through and draw each shape in the shapes arraylist
			for (int i = 0; i < shapesList.size(); i++) {
				(shapesList.get(i)).draw(g2d);
			}

		}

		private class MouseHandler extends MouseAdapter implements MouseMotionListener {
			
			@Override
			public void mousePressed(MouseEvent event) {
				System.out.println("Hi");

				width = Integer.parseInt(lineWidth.getText());
				length = Integer.parseInt(dashLength.getText());
				shape = shapesOptions.getItemAt(shapesOptions.getSelectedIndex());

				if (dashedCheckBox.isSelected()) {
					stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f,
							new float[] { length }, 0f);
				} else {
					stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				}
				if (gradientCheckBox.isSelected()) {
					Paint paint = new GradientPaint(0, 0, color_1, 50, 50, color_2, true);

					if (shape.equals("Line")) {
						shapesList.add(new MyLine(event.getPoint(), event.getPoint(), paint, stroke));
					} else if (shape.equals("Oval")) {
						shapesList.add(new MyOval(event.getPoint(), event.getPoint(), paint, stroke,
								filledCheckBox.isSelected()));
					} else {
						shapesList.add(new MyOval(event.getPoint(), event.getPoint(), paint, stroke,
								filledCheckBox.isSelected()));
					}
				} else {
					if (shape.equals("Line")) {
						shapesList.add(new MyLine(event.getPoint(), event.getPoint(), color_1, stroke));
					} else if (shape.equals("Oval")) {
						shapesList.add(new MyOval(event.getPoint(), event.getPoint(), color_1, stroke,
								filledCheckBox.isSelected()));
					} else {
						shapesList.add(new MyOval(event.getPoint(), event.getPoint(), color_1, stroke,
								filledCheckBox.isSelected()));
					}
				}
				mouseStatus.setText("(" + event.getX() + ", " + event.getY() + ")");

			}

			@Override
			public void mouseReleased(MouseEvent event) {
				System.out.println("Hi");

				shapesList.get(shapesList.size() - 1).setEndPoint(event.getPoint());
				mouseStatus.setText("(" + event.getX() + ", " + event.getY() + ")");

			}

			@Override
			public void mouseDragged(MouseEvent event) {
				(shapesList.get(shapesList.size() - 1)).setEndPoint(event.getPoint());
				mouseStatus.setText("(" + event.getX() + ", " + event.getY() + ")");
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent event) {
				mouseStatus.setText("(" + event.getX() + ", " + event.getY() + ")");
			}
		}

	}
}

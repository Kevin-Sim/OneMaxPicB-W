import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Gui extends JFrame implements Observer {

	private JPanel contentPane;
	private JPanel displayPanel;
	private JButton runButton;
	private Individual individual;
	private HillClimber hc;
	private Thread thread;	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(Color.GRAY);
		controlPanel.setPreferredSize(new Dimension(150, 1000));
		contentPane.add(controlPanel, BorderLayout.WEST);
		controlPanel.setLayout(null);

		runButton = new JButton("Start");
		runButton.setBackground(Color.GREEN);
		runButton.addActionListener(new ActionListener() {			

			public void actionPerformed(ActionEvent arg0) {
				if (hc == null || !hc.isRunning()) {
					hc = new HillClimber();
					hc.addObserver(Gui.this);
					thread = new Thread(hc);
					thread.start();
					runButton.setBackground(Color.red);
					runButton.setText("Stop");
				} else {
					hc.stop();
					runButton.setBackground(Color.green);
					runButton.setText("Run");
				}
			}
		});
		runButton.setBounds(26, 21, 89, 23);
		controlPanel.add(runButton);

		JButton loadBtn = new JButton("Load");
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(hc != null && hc.isRunning()) {
					JOptionPane.showMessageDialog(null, "Stop The current Algorithm First");
					return;
				}
				JFileChooser fc = new JFileChooser("./pics/");				
				int choice = fc.showOpenDialog(null);
				if (choice == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					Parameters.getInstance().setImageFilename(f.getAbsolutePath());
					individual = Individual.getOptimal();
					displayPanel.setPreferredSize(new Dimension(Parameters.getInstance().getWidth(), Parameters.getInstance().getHeight()) );
					repaint();
				}
			}
		});
		loadBtn.setBounds(26, 50, 89, 23);
		controlPanel.add(loadBtn);
		
		JButton btnSaveSvg = new JButton("Save SVG");
		btnSaveSvg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int answer = JOptionPane.showConfirmDialog(null, "Warning A 1000 X 563 Image will create a 40MB SVG and Take a While. Are You Sure?");
					if(answer == JOptionPane.YES_OPTION) {
						SVG.save(displayPanel);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSaveSvg.setBounds(26, 160, 89, 23);
		controlPanel.add(btnSaveSvg);
		
		JSlider slider = new JSlider();
		slider.setEnabled(false);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Parameters.thresholdValue = slider.getValue();
				Parameters.getInstance().setImageFilename(Parameters.getInstance().getImageFilename());
				individual = Individual.getOptimal();
				repaint();
			}
		});
		slider.setMaximum(255);
		slider.setBounds(10, 123, 130, 26);
		controlPanel.add(slider);
		
		JCheckBox chckbxThreshold = new JCheckBox("Threshold");
		chckbxThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxThreshold.isSelected()) {
					Parameters.threshold = true;
					Parameters.thresholdValue = slider.getValue();
					slider.setEnabled(true);
				}else {
					Parameters.threshold = false;
					slider.setEnabled(false);
				}
				Parameters.getInstance().setImageFilename(Parameters.getInstance().getImageFilename());
				individual = Individual.getOptimal();
				repaint();
			}
		});
		chckbxThreshold.setBounds(26, 92, 97, 23);
		controlPanel.add(chckbxThreshold);

		displayPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
//				super.paint(g);
				if (individual != null) {
					individual.draw(g);
				}
//				Individual ind = Individual.getOptimal();
//				ind.draw(g);
//				g.drawImage(ind.getImageFromChromosome(),0,0, null);
//				g.drawImage(Parameters.getInstance().getImage(), 0, 0, null);

			}

		};
		displayPanel.setPreferredSize(new Dimension(Parameters.getInstance().getWidth(), Parameters.getInstance().getHeight()) );
		contentPane.add(displayPanel, BorderLayout.CENTER);
		individual = Individual.getOptimal();
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Individual) {
			individual = (Individual) arg;
			repaint();
		}
		if(o instanceof HillClimber) {
			if(!((HillClimber)o).isRunning()) {
				runButton.setBackground(Color.green);
				runButton.setText("Run");
			}
		}
	}
}

package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pathfinding.Grid;
import pathfinding.Pathfinder;

public class ButtonPanel extends JPanel{
	Grid grid;
    JButton btnCreateMap, btnDeleteMap, btnStart, btnReset, btnPause;
	JComboBox<String> cboTypeOfCell, cboTypeOfHeuristic;
	JSpinner stepSpinner;
	JPanel controlPanel, optionPanel, heuristicPanel;
	JLabel cboTypeOfCellLabel, stepSpinnerLabel, cboTypeOfHeuristicLabel;
	JCheckBox ckDiCheo, ckDiNgang;
	JLabel lblResolutonOfMap;
	JButton btnDecreaseResolution, btnIncreaseResolution, btnResult;
	JTextField txtRows, txtResolutonOfMap;
	JButton btnDifineMap;
	JTextArea txtResult;
	
	public ButtonPanel(Grid grid) {
		this.grid = grid;
		addControls();
		addEvents();
	}

	
	private void addControls() {
		this.setLayout(null);
		
		btnCreateMap = new JButton("Tạo bản đồ");
		btnDeleteMap = new JButton("Xóa bản đồ");
		btnStart = new JButton("Bắt đầu");
		btnReset = new JButton("Reset đường đi");
		btnPause = new JButton("Pause");
		
		btnIncreaseResolution = new JButton("Tăng");
		lblResolutonOfMap = new JLabel("Độ phân giải");
		txtResolutonOfMap = new JTextField(5);
		btnDecreaseResolution = new JButton("Giảm");
		txtResolutonOfMap.setText(grid.getRows() + "*" + grid.getColumns());
				
		String editList[] = {"Khởi hành", "Đích", "Tường", "Xóa trắng"};
	    cboTypeOfCell = new JComboBox(editList);
	    cboTypeOfCellLabel = new JLabel("Chọn:");
	    cboTypeOfCellLabel.setLabelFor(cboTypeOfCell);
	    cboTypeOfCellLabel.setHorizontalAlignment(JLabel.LEFT);
	    cboTypeOfCellLabel.setSize(100, 30);
	    
	    String algorithmList[] = {"Manhattan", "Diagonal", "Euclidean"};
	    cboTypeOfHeuristic = new JComboBox(algorithmList);
	    cboTypeOfHeuristicLabel = new JLabel("Chọn hàm Heuristic:");
	    cboTypeOfHeuristicLabel.setLabelFor(cboTypeOfHeuristic);
	    cboTypeOfHeuristicLabel.setHorizontalAlignment(JLabel.LEFT);
	    cboTypeOfHeuristicLabel.setSize(100, 30);
	    
	    SpinnerNumberModel stepSizeModel = new SpinnerNumberModel(0, 0, 1000, 50);
	    stepSpinner = new JSpinner(stepSizeModel);
	    stepSpinnerLabel = new JLabel("Time per Step (ms): ");
	    stepSpinnerLabel.setLabelFor(stepSpinner);
	    stepSpinnerLabel.setHorizontalAlignment(JLabel.RIGHT);
	    
	    optionPanel = new JPanel(new GridLayout(1, 1, 10, 10));
	    optionPanel.add(stepSpinnerLabel);
		optionPanel.add(stepSpinner);
	    optionPanel.add(cboTypeOfCellLabel);
	    optionPanel.add(cboTypeOfCell);
//	    optionPanel.add(cboTypeOfHeuristic);
//	    optionPanel.add(cboTypeOfHeuristicLabel);
	    
	    heuristicPanel = new JPanel(new GridLayout(1, 1, 10, 10));
	    heuristicPanel.add(cboTypeOfHeuristicLabel);
	    heuristicPanel.add(cboTypeOfHeuristic);
	    
	    JPanel pnCheckBox = new JPanel();
	    ckDiCheo = new JCheckBox("Cho phép đi chéo");
	    ckDiNgang = new JCheckBox("Cho phép đi ngang");
	    ckDiCheo.setSelected(true);
	    ckDiNgang.setSelected(true);
	    pnCheckBox.add(ckDiCheo);
	    pnCheckBox.add(ckDiNgang);
	    
	    btnResult = new JButton("Kết quả:");
	    txtResult = new JTextArea();
 		
	    controlPanel = new JPanel();
	    controlPanel.setBounds(10, 10, 330, 300);
	    controlPanel.setLayout(null);
	    
	    btnCreateMap.setBounds(0, 0, 150, 30);
	    btnDeleteMap.setBounds(160, 0, 150, 30);
	    btnStart.setBounds(0, 40, 150, 30);
	    btnPause.setBounds(160, 40, 150, 30);
	    btnReset.setBounds(0, 80, 150, 30);
	    btnDecreaseResolution.setBounds(0, 120, 70, 30);
	    lblResolutonOfMap.setBounds(80, 120, 100, 30);
	    txtResolutonOfMap.setBounds(190, 120, 50, 30);
	    btnIncreaseResolution.setBounds(240, 120, 70, 30);
	    optionPanel.setBounds(0, 160, 300, 30);
	    heuristicPanel.setBounds(0, 200, 300, 30);
	    pnCheckBox.setBounds(10, 350, 300, 30);
	    btnResult.setBounds(10, 400, 100, 30);
	    txtResult.setBounds(10, 450, 300, 130);
	   
	    controlPanel.add(btnCreateMap);
	    controlPanel.add(btnDeleteMap);
	    controlPanel.add(btnStart);
	    controlPanel.add(btnPause);
	    controlPanel.add(btnReset);
	    controlPanel.add(btnDecreaseResolution);
	    controlPanel.add(lblResolutonOfMap);
	    controlPanel.add(txtResolutonOfMap);
	    controlPanel.add(btnIncreaseResolution);
	    controlPanel.add(optionPanel, BorderLayout.CENTER);
	    controlPanel.add(heuristicPanel);
	    
	    this.add(controlPanel);
	    this.add(pnCheckBox);
	    this.add(btnResult);
	    this.add(txtResult);	    
	   
	}
	
	private void addEvents() {
		ckDiCheo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ckDiCheo.isSelected())
					grid.setDiagonalable(true);
				else
					grid.setDiagonalable(false);
					grid.creatMap();
			}
		});
		ckDiNgang.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ckDiNgang.isSelected())
					grid.setHorizionable(true);
				else
					grid.setHorizionable(false);
				grid.creatMap();
			
			}
		});

		btnIncreaseResolution.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.increaseResolution();
				txtResolutonOfMap.setText(grid.getRows() + "*" + grid.getColumns());
			}
		});
		btnDecreaseResolution.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.decreaseResolution();
				txtResolutonOfMap.setText(grid.getRows() + "*" + grid.getColumns());
			}
		});
		btnCreateMap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.creatMap();
			}
		});
		btnDeleteMap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.deleteMap();
			}
		});
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Pathfinder.isRunning)
					return;
				SwingWorker worker = new SwingWorker<Void,Void>(){
					protected Void doInBackground(){
						grid.start((int)stepSpinner.getValue());
						return null;
					}
				};
				worker.execute();
			}
		});
		btnPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.pause();
			}
		});
		btnReset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.reset();
				btnStart.setEnabled(true);
				btnReset.setEnabled(true);
				btnCreateMap.setEnabled(true);
				btnDeleteMap.setEnabled(true);
				System.out.println("reset");
			}
		});
		cboTypeOfCell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
					grid.setPositionable(cboTypeOfCell.getSelectedIndex());
			}
		});
		
		txtResolutonOfMap.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				txtResolutonOfMap.setText(grid.getRows() + "*" + grid.getColumns());
			}
		});
		
		cboTypeOfHeuristic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.setHeuristic(cboTypeOfHeuristic.getSelectedIndex());
				if(cboTypeOfHeuristic.getSelectedIndex()==0) grid.setDiagonalable(false);
			}
		});
		
		btnResult.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTxtResult();
			}
		});
	}
	
	private void updateTxtResult() {
		txtResult.setText( "Tọa độ điểm xuất phát: (" + grid.getStartCell().getPosition().getX() + "," + grid.getStartCell().getPosition().getY() + ")"
						  + "\nTọa độ điểm đích: (" + grid.getGoalCell().getPosition().getX() + "," + grid.getGoalCell().getPosition().getY() + ")"
						  +"\nSố đỉnh đã xét: " + grid.getPathfinder().getNumNodeInOpenList()
						  + "\nĐộ dài đường đi: " + grid.getPathfinder().getPathLength()
						  + "\nThời gian tìm kiếm: " + grid.getPathfinder().getSearchTime() + "ms");
		
	}
	
}

package ce325.hw3;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class Sudoku extends JFrame implements ActionListener{
	private static final int CLUSTER = 3;
    private static final int MAX_ROWS = 9;
    private static final float FIELD_PTS = 32f;
    private static final int GAP = 3;
    private static final Color BG = Color.GRAY;
    private static final Color SOLVED_BG = Color.LIGHT_GRAY;
    public static final int TIMER_DELAY = 2 * 1000;
    private JButton[][] fieldGrid = new JButton[MAX_ROWS][MAX_ROWS];
    private boolean[][] TruthGrid = new boolean[MAX_ROWS][MAX_ROWS];
    public static final int WIDTH = 850;
	public static final int HEIGHT = 800;
	private int selected_row = -1;
	private int selected_col = -1;
	private int[][] sudokuTable = new int[MAX_ROWS][MAX_ROWS];
	private int[][] Solution = new int[MAX_ROWS][MAX_ROWS];
	private JCheckBox verify;

	public Sudoku(){
		super("Sudoku");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		JPanel bigPanel = new JPanel(new BorderLayout());

		JPanel mainPanel = new JPanel(new GridLayout(CLUSTER, CLUSTER));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        mainPanel.setBackground(BG);
        JPanel[][] panels = new JPanel[CLUSTER][CLUSTER];
        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[i].length; j++) {
                panels[i][j] = new JPanel(new GridLayout(CLUSTER, CLUSTER, 1, 1));
                panels[i][j].setBackground(BG);
                panels[i][j].setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
                mainPanel.add(panels[i][j]);
            }
        }

        for (int row = 0; row < fieldGrid.length; row++) {
            for (int col = 0; col < fieldGrid[row].length; col++) {
                fieldGrid[row][col] = new JButton();
                fieldGrid[row][col].addActionListener( this );
    			fieldGrid[row][col].setActionCommand("empty" +","+ row + "," + col);
    			TruthGrid[row][col] = false;
    			fieldGrid[row][col].setBackground(Color.WHITE);
    			//analoga me url tha allazei se true
                int i = row / 3;
                int j = col / 3;
                panels[i][j].add(fieldGrid[row][col]);
            }
        }

        JPanel lowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton button1 = new JButton("1");
    	button1.addActionListener( this );
    	button1.setActionCommand("1");
    	lowPanel.add(button1);
    	JButton button2 = new JButton("2");
    	button2.addActionListener( this );
    	button2.setActionCommand("2");
    	lowPanel.add(button2);
    	JButton button3 = new JButton("3");
    	button3.addActionListener( this );
    	button3.setActionCommand("3");
    	lowPanel.add(button3);
    	JButton button4 = new JButton("4");
    	button4.addActionListener( this );
    	button4.setActionCommand("4");
    	lowPanel.add(button4);
    	JButton button5 = new JButton("5");
    	button5.addActionListener( this );
    	button5.setActionCommand("5");
    	lowPanel.add(button5);
    	JButton button6 = new JButton("6");
    	button6.addActionListener( this );
    	button6.setActionCommand("6");
    	lowPanel.add(button6);
    	JButton button7 = new JButton("7");
    	button7.addActionListener( this );
    	button7.setActionCommand("7");
    	lowPanel.add(button7);
    	JButton button8 = new JButton("8");
    	button8.addActionListener( this );
    	button8.setActionCommand("8");
    	lowPanel.add(button8);
    	JButton button9 = new JButton("9");
    	button9.addActionListener( this );
    	button9.setActionCommand("9");
    	lowPanel.add(button9);
    	ImageIcon eraser = new ImageIcon("eraser.png");
    	ImageIcon rubik = new ImageIcon("rubik.png");
    	ImageIcon undo = new ImageIcon("undo.png");
    	JButton buttonErase = new JButton(eraser);
    	buttonErase.addActionListener( this );
    	buttonErase.setActionCommand("Eraser");
    	buttonErase.setSize(20, 20);
    	lowPanel.add(buttonErase);
    	JButton buttonUndo = new JButton(undo);
    	buttonUndo.addActionListener( this );
    	buttonUndo.setActionCommand("Undo");
    	lowPanel.add(buttonUndo);
    	verify = new JCheckBox("Verify against solution");
    	verify.addActionListener(this);
    	verify.setActionCommand("Verify");
    	verify.setSelected(false);
    	lowPanel.add(verify);
    	JButton buttonRubik = new JButton(rubik);
    	buttonRubik.addActionListener( this );
    	buttonRubik.setActionCommand("Rubik");
    	lowPanel.add(buttonRubik);

    	JMenuBar menubar = new JMenuBar();
    	JMenu menu = new JMenu("New Game");
    	JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(this);
		JMenuItem intermediate = new JMenuItem("Intermediate");
		intermediate.addActionListener(this);
		JMenuItem expert = new JMenuItem("Expert");
		expert.addActionListener(this);
		menu.add(easy);
		menu.add(intermediate);
		menu.add(expert);
		menubar.add(menu);

		setJMenuBar(menubar);
    	
        bigPanel.add(mainPanel, BorderLayout.CENTER);
        bigPanel.add(lowPanel, BorderLayout.SOUTH);

        add(bigPanel, BorderLayout.CENTER);
	}


	public boolean solveSudoku(int i, int j){
		if (sudokuTable[i][j] == 0){
			for(int k = 1; k <= 9; k++){
				boolean check = true;
				for(int l = 0; l < MAX_ROWS; l++){
					//check if ok in row
					if (Solution[i][l] == k){
						check = false;
						break;
					}
					//check if ok in column
					if (Solution[l][j] == k){
						check = false;
						break;
					}
				}
				////check if ok in cluster
				int rowOffset = (i/CLUSTER)*CLUSTER;
				int colOffset = (j/CLUSTER)*CLUSTER;
				for(int row = 0; row < CLUSTER; row++){
					for (int col = 0; col < CLUSTER; col++){
						if (Solution[rowOffset+row][colOffset+col] == k){
							check = false;
							break;
						}
					}
				}
				if (check == true){
					//System.out.println("i: " + i + ", j: " + j + " fits: " + k);
					Solution[i][j] = k;
					if(j == MAX_ROWS-1 && i == MAX_ROWS-1){
						return true;
					}
					else if (j == MAX_ROWS-1){
						boolean success = solveSudoku(i+1, 0);
						if (success == true){
							return true;
						}
					}
					else{
						boolean success = solveSudoku(i, j+1);
						if (success == true){
							return true;
						}
					}
				}
			}
			Solution[i][j] = 0;
			return false;
		}
		else{
			if(j == MAX_ROWS-1 && i == MAX_ROWS-1){
				return true;
			}
			else if (j == MAX_ROWS-1){
				boolean success = solveSudoku(i+1, 0);
				if (success == true){
					return true;
				}
			}
			else{
				boolean success = solveSudoku(i, j+1);
				if (success == true){
					return true;
				}
			}
			return false;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ("Easy".equals(e.getActionCommand())) {
			try{
				URL url = new URL("http://gthanos.inf.uth.gr/~gthanos/sudoku/exec.php?difficulty=easy");
				URLConnection conn = url.openConnection();
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				for(int i = 0; i < MAX_ROWS; i++){
					for(int j = 0; j < MAX_ROWS; j++){
						int number = isr.read();
						if((char) number =='\n' || (char) number ==' '){
							number = isr.read();
						}
						if ((char) number =='.'){
							sudokuTable[i][j] = 0;
							Solution[i][j] = 0;
							fieldGrid[i][j].setText("");
						}
						else{
							String s = ""+(char)number;
							int x = Integer.parseInt(s);
							sudokuTable[i][j] = x;
							Solution[i][j] = x;
							String str = "";
							str += x;
							fieldGrid[i][j].setText(str);
						}
					}
				}
				boolean success = solveSudoku(0, 0);
				if(success != true){
					System.out.println("Unsolved");
					System.exit(1);
				}
			}
			catch (MalformedURLException ex){
				System.out.println("MalformedURLException");
				System.exit(1);
			}
			catch (IOException ex){
				System.out.println("IOException");
				System.exit(1);
			}
		}
		else if ("Intermediate".equals(e.getActionCommand())) {
			try{
				URL url = new URL("http://gthanos.inf.uth.gr/~gthanos/sudoku/exec.php?difficulty=intermediate");
				URLConnection conn = url.openConnection();
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				for(int i = 0; i < MAX_ROWS; i++){
					for(int j = 0; j < MAX_ROWS; j++){
						int number = isr.read();
						if((char) number =='\n' || (char) number ==' '){
							number = isr.read();
						}
						if ((char) number =='.'){
							sudokuTable[i][j] = 0;
							Solution[i][j] = 0;
							fieldGrid[i][j].setText("");
						}
						else{
							String s = ""+(char)number;
							int x = Integer.parseInt(s);
							sudokuTable[i][j] = x;
							Solution[i][j] = x;
							String str = "";
							str += x;
							fieldGrid[i][j].setText(str);
						}
					}
				}
				boolean success = solveSudoku(0, 0);
				if(success != true){
					System.out.println("Unsolved");
					System.exit(1);
				}
			}
			catch (MalformedURLException ex){
				System.out.println("MalformedURLException");
				System.exit(1);
			}
			catch (IOException ex){
				System.out.println("IOException");
				System.exit(1);
			}
		}
		else if ("Expert".equals(e.getActionCommand())) {
			try{
				URL url = new URL("http://gthanos.inf.uth.gr/~gthanos/sudoku/exec.php?difficulty=expert");
				URLConnection conn = url.openConnection();
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				for(int i = 0; i < MAX_ROWS; i++){
					for(int j = 0; j < MAX_ROWS; j++){
						int number = isr.read();
						if((char) number =='\n' || (char) number ==' '){
							number = isr.read();
						}
						if ((char) number =='.'){
							sudokuTable[i][j] = 0;
							Solution[i][j] = 0;
							fieldGrid[i][j].setText("");
						}
						else{
							String s = ""+(char)number;
							int x = Integer.parseInt(s);
							sudokuTable[i][j] = x;
							Solution[i][j] = x;
							String str = "";
							str += x;
							fieldGrid[i][j].setText(str);
						}
					}
				}
				boolean success = solveSudoku(0, 0);
				if(success != true){
					System.out.println("Unsolved");
					System.exit(1);
				}
			}
			catch (MalformedURLException ex){
				System.out.println("MalformedURLException");
				System.exit(1);
			}
			catch (IOException ex){
				System.out.println("IOException");
				System.exit(1);
			}
		}
		else if("1".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("1");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 1){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("2".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("2");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 2){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("3".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("3");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 3){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("4".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("4");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 4){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("5".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("5");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 5){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("6".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("6");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 6){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("7".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("7");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 7){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("8".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("8");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 8){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("9".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("9");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
				if (verify.isSelected()){
					if (Solution[selected_row][selected_col] != 9){
						fieldGrid[selected_row][selected_col].setBackground(Color.RED);
					}
				}
			}
		}
		else if("Eraser".equals(e.getActionCommand())){
			if (selected_col != -1 && selected_row!=-1){
				fieldGrid[selected_row][selected_col].setText("");
				for (int i=0;i<MAX_ROWS;i++){
					for(int j=0;j<MAX_ROWS;j++){
						if (TruthGrid[i][j] == false){
							fieldGrid[i][j].setBackground(Color.WHITE);
						}
						else{
							fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
						}
					}
				}
			}
		}
		else if("Rubik".equals(e.getActionCommand())){
			for(int i = 0; i < MAX_ROWS; i++){
				for(int j = 0; j < MAX_ROWS; j++){
					String str = "";
					str +=  Solution[i][j];
					fieldGrid[i][j].setText(str);
				}
			}
		}
		/*else if("Verify".equals(e.getActionCommand())){
			verify.setSelected(true);
		}*/
		else if ("empty".equals(e.getActionCommand().substring(0,5))) {
			selected_row = Integer.parseInt(e.getActionCommand().substring(6,7));
			selected_col = Integer.parseInt(e.getActionCommand().substring(8,9));
			String number = fieldGrid[selected_row][selected_col].getText();
			for (int i=0;i<MAX_ROWS;i++){
				for(int j=0;j<MAX_ROWS;j++){
					if (TruthGrid[i][j] == false){
						fieldGrid[i][j].setBackground(Color.WHITE);
					}
					else{
						fieldGrid[i][j].setBackground(Color.LIGHT_GRAY);
					}

					if (fieldGrid[i][j].getText().equals(number) && !(fieldGrid[i][j].getText().equals(""))){
						fieldGrid[i][j].setBackground(Color.YELLOW);
					}
				} 
			}
		}
	}

	public static void main(String[] args) {
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			Sudoku gui = new Sudoku();
    			gui.setVisible(true);
    		}
    	});
    }

}
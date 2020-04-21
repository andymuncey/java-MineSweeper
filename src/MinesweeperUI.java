import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class MinesweeperUI {
    private JButton resetButton;
    private JPanel rootPanel;
    private JPanel minePanel;

    private final int width = 7;
    private final int height = 5;

    private Minefield mineField = new Minefield(width,height);

    public MinesweeperUI() {
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetButtons();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MinesweeperUI");
        frame.setContentPane(new MinesweeperUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private ActionListener mineButtonListener;

    private void createUIComponents() {
        GridLayout mineLayout = new GridLayout(height,width);
        minePanel = new JPanel();
        minePanel.setLayout(mineLayout);
        setupMineButtonListener();

        for (int i = 0; i < (width * height); i++) {
            JButton mineButton = new JButton(" ");
            mineButton.setName(String.valueOf(i));
            mineButton.addActionListener(mineButtonListener);
            mineButton.setActionCommand(String.valueOf(i));
            minePanel.add(mineButton);
        }
    }

    void setupMineButtonListener(){
        mineButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button " + e.getActionCommand() + " pressed");
                int mineId = Integer.parseInt(e.getActionCommand());
                int x = mineId % width;
                int y = mineId / width;
                MineInfo info = mineField.checkMine(new Point(x, y));
                JButton button = (JButton)e.getSource();
                if (info.isMine()){
                    explodeAllMines();
                    button.setText("*");
                }else {
                    button.setText(String.valueOf(info.getAdjacentMineCount()));
                }
            }
        };
    }

    void explodeAllMines(){
        for (Component component:minePanel.getComponents()){
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                int mineId = Integer.parseInt(button.getActionCommand());
                int x = mineId % width;
                int y = mineId / width;
                MineInfo info = mineField.checkMine(new Point(x, y));
                if (info.isMine()) {
                    button.setText("*");
                }
            }
        }
    }

    void resetButtons(){
        mineField = new Minefield(width,height);
        for (Component possibleButton:minePanel.getComponents()) {
            if (possibleButton instanceof JButton){
                JButton actualButton = (JButton)possibleButton;
                actualButton.setText(" ");
            }
        }
    }

}

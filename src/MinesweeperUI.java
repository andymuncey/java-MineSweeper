import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MinesweeperUI
{
    private JButton resetButton;
    private JPanel rootPanel;
    private JPanel minePanel;
    private JLabel minesRemainingLabel;

    private final int width = 7;
    private final int height = 5;

    private int minesFlagged = 0;

    private Minefield mineField = new Minefield(width, height);

    private ActionListener mineButtonListener;

    public MinesweeperUI()
    {
        resetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                resetButtons();
            }
        });
    updateRemainingCount();
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MinesweeperUI");
        frame.setContentPane(new MinesweeperUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void createUIComponents()
    {
        GridLayout mineLayout = new GridLayout(height, width);
        minePanel = new JPanel();
        minePanel.setLayout(mineLayout);
        setupMineButtonListener();
        MouseListener rightClickListener = new RightClickFlagListener();


        for (int i = 0; i < (width * height); i++)
        {
            JButton mineButton = new JButton(" ");
            mineButton.addActionListener(mineButtonListener);
            mineButton.addMouseListener(rightClickListener);
            mineButton.setActionCommand(String.valueOf(i));
            minePanel.add(mineButton);
        }

    }

    private JButton findButtonWithActionCommand(int mineId)
    {
        for (Component component : minePanel.getComponents())
        {
            if (component instanceof JButton)
            {
                JButton button = (JButton) component;
                int thisMineId = Integer.parseInt(button.getActionCommand());
                if (thisMineId == mineId)
                {
                    return button;
                }
            }
        }
        return null;
    }

    private Point pointForId(int mineId)
    {
        final int x = mineId % width;
        final int y = mineId / width;
        return new Point(x, y);
    }

    private void setupMineButtonListener()
    {
        mineButtonListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Button " + e.getActionCommand() + " pressed");
                int mineId = Integer.parseInt(e.getActionCommand());
                Point point = pointForId(mineId);
                MineInfo info = mineField.checkMine(point);
                JButton button = (JButton) e.getSource();
                if (info.isMine())
                {
                    explodeAllMines();
                } else
                {
                    button.setText(String.valueOf(info.getAdjacentMineCount()));

                    if (info.getAdjacentMineCount() == 0)
                    {
                        exposeAdjacentMineCounts(point);
                    }
                }
                updateRemainingCount();
            }
        };
    }

    private void exposeAdjacentMineCounts(Point point)
    {
        Set<Point> neighbours = mineField.validNeighboursForPoint(point);
        Set<Point> pointsWith0 = new HashSet<>();
        for (Point neighbour : neighbours)
        {
            int count = exposeAndReturnMineCount(neighbour);
            if (count == 0)
            {
                pointsWith0.add(neighbour);
            }
        }

        for (Point aPoint : pointsWith0)
        {
            exposeAdjacentMineCounts(aPoint);
        }
    }

    private int exposeAndReturnMineCount(Point point)
    {
        MineInfo belowInfo = mineField.checkMine(point);
        int requiredActionCommand = (point.getY() * width) + point.getX();
        JButton button = findButtonWithActionCommand(requiredActionCommand);
        int mineCount = belowInfo.getAdjacentMineCount();
        if (!button.getText().equals(" "))
        {
            return -1; //already exposed
        }
        button.setText(String.valueOf(mineCount));
        return mineCount;
    }

    private void explodeAllMines()
    {
        for (Component component : minePanel.getComponents())
        {
            if (component instanceof JButton)
            {
                JButton button = (JButton) component;
                int mineId = Integer.parseInt(button.getActionCommand());
                Point point = pointForId(mineId);
                MineInfo info = mineField.checkMine(point);
                if (info.isMine())
                {
                    button.setText("*");
                }
            }
        }
    }

    private void updateRemainingCount(){
        int remaining = mineField.getMineCount() - minesFlagged;
        minesRemainingLabel.setText("Mines Remaining: " + remaining);
    }

    private void resetButtons()
    {
        minesFlagged = 0;
        mineField = new Minefield(width, height);
        for (Component possibleButton : minePanel.getComponents())
        {
            if (possibleButton instanceof JButton)
            {
                JButton actualButton = (JButton) possibleButton;
                actualButton.setText(" ");
            }
        }
        updateRemainingCount();
    }

    private class RightClickFlagListener implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {

        }

        @Override
        public void mousePressed(MouseEvent e)
        {

        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (e.getButton() != MouseEvent.BUTTON1){
                if (e.getSource() instanceof JButton) {
                    JButton button = ((JButton)e.getSource());
                    if (button.getText().equals(" ")){
                        button.setText("+");
                        minesFlagged++;
                    } else if (button.getText().equals("+")) {
                        button.setText(" ");
                        minesFlagged--;
                    }
                }
                updateRemainingCount();
            }

        }

        @Override
        public void mouseEntered(MouseEvent e)
        {

        }

        @Override
        public void mouseExited(MouseEvent e)
        {

        }
    }


}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MinesweeperUI
{
    private JButton resetButton;
    private JPanel rootPanel;
    private JPanel minePanel;

    private final int width = 7;
    private final int height = 5;

    private Minefield mineField = new Minefield(width, height);

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
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MinesweeperUI");
        frame.setContentPane(new MinesweeperUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private ActionListener mineButtonListener;

    private void createUIComponents()
    {
        GridLayout mineLayout = new GridLayout(height, width);
        minePanel = new JPanel();
        minePanel.setLayout(mineLayout);
        setupMineButtonListener();

        for (int i = 0; i < (width * height); i++)
        {
            JButton mineButton = new JButton(" ");
            mineButton.addActionListener(mineButtonListener);
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
        int x = mineId % width;
        int y = mineId / width;
        return new Point(x, y);
    }

    void setupMineButtonListener()
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
                    button.setText("*");
                } else
                {
                    button.setText(String.valueOf(info.getAdjacentMineCount()));

                    if (info.getAdjacentMineCount() == 0)
                    {
                        exposeAdjacentMineCounts(point);
                    }
                }
            }
        };
    }

    private void exposeAdjacentMineCounts(Point point)
    {
        ArrayList<Point> neighbours = mineField.validNeighboursForPoint(point);
        ArrayList<Point> pointsWith0 = new ArrayList<Point>();
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

    void explodeAllMines()
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

    void resetButtons()
    {
        mineField = new Minefield(width, height);
        for (Component possibleButton : minePanel.getComponents())
        {
            if (possibleButton instanceof JButton)
            {
                JButton actualButton = (JButton) possibleButton;
                actualButton.setText(" ");
            }
        }
    }

}

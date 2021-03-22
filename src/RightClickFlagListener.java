import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RightClickFlagListener implements MouseListener
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
                } else if (button.getText().equals("+")) {
                    button.setText(" ");
                }
            }
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

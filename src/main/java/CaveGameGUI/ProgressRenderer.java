package CaveGameGUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class ProgressRenderer extends JProgressBar implements TableCellRenderer {
    int jpbWidth = 100;
    int jpbHeight = 20;

    public ProgressRenderer(){
        super();
        this.setPreferredSize(new Dimension(jpbWidth, jpbHeight));
    }
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        this.setValue(55);
        return this;
    }


}

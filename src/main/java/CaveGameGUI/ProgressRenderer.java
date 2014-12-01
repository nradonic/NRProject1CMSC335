package CaveGameGUI;

import DataTree.Job;
import DataTree.JobState;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class ProgressRenderer extends JProgressBar implements TableCellRenderer {
    int jpbWidth = 100;
    int jpbHeight = 20;
    Job job;

    public ProgressRenderer(Job job, int row, int column){
        super();
        this.setPreferredSize(new Dimension(jpbWidth, jpbHeight));
        this.job = job;
    }
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        this.setValue(job.getProgress());
        return this;
    }


}

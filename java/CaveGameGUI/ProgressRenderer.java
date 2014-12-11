package CaveGameGUI;

import DataTree.Job;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import static java.lang.Thread.sleep;

/**
 * Created by NickRadonic on 11/29/14.
 */
class ProgressRenderer extends JProgressBar implements TableCellRenderer {
    private final int jpbWidth = 100;
    private final int jpbHeight = 20;
    private final Job job;

    public ProgressRenderer(Job job, int row, int column){
        super();
        this.setPreferredSize(new Dimension(jpbWidth, jpbHeight));
        this.job = job;
    }
    @Override
    public synchronized Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        this.setValue(job.getProgress());
        try{sleep(10);}catch (Exception ex){}finally{};
        return this;
    }
}

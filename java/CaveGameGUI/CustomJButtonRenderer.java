package CaveGameGUI;

import DataTree.Job;
import DataTree.JobState;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by NickRadonic on 11/29/14.
 */
class CustomJButtonRenderer extends JButton implements TableCellRenderer {
    private int row = 0;
    private int column = 0;
    private final Job job;
    private final int JOBSTATECOLUMN = 5;

    public CustomJButtonRenderer(Job job, int row, int column){
        super((column==5) ? job.getJobState().name() : JobState.CANCEL.name());

        this.row = row;
        this.column=column;
        this.job = job;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel jp = new JPanel();
        this.setText((column==JOBSTATECOLUMN) ? job.getJobState().name() : JobState.CANCEL.name());
        jp.add(this);
        table.setRowHeight(30);

        return jp;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }


}

package CaveGameGUI;

import DataTree.Job;
import DataTree.JobState;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class CustomJButtonRenderer extends JButton implements TableCellRenderer {
    int row = 0;
    int column = 0;
    Job job;
    final int JOBSTATECOLUMN = 5;

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

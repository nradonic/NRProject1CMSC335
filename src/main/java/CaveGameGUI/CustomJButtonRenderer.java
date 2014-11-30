package CaveGameGUI;

import DataTree.JobState;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class CustomJButtonRenderer extends JButton implements TableCellRenderer, ActionListener {
    JobState initialJobState = JobState.NEVERSTARTED;
    int row = 0;
    int column = 0;

    public CustomJButtonRenderer(JobState jsInitial, int row, int column){
        super(jsInitial.toString());
        initialJobState = jsInitial;
        this.row = row;
        this.column=column;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (initialJobState == JobState.CANCEL){
            System.out.println("JButton "+initialJobState.toString());
            initialJobState = JobState.CANCELLED;
            this.setText("CANCELLED");
        } else { initialJobState = JobState.CANCEL;
        this.setText("CANCEL");
        }

    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}

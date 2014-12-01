package CaveGameGUI;

import DataTree.Job;
import DataTree.JobState;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

/**
 * Created by NickRadonic on 11/30/14.
 */
public class CustomButtonEditor implements TableCellEditor {
    int row;
    int column ;
    JButton button;
    Job job;

    public CustomButtonEditor(Job job, int row, int column){
        this.row = row;
        this.column  = column;
        this.job = job;
        if(column == 4) {
            button = new JButton(this.job.getJobState().name());
        } else if(column==5){
            button = new JButton(JobState.CANCEL.name());
        }
        button.setOpaque(true);

    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //System.out.printf("CustomButtonEditor:getTableCellEditorComponent row %d column %d\n", row, column);
        return (CustomJButtonRenderer)value;
    }

    @Override
    public Object getCellEditorValue() {

        System.out.println("CustomButtonEditor:getCellEditorValue");
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        //System.out.println("CustomButtonEditor:isCellEditable" + anEvent.toString());
        //if (anEvent. == MouseEvent.MOUSE_CLICKED)
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        //System.out.println("CustomButtonEditor:shouldSelectCell");

        return false;
    }

    @Override
    public boolean stopCellEditing() {
        //System.out.println("CustomButtonEditor:stopCellEditing");

        return true;
    }

    @Override
    public void cancelCellEditing() {
        System.out.println("CustomButtonEditor:cancelCellEditing");
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
//        System.out.println("CustomButtonEditor:addCellEditorListener");
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {

    }
}

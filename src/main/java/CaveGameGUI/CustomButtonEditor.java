package CaveGameGUI;

import DataTree.JobState;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

/**
 * Created by NickRadonic on 11/30/14.
 */
public class CustomButtonEditor implements TableCellEditor , MouseListener {
    int row;
    int column ;

    public CustomButtonEditor(JobState jobState, int row, int column){
        this.row = row;
        this.column  = column;
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        return false;
    }

    @Override
    public void cancelCellEditing() {

    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        System.out.println("cell editor listener interrupt");
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("cell mouse click interrupt");
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

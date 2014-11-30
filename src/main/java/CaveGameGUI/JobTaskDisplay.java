package CaveGameGUI;

import DataTree.Cave;
import DataTree.GameElement;
import DataTree.Job;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class JobTaskDisplay extends JFrame implements TableModel {
    Cave cave;
    Vector<GameElement> geV;
    final int COLUMNCOUNT = 5;

    JobTaskDisplay(Cave cave){
        this.cave = cave;
        geV = cave.getTasks();

        //JTable jt = setupTaskTable();
        JTable jt = new JTable(this);
        JScrollPane jsp = new JScrollPane(jt);
        jsp.setPreferredSize(new Dimension(500, 500));
        jsp.setBorder(BorderFactory.createTitledBorder("Task Status"));

        this.getContentPane().add(jsp);

        this.pack();
        this.setVisible(true);
    }

    private JTable setupTaskTable(){

//        String[][] rowData = {{"zero","one","two","three","four"},
//                {"zero","one","two","three","four"}};

        JTable jt = new JTable();
        if(geV.size()>0) {
            for (GameElement ge : geV) {
                System.out.println(ge.toString());
            }
        }
        return jt;
    }

    @Override
    public int getRowCount() {
        return geV.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNCOUNT;
    }

    @Override
    public String getColumnName(int columnIndex) {

        String[] columnNames = {"Progress",
                "Job",
                "Job ID",
                "Creature ID",
                "Button"};

        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Job job = (Job) geV.elementAt(rowIndex);

        switch (columnIndex){
            case 0: {
                double progress = job.getProgress();
                JProgressBar jpb = new JProgressBar(0, 100);
                jpb.setValue((int)Math.floor(progress*100));
                jpb.setStringPainted(true);
                return jpb;
            }
            case 1: {
                return job.getJobType();
            }
            case 2: {
                return job.getID();
            }
            case 3: {
                return job.getCreatureID();
            }
            default: {
                return new JButton("state");
            }
        }

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        System.out.println(aValue + " row: " + rowIndex + "  columnIndex: "+columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}

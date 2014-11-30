package CaveGameGUI;

import DataTree.Cave;
import DataTree.GameElement;
import DataTree.Job;
import DataTree.JobState;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
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
    final int COLUMNCOUNT = 6;

    JobTaskDisplay(Cave cave){
        this.cave = cave;
        geV = cave.getTasks();

        JTable jt = new JTable(this){
            public TableCellRenderer getCellRenderer(int row, int column){
                switch (column) {
                    case 0: return new ProgressRenderer();
                    case 1:
                    case 2:
                    case 3: return super.getCellRenderer(row, column);
                    case 4: return new CustomJButtonRenderer(JobState.RUN, row, column);
                    default : return new CustomJButtonRenderer(JobState.CANCEL, row, column);
                }
            }
        }; // note this implements TableModel interface
        jt.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                    System.out.println("Interrupt e"+e.toString());
                    int col = e.getColumn();
                    int row = e.getFirstRow();
                    System.out.printf("table changed Row: %d Column: %d\n", row, col);
            }
        });
        JScrollPane jsp = new JScrollPane(jt);
        jsp.setPreferredSize(new Dimension(500, 500));
        jsp.setBorder(BorderFactory.createTitledBorder("Task Status"));

        this.getContentPane().add(jsp);

        this.pack();
        this.setVisible(true);
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
                "Button",
                "Cancel"
        };

        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> klass =  getValueAt(0, columnIndex).getClass();
        return klass;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex==4 || columnIndex == 5){
            return true;
        } else { return false;}
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
            case 4: {
                return new JButton("state");
            }
            default : return new JButton("cancel");
        }

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        System.out.println(aValue + " row: " + rowIndex + "  columnIndex: "+columnIndex);
    }

   @Override
   public void addTableModelListener(TableModelListener l) {
        System.out.println("\nInterrupt l "+l.toString()+"\n");
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }


}

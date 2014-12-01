package CaveGameGUI;

import DataTree.Cave;
import DataTree.GameElement;
import DataTree.Job;
import DataTree.JobState;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class JobTaskDisplay extends JFrame implements TableModel {
    Cave cave;
    Vector<GameElement> geV;
    final int COLUMNCOUNT = 6;

    ArrayList rowOfCells = new ArrayList(7);
    ArrayList columnOfCells = new ArrayList();

    final int PROGRESSBAR = 0;
    final int JOBTYPE = 1;
    final int JOBID = 2;
    final int CREATUREID = 3;
    final int RUNBUTTON = 4;
    final int CANCELBUTTON = 5;
    final int JOB = 6;
    final int THREAD = 7;

    JobTaskDisplay(Cave cave){
        this.cave = cave;
        geV = cave.getTasks();
        createTableModel();

        JTable jt = new JTable(this){
            public TableCellRenderer getCellRenderer(int row, int column){
                return (TableCellRenderer) getValueAt(row, column);
            }
            public TableCellEditor getCellEditor(int row, int column){
                switch (column) {
                    case 0:
                    case 1:
                    case 2:
                    case 3: return super.getCellEditor(row, column);
                    case 4: return new CustomButtonEditor((Job)(geV.get(row)), row, column);
                    case 5: return new CustomButtonEditor((Job)(geV.get(row)), row, column);
                    default : return null;
                }
            }
        }; // note this implements TableModel interface

        jt.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                    System.out.println("Interrupt e"+e.toString());
                    int col = e.getColumn();
                    int row = e.getFirstRow();
                    System.out.printf("\ntable changed Row: %d Column: %d\n", row, col);
            }
        });
        JScrollPane jsp = new JScrollPane(jt);
        jsp.setPreferredSize(new Dimension(700, 500));
        jsp.setBorder(BorderFactory.createTitledBorder("Task Status"));

        this.getContentPane().add(jsp);

        this.pack();
        this.setVisible(true);
    }

    private void createTableModel(){  //crates renderers for each column

        int rowCount = 0;
        for(GameElement jobGE : geV){
            rowOfCells = new ArrayList(6);

            Job job = (Job)jobGE;
            double progress = job.getProgress();
            ProgressRenderer pr = new ProgressRenderer(job, rowCount, PROGRESSBAR );
            pr.setMinimum(0);
            pr.setMaximum(100);
            pr.setValue((int)Math.floor(progress*100));
            pr.setStringPainted(true);
            rowOfCells.add(PROGRESSBAR, pr);

            DefaultTableCellRendererWithData dtcrt = new DefaultTableCellRendererWithData(job, rowCount, JOBTYPE);
            rowOfCells.add(JOBTYPE, dtcrt);

            DefaultTableCellRendererWithData dtcri = new DefaultTableCellRendererWithData(job, rowCount, JOBID);
            rowOfCells.add(JOBID, dtcri);

            DefaultTableCellRendererWithData dtcrc = new DefaultTableCellRendererWithData(job, rowCount, CREATUREID);
            rowOfCells.add(CREATUREID, dtcrc);

            CustomJButtonRenderer cjbrr = new CustomJButtonRenderer(job.getJobState(), rowCount, RUNBUTTON);
            cjbrr.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int buttonRow = ((CustomJButtonRenderer)e.getSource()).getRow();
                    System.out.printf("Mouse Event RUN: row %d\n", buttonRow);
                    startJob(buttonRow);
                }
            });
            rowOfCells.add(RUNBUTTON, cjbrr);

            CustomJButtonRenderer cjbrc = new CustomJButtonRenderer(JobState.CANCEL, rowCount, CANCELBUTTON);
            cjbrc.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int buttonRow = ((CustomJButtonRenderer)e.getSource()).getRow();
                    System.out.printf("Mouse Event CANCEL: row %d\n", buttonRow);
                    cancelJob(buttonRow);
                }
            });
            rowOfCells.add(CANCELBUTTON, cjbrc);

            rowOfCells.add(JOB, job);

            rowOfCells.add(new Thread(job));

            columnOfCells.add(rowOfCells);
            rowCount++;
        }
    }

    public void startJob(int rowIndex){
        rowOfCells = (ArrayList)columnOfCells.get(rowIndex);
        JobState jobState = ((Job) rowOfCells.get(JOB)).getJobState();

        Thread thread;
        if(rowOfCells.get(THREAD) == null || jobState == JobState.CANCELLED ){
            thread = new Thread((Job)rowOfCells.get(JOB));
            rowOfCells.add(THREAD, thread );
        } else {
            thread = (Thread) rowOfCells.get(THREAD);
        }

        if( jobState != JobState.RUNNING && jobState != JobState.FINISHED) {
            thread.start();
        }
    }

    public void pauseJob(int rowIndex){
        rowOfCells = (ArrayList)columnOfCells.get(rowIndex);
        Job job = (Job) rowOfCells.get(JOB);
        Thread thread = (Thread) rowOfCells.get(THREAD);
        job.pause();
        thread.interrupt();
    }

    public void cancelJob(int rowIndex){
        rowOfCells = (ArrayList)columnOfCells.get(rowIndex);
        Job job = (Job) rowOfCells.get(JOB);
        Thread thread = (Thread) rowOfCells.get(THREAD);
        job.cancel();
        thread.interrupt();
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
                "Run/Pause",
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
//        if(columnIndex==4 || columnIndex == 5){
//            return true;
//        } else { return false;}
//        System.out.printf("JobTaskDisplay: iscelleditable Row %d  Column %d\n", rowIndex, columnIndex);
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Job job = (Job) geV.elementAt(rowIndex);
        rowOfCells = (ArrayList)columnOfCells.get(rowIndex);

        switch (columnIndex){
            case 0: {
                ProgressRenderer pr = (ProgressRenderer) rowOfCells.get(columnIndex);
                return pr;
            }
            case 1:
            case 2:
            case 3: {
                DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) rowOfCells.get(columnIndex);
                return dtcr;
            }
            case 4:
            case 5: {
                CustomJButtonRenderer cjbr = (CustomJButtonRenderer) rowOfCells.get(columnIndex);
                return cjbr;
            }
            default : {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        System.out.println(aValue + " setValueAt row: " + rowIndex + "  columnIndex: "+columnIndex);
    }

   @Override
   public void addTableModelListener(TableModelListener l) {
       // System.out.println("\naddTableModelListener Interrupt l "+l.toString()+"\n");
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }


}

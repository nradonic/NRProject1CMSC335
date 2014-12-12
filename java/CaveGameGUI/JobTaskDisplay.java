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
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by NickRadonic on 11/29/14.
 */
class JobTaskDisplay extends JFrame implements TableModel {
    private final Cave cave;
    private final Vector<GameElement> geV;
    private final int COLUMNCOUNT = 7;

    private ArrayList rowOfCells = new ArrayList(COLUMNCOUNT+1);
    private final ArrayList columnOfCells = new ArrayList();

    private final int PROGRESSBAR = 0;
    private final int JOBTYPE = 1;
    private final int JOBID = 2;
    private final int CREATUREID = 3;
    private final int TIME = 4;

    private final int RUNBUTTON = 5;
    private final int CANCELBUTTON = 6;
    private final int JOB = 7;
    private final int THREAD = 8;
    private final HashMap<Integer, Boolean> creatureIDs;
    private ReentrantLock lock = new ReentrantLock();

    JobTaskDisplay(Cave cave){
        this.cave = cave;
        this.setTitle("Sorcerer's Cave Job Display");
        geV = cave.getTasks();
        createTableModel();
        creatureIDs = createCreatureSet(geV);

        JTable jt = new JTable(this){
            public TableCellRenderer getCellRenderer(int row, int column){
                return (TableCellRenderer) getValueAt(row, column);
            }
            public TableCellEditor getCellEditor(int row, int column){
                switch (column) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4: return super.getCellEditor(row, column);
                    case 5: return new CustomButtonEditor((Job)(geV.get(row)), row, column);
                    case 6: return new CustomButtonEditor((Job)(geV.get(row)), row, column);
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
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private  HashMap<Integer, Boolean> createCreatureSet(Vector<GameElement> geV){
        HashMap<Integer, Boolean> creatureIDs = new  HashMap<Integer, Boolean>();
        for(GameElement ge: geV){
            creatureIDs.put(((Job) ge).getCreatureID(), new Boolean(false));
        }
        return creatureIDs;
    }

    private void createTableModel(){  //creates renderers for each column

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

            DefaultTableCellRendererWithData dtcrtime = new DefaultTableCellRendererWithData(job, rowCount, TIME);
            rowOfCells.add(CREATUREID, dtcrtime);

            DefaultTableCellRendererWithData dtcrc = new DefaultTableCellRendererWithData(job, rowCount, CREATUREID);
            rowOfCells.add(CREATUREID, dtcrc);

            CustomJButtonRenderer cjbrr = new CustomJButtonRenderer(job, rowCount, RUNBUTTON);
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

            CustomJButtonRenderer cjbrc = new CustomJButtonRenderer(job, rowCount, CANCELBUTTON);
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

    void startJob(int rowIndex){
        rowOfCells = (ArrayList)columnOfCells.get(rowIndex);
        Job job = (Job) rowOfCells.get(JOB);
        Integer creatureID = job.getCreatureID();
        lock.lock();
        {
            Boolean creatureBusy = creatureIDs.get(creatureID);

            JobState jobState = job.getJobState();

            Thread thread;
            if (rowOfCells.get(THREAD) == null || jobState == JobState.CANCELLED) {
                thread = new Thread((Job) rowOfCells.get(JOB));
                rowOfCells.add(THREAD, thread);
            } else {
                thread = (Thread) rowOfCells.get(THREAD);
            }

            if (jobState != JobState.RUNNING && jobState != JobState.FINISHED) {
                thread.start();
            }
        }
    }

    public void pauseJob(int rowIndex){
        rowOfCells = (ArrayList)columnOfCells.get(rowIndex);
        Job job = (Job) rowOfCells.get(JOB);
        Thread thread = (Thread) rowOfCells.get(THREAD);
        job.pause();
        thread.interrupt();
    }

    void cancelJob(int rowIndex){
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
                "Job Time",
                "Creature ID",
                "Run",
                "Pause/Cancel"
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
            case 3:
            case 4:{
                DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) rowOfCells.get(columnIndex);
                return dtcr;
            }
            case 5:
            case 6: {
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

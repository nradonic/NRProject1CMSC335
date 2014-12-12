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
    private final int CANCELPAUSECOLUMN = 6;

    public CustomJButtonRenderer(Job job, int row, int column){
        super();

        this.row = row;
        this.column=column;
        this.job = job;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel jp = new JPanel();
        this.setText(getLabel(column));

        jp.add(this);
        table.setRowHeight(30);

        return jp;
    }

    private String getLabel(int column) {
        JobState js = job.getJobState();
        if(column == JOBSTATECOLUMN) {
            switch (js) {
                case NEW:
                    return "NEW JOB";
                case RUNNING: {
                    return "RUNNING";
                }
                case PAUSED: {
                    return "RUNNING";
                }
                case CANCELLED: {
                    return "CANCELLED";
                }
                case FINISHED: {
                    return "FINISHED";
                }
                case NEEDSRESOURCES: {
                    return "RESOURCES";
                }
                case BLOCKED: {
                    return "BLOCKED";
                }
                default: {
                    return "";
                }
            }
        } else if (column==CANCELPAUSECOLUMN){
            switch (js) {
                case NEW:
                    return "";
                case RUNNING: {
                    return "PAUSE";
                }
                case PAUSED: {
                    return "CANCEL";
                }
                case CANCELLED: {
                    return "";
                }
                case FINISHED: {
                    return "";
                }
                case NEEDSRESOURCES: {
                    return "CANCEL";
                }
                default: {
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}

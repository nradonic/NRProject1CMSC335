package CaveGameGUI;

import DataTree.Job;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Nick Radonic on 11/30/14.
 */
public class DefaultTableCellRendererWithData extends DefaultTableCellRenderer{
    int row;
    int column;
    Job job;

    public DefaultTableCellRendererWithData(Job job, int row, int column){
        this.row = row;
        this.column = column;
        this.job = job;
    }


    public void setValue(Object value) {
        String displaytext = "";

        switch(column){
            case 1: {
                setText(job.getJobType());
                break;
            }
            case 2: {
                setText(Integer.toString(job.getID()));
                break;
            }
            case 3: {
                setText(String.format("%4.1f",job.getTime()));
                break;
            }
            case 4: {
                setText(Integer.toString(job.getCreatureID()));
                break;
            }
            default: break;
        }
    }
}

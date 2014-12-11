package CaveGameGUI;

import DataTree.Job;
import static java.lang.Thread.sleep;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Created by Nick Radonic on 11/30/14.
 */
class DefaultTableCellRendererWithData extends DefaultTableCellRenderer{
    private final int row;
    private final int column;
    private final Job job;

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
                setText(String.format("%4.1f", job.getTime()));
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

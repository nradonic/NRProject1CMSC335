package CaveGameGUI;

import DataTree.Cave;
import DataTree.GameElement;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by NickRadonic on 11/29/14.
 */
public class JobTaskDisplay extends JFrame {
    Cave cave;
    JobTaskDisplay(Cave cave){
        this.cave = cave;
        JTable jt = setupTaskTable();
        JScrollPane jsp = new JScrollPane(jt);
        jsp.setPreferredSize(new Dimension(500, 500));
        jsp.setBorder(BorderFactory.createTitledBorder("Task Status"));

        this.getContentPane().add(jsp);
    }

    private JTable setupTaskTable(){
        String[] columnNames = {"Progress",
                "Job",
                "Job ID",
                "Creature ID",
                "Button"};
        String[][] rowData = {{"zero","one","two","three","four"},
                {"zero","one","two","three","four"}};
        JTable jt = new JTable(rowData, columnNames);

        Vector<GameElement> geV = cave.getTasks();
        for(GameElement ge : geV){
            System.out.println(ge.toString());
        }

        return jt;
    }

}

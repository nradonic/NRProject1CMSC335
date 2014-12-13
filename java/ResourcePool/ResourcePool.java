package ResourcePool;

import DataTree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by NickRadonic on 12/13/14.
 */
public class ResourcePool {

    private Cave cave;
    private ArrayList<Party> partiesList;

    private Vector<Party> parties = new Vector<>();
    private HashMap<Party, PartyResourcePools> partiesAndPools = new HashMap<>();

    public ResourcePool(Cave cave){
        this.cave = cave;
        makePartiesList();

    }

    private void makePartiesList(){

        parties = cave.getParties();
        for(Party party : parties){
            partiesAndPools.put(party, new PartyResourcePools(party));
        }
    }

    public PartyResourcePools getPartyResourcePool(Job job){
        return partiesAndPools.get(job.getParent().getParent());

    }
}

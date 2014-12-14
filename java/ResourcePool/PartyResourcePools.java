package ResourcePool;

import DataTree.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * Created by NickRadonic on 12/13/14.
 */
public class PartyResourcePools {

    private Party partySelf;
    protected Vector<Artifact> artifactsInParty = new Vector<>();
    protected Vector<Treasure> treasureInParty = new Vector<>();
    private HashMap<String, Integer> artifactInventory = new HashMap<>();
    private HashMap<String, Integer> treasureInventory = new HashMap<>();

    private HashMap<String, Integer> artifactInventoryBusy = new HashMap<>();
    private HashMap<String, Integer> treasureInventoryBusy = new HashMap<>();

    PartyResourcePools(Party party){
        partySelf = party;
        artifactsInParty = party.getArtifacts();
        makeInventoryOfArtifacts();
        treasureInParty = party.getTreasures();
        makeInventoryOfTreasures();
    }

    private void makeInventoryOfArtifacts(){
        artifactInventory = new HashMap<>();
        for(GameElement ge: artifactsInParty){
            String artType = ((Artifact)ge).getArtifactType();
            Integer quantity = 1;
            if(artifactInventory.containsKey(artType)){
                quantity = artifactInventory.get(artType)+1;
            }
            artifactInventory.put(artType, quantity);
        }
    }

    private void makeInventoryOfTreasures(){
        treasureInventory = new HashMap<>();
        for(GameElement ge: treasureInParty){
            String trType = ((Treasure)ge).getTreasureType();
            Integer quantity = 1;
            if(treasureInventory.containsKey(trType)){
                quantity = treasureInventory.get(trType)+1;
            }
            treasureInventory.put(trType, quantity);
        }
    }

    public synchronized Boolean checkResources(HashMap<String, Integer> resourcesFromJob) {
        String nameOfResource;
        Integer quantityOfResourceNeeded;
        Boolean hasResources = true;

        Iterator it = resourcesFromJob.entrySet().iterator();
        HashMap<String, Integer> checkArtifactsInventory = new HashMap<>();
        hasResources = true;
        // move inventory
        while (it.hasNext() && hasResources) {
            Map.Entry pairs = (Map.Entry) it.next();
            nameOfResource = (String) pairs.getKey();
            quantityOfResourceNeeded = (Integer) pairs.getValue();

            Integer quantityInInventory = artifactInventory.get(nameOfResource);
            if (quantityInInventory == null) {
                hasResources = false;
            } else if (quantityOfResourceNeeded > quantityInInventory) {
                hasResources = false;
            } else if(quantityOfResourceNeeded > 0) {
                artifactInventory.put(nameOfResource, quantityInInventory - quantityOfResourceNeeded);
                checkArtifactsInventory.put(nameOfResource, quantityOfResourceNeeded);
            }
        }
        if (!hasResources) {
            // copy back on fail
            it = checkArtifactsInventory.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                nameOfResource = (String) pairs.getKey();
                quantityOfResourceNeeded = (Integer) pairs.getValue();
                artifactInventory.put(nameOfResource, quantityOfResourceNeeded + artifactInventory.get(nameOfResource));
            }
        } else {
            // permanently borrow inventory
            it = checkArtifactsInventory.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                nameOfResource = (String) pairs.getKey();
                quantityOfResourceNeeded = (Integer) pairs.getValue();

                Integer qtyOut = artifactInventoryBusy.get(nameOfResource);
                if (qtyOut == null) {
                    artifactInventoryBusy.put(nameOfResource, quantityOfResourceNeeded);
                } else {
                    artifactInventoryBusy.put(nameOfResource, quantityOfResourceNeeded + qtyOut);
                }
            }
        }
        return hasResources;
    }

    public synchronized void releaseResources(HashMap<String, Integer> resourcesFromJob){
        String nameOfResource;
        Integer quantityOfResourceNeeded;

        Iterator it = resourcesFromJob.entrySet().iterator();
        // move inventory
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            nameOfResource = (String) pairs.getKey();
            quantityOfResourceNeeded = (Integer) pairs.getValue();

            Integer quantityBusy = artifactInventoryBusy.get(nameOfResource);
            if (quantityOfResourceNeeded > 0) {
                Integer leftOverArtifacts = quantityBusy - quantityOfResourceNeeded;
                artifactInventoryBusy.put(nameOfResource, leftOverArtifacts);
                artifactInventory.put(nameOfResource, artifactInventory.get(nameOfResource)+quantityOfResourceNeeded);
            }
        }
    }
};


/*
 * Created on Oct 21, 2006
 */
package cbg.player;

import cbg.common.NoSuchNoteException;
import cbg.ui.CBGDlgFactory;

/**
 * @author Stephen
 */
public class RisingChipModel implements cbg.common.UIConsts {
    private int excessFood, excessAir, excessImp;
    private short[] fo;
    private short[] ao;
    private short[] io;
    
    public RisingChipModel() {
        fo = new short[8];
        ao = new short[6];
        io = new short[4];
    }
    public RisingChipModel(short[] f, short[] a, short[] i) {
        if (f == null) {
	        f = new short[8];
	    }
	    if (a == null) {
	        a = new short[6];
	    }
	    if (i == null) {
	        i = new short[4];
	    }
        fo = f;
        ao = a;
        io = i;
    }
    public short get(short type, short place) {
        if (type == FOOD) {
            return fo[place];
        } else if (type == AIR) {
            return ao[place];
        } else if (type == IMP) {
            return io[place];
        } else {
            System.err.println("RisingChipModel.get("+type+","+place+") invalid.");
            return 0;
        }
    }
    public void set(short type, short place, short value) {
        if (type == FOOD) {
            fo[place]=value;
        } else if (type == AIR) {
            ao[place]=value;
        } else if (type == IMP) {
            io[place]=value;
        }
    }
    public void add(short type, short place) {
        if (type == FOOD) {
            fo[place]++;
        } else if (type == AIR) {
            ao[place]++;
        } else if (type == IMP) {
            io[place]++;
        }
    }
    public boolean hasMoreChips() {
	    int i=0;
        for (i=0; i<4; i++) {
            if (fo[i] > 0) return true;
            if (ao[i] > 0) return true;
            if (io[i] > 0) return true;
        }
        for (i=4; i<6; i++) {
            if (fo[i] > 0) return true;
            if (ao[i] > 0) return true;
        }
        for (i=6; i<8; i++) {
            if (fo[i] > 0) return true;
        }
        return false;
    }
    
    /**
     * Important: the order in this method surely matters.
     * @param fd
     */
    public void advance(FoodDiagram fd) {
        try {
	        // first try to place chip at note. if full, move chip up one spot
	        // C-6
	        if (fo[7]>0) {
	            if (!fd.hasChip(FOOD, 7)) {
	                fd.putChip(FOOD, 7);
	                fo[7]--;
	            }
	            excessFood += fo[7];
	            fo[7]=0;
	        }
	        if (ao[5]>0) {
	            if (!fd.hasChip(AIR, 5)) {
	                fd.putChip(AIR, 5);
	                ao[5]--;
	            }
	            excessAir += ao[5];
	            ao[5]=0;
	        }
	        if (io[3]>0) {
	            if (!fd.hasChip(IMP, 3)) {
	                fd.putChip(IMP, 3);
	                io[3]--;
	            }
	            excessImp += io[3];
	            io[3]=0;    
	        }
	        // C-12
	        while (fo[6]>0) {
	            if (!fd.putChip(FOOD, 6)) {
	    		    CBGDlgFactory.displayMessage("You drew a card by Higher 12.");
	                fd.getPlayer().drawPOCCard(true);
	    		} 
	            fo[6]--;
	        }
	        while (io[2]>0) {
	            if (!fd.putChip(IMP, 2)) {
	                CBGDlgFactory.displayMessage("You drew a card by Higher 12.");
	                fd.getPlayer().drawPOCCard(true);
	            }
	            io[2]--;
	        }
	        if (ao[4]>0) {
	            if (!fd.hasChip(AIR, 4)) {
	                fd.putChip(AIR, 4);
	                ao[4]--;
	            }
	            ao[5]=ao[4];
	            ao[4]=0;
	        }
	        // C-24
	        if (fo[5]>0) {
	            if (!fd.hasChip(FOOD, 5)) {
	                fd.putChip(FOOD, 5);
	                fo[5]--;
	            }
	            if (fd.has6()) {
	                fo[6]=fo[5];
	            } else if (fo[5]>0){
	                CBGDlgFactory.displayMessage("No Hydrogen-6 for food.");
	            }
	            fo[5]=0;
	        }
	        if (io[1]>0) {
	            if (!fd.hasChip(IMP, 1)) {
	                fd.putChip(IMP, 1);
	                io[1]--;
	            }
	            if (fd.has6()) {
	                io[2]=io[1];
	            } else if (io[1]>0) {
	                CBGDlgFactory.displayMessage("No Hydrogen-6 for impression.");
	            }
	            io[1]=0;
	        }
	        if (ao[3]>0) {
	            if (!fd.hasChip(AIR, 3)) {
	                fd.putChip(AIR, 3);
	                ao[3]--;
	            }
	            // don't care about H-6 for air octave
	            ao[4]=ao[3];
	            ao[3]=0;
	        }
	        // C-48
	        if (fo[4]>0) {
	            if (!fd.hasChip(FOOD, 4)) {
	                fd.putChip(FOOD, 4);
	                fo[4]--;
	            }
	            if (fd.has12()) {
	                fo[5]=fo[4];
	            } else if (fo[4]>0){
	                CBGDlgFactory.displayMessage("No Hydrogen-12 for food.");
	            }
	            fo[4]=0;
	        }
	        if (ao[2]>0) {
	            if (!fd.hasChip(AIR, 2)) {
	                fd.putChip(AIR, 2);
	                ao[2]--;
	            }
	            while (ao[2]>0) {
	                if (CBGDlgFactory.giveEWBChoice(fd.getPlayer())) {
	                    ao[3]++;
	                } else {
	                    if (!fd.leaveMI48()) {
	                        CBGDlgFactory.displayInformationMessage(
	                                "Too Much Air",
	                                "Hyper-ventilation - Don't Panic!"
	                        );
	                    }
	                }
	                ao[2]--;
	            }
	        }
	        if (io[0]>0) {
	            if (!fd.hasChip(IMP, 0)) {
	                fd.putChip(IMP, 0);
	                io[0]--;
	            }
	            while (io[0]>0) {
	                if (CBGDlgFactory.giveSelfRemChoice(fd.getPlayer())) {
	                    io[1]++;
	                    if (fd.takeChip(AIR, 2)) {
	                        CBGDlgFactory.displayMessage("Self-remembering shocks air.");
	                        ao[3]++;
	                    }
	                } else {
	                    if (!fd.leaveDO48()) {
	                        CBGDlgFactory.displayInformationMessage(
	                                "Too Many Impressions",
	                                "Pouring from the empty into the void."
	                        );
	                    }
	                }
	                io[0]--;
	            }
	        }
	        // C-96
	        if (ao[1]>0) {
	            if (!fd.hasChip(AIR, 1)) {
	                fd.putChip(AIR, 1);
	                ao[1]--;
	            }
	            if (fd.has24()) {
	                ao[2]=ao[1];
	            } else if (ao[1]>0){
	                CBGDlgFactory.displayMessage("No Hydrogen-24 for air.");
	            }
	            ao[1]=0;
	        }
	        if (fo[3]>0) {
	            if (!fd.hasChip(FOOD, 3)) {
	                fd.putChip(FOOD, 3);
	                fo[3]--;
	            }
	            if (fd.has24()) {
	                fo[4]=fo[3];
	            } else if (fo[3]>0){
	                CBGDlgFactory.displayMessage("No Hydrogen-24 for food.");
	            }
	            fo[3]=0;
	        }
	        // C-192
	        if (fo[2]>0) {
	            if (!fd.hasChip(FOOD, 2)) {
	                fd.putChip(FOOD, 2);
	                fo[2]--;
	            }
	            while (fo[2]>0) {
	                if (CBGDlgFactory.giveBWEChoice(fd.getPlayer())) {
	                    fo[3]++;
	                    fo[2]--;
	                } else {
	                    if (!fd.leaveMI192()) {
	                        CBGDlgFactory.displayInformationMessage(
	                                "Too Much Food",
	                                "BURRRP - Indigestion!"
	                        );
	                    }
	                    fo[2]--;
	                }
	            }
	        }
	        if (ao[0]>0) {
	            if (!fd.hasChip(AIR, 0)) {
	                fd.putChip(AIR, 0);
	                ao[0]--;
	            }
	            if (fd.has48()) {
	                while (ao[0]>0) {
	                    ao[1]++;
	                    ao[0]--;
	                    if (fd.takeChip(FOOD, 2)) {
	                        CBGDlgFactory.displayMessage("Air at RE-96 shocks food.");
	                        fo[3]++;
	                    }
	                }
	            } else if (ao[0]>0){
	                CBGDlgFactory.displayMessage("No Hydrogen-48 for air.");
	            }
	            ao[0]=0;
	        }
	        // C-384
	        if (fo[1]>0) {
	            if (!fd.hasChip(FOOD, 1)) {
	                fd.putChip(FOOD, 1);
	                fo[1]--;
	            }
	            if (fd.has96()) {
	                fo[2]=fo[1];
	            } else if (fo[1]>0) {
	                CBGDlgFactory.displayMessage("No Hydrogen-96 for food.");
	            }
	            fo[1]=0;
	        }
	        // C-768
	        if (fo[0]>0) {
	            if (!fd.hasChip(FOOD, 0)) {
	                fd.putChip(FOOD, 0);
	                fo[0]--;
	            }
	            if (fd.has192()) {
	                fo[1]=fo[0];
	            } else if (fo[0]>0) {
	                CBGDlgFactory.displayMessage("No Hydrogen-192 for food.");
	            }
	            fo[0]=0;
	        }
        } catch (NoSuchNoteException nsc) {
            System.err.println(nsc.getMessage());
            nsc.printStackTrace(System.err);
        }
    }
    /**
     * @param diagram
     */
    public void process(FoodDiagram diagram) {
        while (this.hasMoreChips()) {
	        advance(diagram);
	    }
	    if (this.hasExcess()) {
            System.out.println("handling new pieces");
	        diagram.handleNewPieces(this);
	    }        
    }
    /**
     * @return
     */
    public boolean hasExcess() {
        return (excessFood>0 || excessAir>0 || excessImp>0);
    }
    /**
     * @return
     */
    public int excessFood() {
        return excessFood;
    }
    /**
     * @return
     */
    public int excessAir() {
        return excessAir;
    }
    /**
     * @return
     */
    public int excessImp() {
        return excessImp;
    }
}

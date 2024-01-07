package consultation;

import java.util.ArrayList;

import reparation.Reparation;
import teeth.AbnormalTeeth;

public class Facture {

    Reparation[] reparationsOK ;
    Double initialBudget ;
    Double finalBudget ;


    /**
     * Creation de facture qui va avoir une de reparation faite grace au budget
     * @param abnormalTeeth
     * @param budget
     */
    public Facture(AbnormalTeeth[] abnormalTeeth, Double budget) {
        ArrayList<Reparation > l = new ArrayList<Reparation>();
        initialBudget = budget;
        for (int i = 0; i < abnormalTeeth.length; i++) {
            Reparation[] reparations = abnormalTeeth[i].getAllReparation();

            for (int j = 0; j < reparations.length; j++) {
                if (reparations[j].getPrice()<= budget) {
                    l.add(reparations[j]);
                    budget= budget- reparations[j].getPrice() ;
                }                
                else{
                    break;
                }
            }            

        }
        reparationsOK = l.toArray(new Reparation[l.size()]);
        finalBudget = budget;
    }

}

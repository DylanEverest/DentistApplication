package reparation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import teeth.AbnormalTeeth;
import teeth.Teeth;

public class Reparation {
    Integer reparationid ;
    Teeth  teeth ;
    Integer note ;
    Double price ;
    Reparation nextreparation ;



    public Integer getReparationid() {
        return reparationid;
    }
    public void setReparationid(Integer reparationid) {
        this.reparationid = reparationid;
    }
    public Teeth getTeeth() {
        return teeth;
    }
    public void setTeeth(Teeth teeth) {
        this.teeth = teeth;
    }
    public Integer getNote() {
        return note;
    }
    public void setNote(Integer note) {
        this.note = note;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Reparation getNextreparation() {
        return nextreparation;
    }
    public void setNextreparation(Reparation nextreparation) {
        this.nextreparation = nextreparation;
    }
    /**
     * etablir tous les attributs de la classe reparation pour un dent malade connu
     * @param connection
     * @param abnormalTeeth
     * @param isTransactional
     * @throws Exception
     */
    public void setAllByAbnormalTeeth(Connection connection , AbnormalTeeth abnormalTeeth  , boolean isTransactional) throws Exception{
        setTeeth(abnormalTeeth);
        setNote(abnormalTeeth.getNote());
        try {
            
            PreparedStatement p = connection.prepareStatement("SELECT * FROM v_reparation WHERE teethiD =? AND note =? ");
            p.setInt(1, getTeeth().getTeethiD());
            p.setInt(2,getNote());
            ResultSet res = p.executeQuery();
            if (res.next()) {
                setReparationid(res.getInt("reparationid"));
                setPrice(res.getDouble("price"));

                if (res.getObject("nextreparation")!=null) {
                    AbnormalTeeth newAbnormalTeeth = abnormalTeeth.getClone();
                    newAbnormalTeeth.setNote(res.getInt("childnote"));
                    nextreparation = new Reparation() ;
                    nextreparation.setAllByAbnormalTeeth(connection ,newAbnormalTeeth,true);
                }

            }
            
        } catch (Exception e) {
            throw e ;
        }
        finally{
            if (!isTransactional) {
                connection.close();
            }
        }
    } 

    public boolean hasChild(){
        return nextreparation != null;
    }
}

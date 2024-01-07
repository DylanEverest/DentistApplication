package teeth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import consultation.Consultation;
import fileProcessing.FileProcessing;
import reparation.Reparation;

public class AbnormalTeeth extends Teeth{
    int abnormalTeethId ;
    Consultation consultation ;
    int note ;

    Reparation reparations ;


    // avec images:
    Double area;
    Double positionX;
    Double positionY;
    


    public Double getPositionY() {
        return positionY;
    }
    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }
    public Double getPositionX() {
        return positionX;
    }
    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }
    public Double getArea() {
        return area;
    }
    public void setArea(Double area) {
        this.area = area;
    }
    public Reparation getReparations() {
        return reparations;
    }
    public void setReparations(Reparation reparations) {
        this.reparations = reparations;
    }
    public int getAbnormalTeethId() {
        return abnormalTeethId;
    }
    public void setAbnormalTeethId(int abnormalTeethId) {
        this.abnormalTeethId = abnormalTeethId;
    }
    public Consultation getConsultation() {
        return consultation;
    }
    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }
    public int getNote() {
        return note;
    }
    public void setNote(int note) {
        this.note = note;
    }
    /**
     * Cette fonction a pour objectif de recuperer les dents malades par consultation (consultation = patient+ date de consultation)
     * @param connection
     * @param consultation
     * @param isTransactional
     * @return les dents malades par consultation
     * @throws Exception
     */
    public static AbnormalTeeth[] selectByConsultation(Connection connection, Consultation consultation ,boolean isTransactional) throws Exception {

        try {
            PreparedStatement preparedStatement =  connection.prepareStatement("SELECT * FROM client_teeth_anomaly WHERE consultationid = ?");
            preparedStatement.setInt(1, consultation.getConsultationID());

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<AbnormalTeeth> list = new ArrayList<AbnormalTeeth>();
            while (resultSet.next()) {
                AbnormalTeeth a = new AbnormalTeeth();
                a.setAbnormalTeethId(resultSet.getInt("client_teeth_anomaly_id"));
                a.setConsultation(consultation);
                a.setNote(resultSet.getInt("note"));
                a.setTeethiD(resultSet.getInt("toothiD"));
                a.setReparation(connection , true);
                list.add(a);                                
            }
            return list.toArray(new AbnormalTeeth[list.size()]);
        } catch (Exception e) {
            throw e ;
        }
        finally{
            if (!isTransactional) {
                connection.close();
            }
        }

    }

    public Reparation[] getAllReparation(){
        ArrayList<Reparation> list = new ArrayList<Reparation>();
        list.add(getReparations());
        while (list.get(list.size()-1).hasChild()) {
            list.add(list.get(list.size()-1).getNextreparation());            
        }

        return list.toArray(new Reparation[list.size()]);
    }

    private void setReparation(Connection connection, boolean b) throws Exception {
        reparations = new Reparation() ;
        reparations.setAllByAbnormalTeeth(connection, this ,b);
    }

    public AbnormalTeeth getClone(){
        AbnormalTeeth x = new AbnormalTeeth();
        x.setAbnormalTeethId(getAbnormalTeethId());
        x.setNote(getNote());
        x.setTeethiD(getTeethiD());
        return x ;
    }

    /**
     * Retrieves an array of ToothAnomaly objects representing patient anomalies from a file.
     * connection will be closed if not transactional
     *
     * @param filePath The path to the file containing ToothAnomaly data.
     * @param connection The database connection for associating data with database records.
     * @return An array of ToothAnomaly objects representing patient anomalies.
     * @throws Exception If an error occurs during file reading or database operations.
     */
    public static AbnormalTeeth[] getAbnormalTeeth(String anomaliesFile, Connection connection,
            boolean isTransactional) throws Exception {

        try {
            String txt = FileProcessing.readFromFile(anomaliesFile);

            Iterator<String> lines = txt.lines().iterator();
            ArrayList<AbnormalTeeth> list = new ArrayList<>();
            while (lines.hasNext()) {
                AbnormalTeeth toothAnomaly = new AbnormalTeeth();
                String line = lines.next().replaceAll(" ", "");
                toothAnomaly.setPositionX(Double.parseDouble(line.split(",")[0].substring(1)));
                toothAnomaly.setPositionY(Double.parseDouble(line.split(",")[1].replace(")", "")));
                toothAnomaly.setArea(Double.parseDouble(line.split(",")[2]));
                toothAnomaly.setNoteByArea(connection ,true);
                toothAnomaly.setParentsAttribute(connection,true);
                toothAnomaly.setReparation(connection , true);

                list.add(toothAnomaly);
            }
            if (!isTransactional) {
                connection.close();
            }            
            return list.toArray(new AbnormalTeeth[list.size()]);            
        } catch (Exception e) {
            if (!isTransactional) {
                connection.close();
            }
            throw e;
        }
    }

    /**
     * Etablir la note selon la taille de la fissure du dent malade
     * 
     * @param connection
     * @param isTransaction
     * @throws Exception
     */
    private void setNoteByArea(Connection connection, boolean isTransactional) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(" select note from areaconditionnote where areainf <= ? and ?< areaSup");
        preparedStatement.setDouble(1, getArea());
        preparedStatement.setDouble(2, getArea());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            setNote(resultSet.getInt("note"));
        }
        else{
            throw new Exception("Donnee note correspondant a la superficie non presente");
        }
    }
        /**
     * Sets the parent attributes of the ToothAnomaly based on its position.
     * :les delimitations surtour
     *
     * @param connection The database connection.
     * @throws Exception If an error occurs during database operations.
     */
    public void setParentsAttribute(Connection connection, boolean isTransactional) throws Exception {
        Teeth[] teeth = select(connection, isTransactional);

        for (int i = 0; i < teeth.length; i++) {
            if (teeth[i].getLeftlimit() <= positionX && positionX <= teeth[i].getRightlimit()
                    && teeth[i].getToplimit() <= positionY && positionY <= teeth[i].getBottomlimit()) {
                setTeethiD(teeth[i].getTeethiD());
                setToplimit(teeth[i].getToplimit());
                setBottomlimit(teeth[i].getBottomlimit());
                setLeftlimit(teeth[i].getLeftlimit());
                setRightlimit(teeth[i].getRightlimit());
                return;
            }
        }
    }

}

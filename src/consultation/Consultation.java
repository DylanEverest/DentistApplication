package consultation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ImageProcessing.PythonCaller;
import patient.Patients;
import teeth.AbnormalTeeth;

public class Consultation {
    
    int consultationID ;
    Patients patients ;
    Double budget ;
    Date consultationDate ;
    AbnormalTeeth[] abnormalTeeth ;


    /**
     * On n'utilise cet constructeur que si les informations des dents malades du patient sont deja dans la base
     * 
     * Constructeur de consultation qui va recuperer from database les elements suivants:
     *  . les informations du patient (patient ID)
     *  . les dents malades du patient (abnormalTeeth) 
     * @param connection
     * @param isTransactional
     * @param patientsID : pour dire qui est le patient
     * @param consultationDate : pour dire quelle date de consultation on fait reference
     * @throws Exception
     */
    public Consultation(Connection connection ,boolean isTransactional,String patientsID ,Date consultationDate) throws Exception{
        try {
            setPatients(patientsID);
            setConsultationDate(consultationDate);
            setBudget(connection ,true);
            setConsultationID(connection, true);
            setAbnormalTeeth(connection ,true);
            
        }
        finally{
            if (!isTransactional) {
                connection.close();
            }
        }
    }

        /**
     * Constructor for Consultation class.
     *
     * @param connection The database connection. 
     * @param budget The budget for the consultation.
     * @param name The name of the patient.
     * @param imgFileOfPatient The file path of the patient's image.
     * @param isTransaction : true if you want to close manually the connection 
     * @throws Exception If an error occurs during database operations or Python script execution.
     */
    public Consultation(Connection connection, Double budget, String name ,Date dateConsultation , String imgFileOfPatient ,
                String scriptPython ,String anomaliesFile, boolean isTransactional) throws Exception {
        try{
            setPatients(name);
            setConsultationDate(dateConsultation);
            setBudget(budget);
            PythonCaller.call(scriptPython, imgFileOfPatient, anomaliesFile);
            setAbnormalTeeth(anomaliesFile, connection ,true);
        }finally{
            if (!isTransactional) {
                connection.close();
            }
        }
    }

    private void setAbnormalTeeth(String anomaliesFile, Connection connection, boolean isTransactional) throws Exception {
        abnormalTeeth = AbnormalTeeth.getAbnormalTeeth(anomaliesFile,connection, isTransactional);       
    }

    private void setBudget(Connection connection, boolean isTransactional) throws Exception{
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT budget FROM CONSULTATION WHERE DATECONSULTATION = ? AND patientsId =?");
            preparedStatement.setDate(1, getConsultationDate());
            preparedStatement.setString(2, getPatients().getPatientID());                
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                setBudget(res.getDouble("budget"));            
            }
            else{
                throw new Exception("Ce patient n'a pas de consultation pour cette date donnee");
            }            
        } 
        finally{
            if (!isTransactional) {
                connection.close();
            }
        }
    }

    /**
     * Cette fonction a pour role d'avoir la facture si on choisit comme priorite beaute
     * @return facture correspondant a l'option beaute comme priorite
     */
    public Facture getFactureByEsthetic(){
        // trier par teethID desc 
        AbnormalTeeth.sortDESC(abnormalTeeth, new String []{"teethiD"});
        return new Facture(abnormalTeeth,getBudget());
    }

    /**
     * Cette fonction a pour role d'avoir la facture si on choisit comme priorite sante
     * @return facture correspondant a l'option sante comme priorite
     */
    public Facture getFactureByHealthPriority(){
        // trier par teethID ASC
        AbnormalTeeth.sort(abnormalTeeth, new String[]{"teethiD"});
        return new Facture(abnormalTeeth ,getBudget());
    }


    /**
     * setConsultationID est une fonction qui va etablir la consultationID selon la date de consultation et le patientID
     * @param connection
     * @param isTransactional
     * @throws Exception
     */
    private void setConsultationID(Connection connection , boolean isTransactional) throws Exception {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT CONSULTATIONID FROM CONSULTATION WHERE DATECONSULTATION = ? AND patientsId =?");
            preparedStatement.setDate(1, getConsultationDate());
            preparedStatement.setString(2, getPatients().getPatientID());                
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                setConsultationID(res.getInt("CONSULTATIONID"));            
            }
            else{
                throw new Exception("Ce patient n'a pas de consultation pour cette date donnee");
            }            
        } 
        finally{
            if (!isTransactional) {
                connection.close();
            }
        }

    }


    /**
     * Pour etablir abnormalTeeth selon la date de consultation 
     * @param connection
     * @param isTransactional
     * @throws Exception
     */
    private void setAbnormalTeeth(Connection connection, boolean isTransactional) throws Exception {
        try {
            abnormalTeeth= AbnormalTeeth.selectByConsultation(connection, this ,isTransactional);
        }
        finally{
            if (!isTransactional) {
                connection.close();
            }
        }
    }


    private void setPatients(String patientsID) {
        patients = new Patients();
        patients.setPatientID(patientsID);
    }


    public AbnormalTeeth[] getAbnormalTeeth() {
        return abnormalTeeth;
    }
    public void setAbnormalTeeth(AbnormalTeeth[] abnormalTeeth) {
        this.abnormalTeeth = abnormalTeeth;
    }
    public int getConsultationID() {
        return consultationID;
    }
    public void setConsultationID(int consultationID) {
        this.consultationID = consultationID;
    }
    public Patients getPatients() {
        return patients;
    }
    public void setPatients(Patients patients) {
        this.patients = patients;
    }
    public Double getBudget() {
        return budget;
    }
    public void setBudget(Double budget) {
        this.budget = budget;
    }
    public Date getConsultationDate() {
        return consultationDate;
    }
    public void setConsultationDate(Date consultationDate) {
        this.consultationDate = consultationDate;
    }
}

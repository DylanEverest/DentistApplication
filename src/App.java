import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import consultation.Consultation;
import consultation.Facture;

public class App {
    public static void main(String[] args) throws Exception {
     Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dentistapp", "postgres", "post");         
     avecImage(connection);
    }

    public static void avecImage(Connection connection ) throws SQLException{
        try {
            Consultation c=  new Consultation(connection ,1000.,"P001",Date.valueOf("2024-01-07"),"C:/Users/ratia/Desktop/workspace/S5/pythonTraining/workspace/imgProcessing/Img-Client/2.png",
            "C:/Users/ratia/Desktop/workspace/S5/pythonTraining/workspace/imgProcessing/dental/DentalApplication/src/ImageProcessing/Detector.py",
            "C:/Users/ratia/Desktop/workspace/S5/pythonTraining/workspace/imgProcessing/dental/DentalApplication/bin/Output.txt",true );

            Facture f= c.getFactureByEsthetic() ;
            
        } catch (Exception e) {
            System.out.println(e);
        }
        finally{
            connection.close();
        }
    }

    public static void sansImage(Connection connection) throws SQLException{
        try {
            Consultation c=  new Consultation(connection, true, "P001", Date.valueOf("2024-01-07"));
            Facture f= c.getFactureByEsthetic() ;
            Facture f2 = c.getFactureByHealthPriority();

        } catch (Exception e) {
            System.out.println(e);
        }        
        finally{
            connection.close();
            
        }
    }
}

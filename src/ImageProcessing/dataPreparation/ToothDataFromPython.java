package ImageProcessing.dataPreparation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import fileProcessing.FileProcessing;
import teeth.Teeth;

public class ToothDataFromPython  {

    public void insertDatas(Connection connection , String dataPath , int [] indexes) throws SQLException{
         
        String textForTreatment = FileProcessing.readFromFile(dataPath);
        ArrayList<Teeth> teeth = getTeethFromData(textForTreatment);

        connection.setAutoCommit(false);
        for (int i = 0; i < teeth.size(); i++) {
            try {
                teeth.get(i).setTeethiD(indexes[i]);
                teeth.get(i).insert(connection,true);
            } catch (Exception e) {
                connection.rollback();
                connection.close();;
                throw e;
            }
        }
        connection.commit();
        connection.close();

    }
    
    private ArrayList<Teeth> getTeethFromData(String textForTreatment) {
        Iterator<String> lines = textForTreatment.lines().iterator();
        ArrayList<Teeth> teeth = new ArrayList<Teeth>();
        while (lines.hasNext()) {
            String line = lines.next();
            if (line.contains("Gauche")) {
                Teeth t = new Teeth();
                teeth.add(t);
                t.setLeftlimit(Integer.parseInt(line.split(":")[1].replaceAll(" ","")));
            }
            else if(line.contains("Droite")) {
                teeth.get(teeth.size()-1).setRightlimit(Integer.parseInt(line.split(":")[1].replaceAll(" ","")));
            }
            else if(line.contains("Haut")) {
                teeth.get(teeth.size()-1).setToplimit(Integer.parseInt(line.split(":")[1].replaceAll(" ","")));
            }
            else if(line.contains("Bas")) {
                teeth.get(teeth.size()-1).setBottomlimit(Integer.parseInt(line.split(":")[1].replaceAll(" ","")));
            }
            
        }

        return teeth;
    }
        

    public static void main(String[] args) throws Exception{
        haut();
    }

    public static void haut() throws Exception{
        Connection c = null;
        try {
            c=  DriverManager.getConnection("jdbc:postgresql://localhost:5432/dentistapp", "postgres", "post") ;
            new ToothDataFromPython().insertDatas(c, "C:\\Users\\ratia\\Desktop\\workspace\\S5\\pythonTraining\\workspace\\imgProcessing\\dental\\Datasets\\HautFinal", 
            new int[]{1,5,9,13,17,21,25,29,31,27,23,19,15,11,7,3});            
        } catch (Exception e) {
            System.out.println(e);
        }
        finally{
            c.close();
        }
    }

    public static void bas() throws Exception{
        Connection c = null;
        try {
            c=  DriverManager.getConnection("jdbc:postgresql://localhost:5432/dentistapp", "postgres", "post") ;
            new ToothDataFromPython().insertDatas(c, "C:\\Users\\ratia\\Desktop\\workspace\\S5\\pythonTraining\\workspace\\imgProcessing\\dental\\Datasets\\BasFinal", 
            new int[]{2,6,10,14,18,22,26,30,32,28,24,20,16,12,8,4});            
        } catch (Exception e) {
            System.out.println(e);
        }
        finally{
            c.close();
        }
    }
}
package ImageProcessing;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonCaller {

    public static void call(String pythonScriptPath, String ... args) throws Exception {
        String [] command = new String[args.length+2];
        command[0]="python";
        command[1]= pythonScriptPath;
        for (int i = 2; i < command.length; i++) {
            command[i]=args[i-2];
        }
        
        // Créer un processus pour exécuter la commande
        ProcessBuilder processBuilder = new ProcessBuilder(command);
    
        // Rediriger la sortie standard du processus vers un flux
        processBuilder.redirectErrorStream(true);
    
        // Démarrer le processus
        Process process = processBuilder.start();
    
        // Lire la sortie du processus
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line); // Afficher la sortie du script Python
        }
    
        // Attendre que le processus se termine
        int exitCode = process.waitFor();
        System.out.println("Le script Python s'est terminé avec le code de sortie : " + exitCode);
    }
    

    public static void main(String[] args) throws Exception {
        // call("C:\\Users\\ratia\\Desktop\\workspace\\S5\\pythonTraining\\workspace\\imgProcessing\\dental\\ImageProcessing\\Detector.py");
    }



}

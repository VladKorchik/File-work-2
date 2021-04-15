import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    static final String savesPath = "H:\\Games";
    static protected String zipPath = "H:\\Games\\zip.zip";
    static protected int count = 0;//is used for creating filename in saveGame()
    static protected ArrayList<String> savesList = new ArrayList<>();

    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(80,3,4, 4.35);
        GameProgress gameProgress2 = new GameProgress(100, 5, 10, 0.0000);
        GameProgress gameProgress3 = new GameProgress(50,5,3,8.46);
        saveGame(savesPath, gameProgress1);
        saveGame(savesPath, gameProgress2);
        saveGame(savesPath, gameProgress3);
        zipFiles(zipPath,savesList);
        deleteSaves(savesList);
    }

    public static void saveGame (String path, GameProgress gameProgress) {
        //file name creation
        String countStr = Integer.toString(count);
        String savesFileName = "save" + countStr +".dat";
        //create dir and file of save
        File gamesDir = new File(path);
        if (gamesDir.mkdir())
            System.out.println("Directory " + gamesDir.getName() + " was created\n");
        File saveDat = new File(path, savesFileName);
        try {
            if (saveDat.createNewFile())
                System.out.println("File " + saveDat.getName() + " was created\n");
        }   catch (IOException ex) {
            System.out.println("Error while creating file or other\n");
        }
        //write savings
        try (FileOutputStream fos = new FileOutputStream(saveDat,true);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println( gameProgress.toString() + " successfully saved");
        } catch (Exception ex) {
            System.out.println("Warning! Game has not beed saved");
        }
        count++;
        savesList.add(path + "\\" + savesFileName);
        System.out.println(path + "\\" + savesFileName + " was created");
    }

    public static void zipFiles(String zipPath, ArrayList<String> arrayList) {
        for (String filePath : arrayList) {
            try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath));
                FileInputStream fis = new FileInputStream(filePath)) {
                ZipEntry entry = new ZipEntry("Packed_saves.dat");
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                System.out.println("Files have been packed");
            }   catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void deleteSaves (ArrayList<String> arrayList) {
        for (String str : arrayList) {
            File file = new File(str);
            if (file.delete()) {
                System.out.println("Old saves has been deleted");
            }
        }
    }
}

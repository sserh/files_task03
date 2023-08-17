import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    static String saveDirectory = "C:/SuperGame/savegames/";

    public static void main(String[] args) {


        GameProgress gameProgress_1 = new GameProgress(95, 22, 1, 111);
        GameProgress gameProgress_2 = new GameProgress(83, 104, 2, 121);
        GameProgress gameProgress_3 = new GameProgress(3, 7, 4, 360);

        //сохраняем прогресс несколько раз
        saveGame("File_1", gameProgress_1);
        saveGame("File_2", gameProgress_2);
        saveGame("File_3", gameProgress_3);

        //список dat-файлов в директории сохранения
        List<String> datFileList = Arrays.asList(Objects.requireNonNull(new File(saveDirectory).list(new MyFileNameFilter(".dat"))));

        //вызываем архивирование всех сохранённых файлов согласно списку выше
        zipSaves("arc.zip", datFileList);

        //удаляем файлы сохранений
        deleteSaves(datFileList);

        //разархивируем файлы сохранений
        openZip("arc.zip", saveDirectory);

        //выведем в консоль
        System.out.println(openProgress("File_2"));
    }

    public static void saveGame(String saveFileName, GameProgress gameProgress) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(saveDirectory + saveFileName + ".dat")) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String saveFileNAme) {

        GameProgress gameProgress = null;

        try (FileInputStream fileInputStream = new FileInputStream(saveDirectory + saveFileNAme + ".dat");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                 gameProgress = (GameProgress) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }

        public static void zipSaves(String zipFileName, List<String> savesList) {

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(saveDirectory + zipFileName))) {
            for (String save :
                    savesList) {
                try (FileInputStream fileInputStream = new FileInputStream(saveDirectory + save)) {
                    ZipEntry entry = new ZipEntry(save);
                    zipOutputStream.putNextEntry(entry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String unzipFileName, String unzipFolder) {

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(unzipFolder + unzipFileName))) {
            ZipEntry entry;
            String name;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(unzipFolder + name);
                for (int i = zipInputStream.read(); i != -1; i = zipInputStream.read()) {
                    fileOutputStream.write(i);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteSaves(List<String> savesList) {

        for (String save :
                savesList) {
            new File(saveDirectory + save).delete();
        }
    }
}
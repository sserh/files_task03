import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
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
    }

    public static void saveGame(String saveFileName, GameProgress gameProgress) {


        try (FileOutputStream fileOutputStream = new FileOutputStream(saveDirectory + saveFileName + ".dat")) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public static void deleteSaves(List<String> savesList) {

        for (String save :
                savesList) {
            new File(saveDirectory + save).delete();
        }
    }
}
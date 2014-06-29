package com.tools.songs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Radha Krishnan
 * 
 */
public class PlaylistCreator {

    private static int CDCOVER_IN_HTML = 0;

    private static Set<String> EXCLUDED_FORMATS_LIST = null;

    private static Map<String, List> folderMap = new HashMap<String, List>();

    private static String MUSIC_PATH = "NO_PATH_FOUND";

    private static int OUTPUT_FORMAT = 2;

    private static boolean SUPPORT_ALL = false;

    private static Set<String> SUPPORTED_FORMATS_LIST = null;

    static {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("PlaylistCreator");

            MUSIC_PATH = rb.getString("MUSIC_LIBRARY_LOCATION");

            SUPPORTED_FORMATS_LIST = getAllFormats(rb
                    .getString("SUPPORTED_FORMATS"));
            EXCLUDED_FORMATS_LIST = getAllFormats(rb
                    .getString("EXCLUDE_FORMATS"));

            if ((SUPPORTED_FORMATS_LIST.isEmpty() || SUPPORTED_FORMATS_LIST
                    .contains("*"))
                    && EXCLUDED_FORMATS_LIST.isEmpty()) {
                SUPPORT_ALL = true;
            }

            OUTPUT_FORMAT = Integer.parseInt(rb.getString("OUTPUT_FORMAT"));

            CDCOVER_IN_HTML = Integer.parseInt(rb.getString("CDCOVER_IN_HTML"));

        } catch (Exception e) {
            MUSIC_PATH = "NO_PATH_FOUND";
            OUTPUT_FORMAT = 1;
        }
    }

    public static void main(String[] args) {
        MUSIC_PATH = getLibraryLocation(args);
        File root = new File(MUSIC_PATH);

        try {

            getList(root);

            GenerateOutput();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // sop purpose
        //display(folderMap);
    }

    private static boolean containFiles(File root) {
        File[] fs = root.listFiles();
        for (File file : fs) {
            String fileName = file.getName();
            if (file.isFile()
                    && isSupportedFormat(fileName
                            .substring(fileName.length() - 3))) {
                return true;
            }
        }
        return false;
    }

    /**
     * this function is just used to display the object contents
     * @param obj
     */
    private static void viewObject(Object obj) {
        if (obj instanceof HashMap) {
            HashMap<String, ArrayList> hmap = (HashMap) obj;
            for (String key : hmap.keySet()) {
                System.out.println("folder==>" + key);
                for (String str : (ArrayList<String>) hmap.get(key)) {
                    System.out.println(str);
                }
            }
        }
    }

    /**
     * this function is just used to display the object contents
     * @param obj
     */
    private static void display() {
        for (String key : folderMap.keySet()) {
            System.out.println("folder==>" + key);
            for (String str : (ArrayList<String>) folderMap.get(key)) {
                System.out.println(str);
            }
        }
    }

    /**
     * this function is just used to display the object contents
     * @param obj
     */
    private static void displayArray(String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + "\t\t");
            }
            System.out.println();
        }
    }

    /**
     * Depending on configuration, data is written to each folder, a single list, or displayed on screen
     * 1 - Display a single list on screen in CD COver format
     * 2 - Generate a single list in CD COver format
     * 3 - Generate playlists in individual folders
     * 4 - Generate a single list in directory listing format
     * 
     * @throws IOException
     */
    private static void GenerateOutput() throws IOException {
        switch (OUTPUT_FORMAT) {
        case 1:
            // Display a single list on screen in CD COver format
            display();
            break;
        case 2:
            // Generate a single list in CD COver format
            writeCDCover();
            break;
        case 3:
            // Generate playlists in individual folders
            generatePlaylists();
            break;
        case 4:
            // Generate a single list in directory listing format
            writeDirListing();
            break;
        default:
            // Display a single list on screen in CD COver format
            display();
        }

    }

    /**
     * Depending on the parameter, decided if the data is to written to each folder, a single list
     * @throws IOException
     */
    public static void generatePlaylists() throws IOException {
        FileOutputStream outFile = null;
        for (String absoluteFolderName : folderMap.keySet()) {
            outFile = new FileOutputStream(
                    new File(absoluteFolderName + ".m3u"));
            for (String fileName : (ArrayList<String>) folderMap
                    .get(absoluteFolderName)) {
                writeToFile(fileName, outFile);
            }
            if (outFile != null) {
                outFile.close();
            }
        }
    }

    private static String[][] get2DArray() {
        int totalElements = getElementSize();

        int columnSize = 3;
        int rowSize = totalElements / columnSize;
        if (totalElements % columnSize != 0) {
            rowSize += 1;
        }

        String arr[][] = new String[rowSize][columnSize];
        int row = 0;
        int col = 0;

        for (String absoluteFolderName : folderMap.keySet()) {
            String folderName = absoluteFolderName.substring(absoluteFolderName
                    .lastIndexOf("\\") + 1);

            arr[row++][col] = "<b>"+folderName+"</b>";
            if (row == rowSize) {
                row = 0;
                col++;
            }
            for (String fileName : (ArrayList<String>) folderMap
                    .get(absoluteFolderName)) {
                arr[row++][col] = fileName;

                if (row == rowSize) {
                    row = 0;
                    col++;
                }
            }
        }
        //displayArray(arr);
        return arr;
    }

    private static Set<String> getAllFormats(String supportedFormats) {
        if (supportedFormats.trim().length() == 0) {
            return new HashSet<String>(0);
        }
        String[] arr = supportedFormats.split(",");
        Set<String> allSupportedFormats = new HashSet<String>(arr.length);
        //allSupportedFormats.addAll(arr);
        for (String str : arr) {
            allSupportedFormats.add(str);
        }
        return allSupportedFormats;
    }

    private static int getElementSize() {

        int totalElements = folderMap.size();
        for (String absoluteFolderName : folderMap.keySet()) {
            totalElements += folderMap.get(absoluteFolderName).size();
        }
        return totalElements;
    }

    private static String getLibraryLocation(String[] args) {
        if (args.length < 1) {
            if ("NO_PATH_FOUND".equals(MUSIC_PATH)) {
                System.out.println("Please specify the music library path");
                System.out
                        .println("format: javac PlaylistCreator <music_library_path>");
                System.out
                        .println("eg: java PlaylistCreator D:\\Music\\English");
                System.exit(0);
            }
        }
        return MUSIC_PATH;
    }

    public static void getList(File root) throws IOException {
        String absoluteFolderName = null;
        List<String> filelist = null;
        if (root.isDirectory()) {
            if (SUPPORT_ALL || containFiles(root)) {
                absoluteFolderName = root.getAbsolutePath() + "\\"
                        + root.getName();
                filelist = new ArrayList<String>();
                folderMap.put(absoluteFolderName, filelist);
            }
            File[] fs = root.listFiles();
            for (File f : fs) {
                if (f.isDirectory()) {
                    getList(f);
                } //node is a directory, getinside and generate a new list of files
                else {
                    String fileName = f.getName();
                    if (SUPPORT_ALL
                            || isSupportedFormat(fileName.substring(fileName
                                    .length() - 3))) {
                        filelist.add(f.getName());
                    } //add the list of files
                }
            }
        }
    }

    private static boolean isSupportedFormat(String str) {
        return (SUPPORTED_FORMATS_LIST.isEmpty() || SUPPORTED_FORMATS_LIST
                .contains(str))
                && !EXCLUDED_FORMATS_LIST.contains(str);
    }

    /**
     * Depending on the parameter, decided if the data is to written to each folder, a single list
     * @throws IOException
     */
    public static void writeCDCover() throws IOException {

        if (CDCOVER_IN_HTML > 0) {
            writeCDCoverInHTML();
            return;
        }
        FileOutputStream outFile = new FileOutputStream(new File("cdcover.txt"));

        for (String absoluteFolderName : folderMap.keySet()) {
            String folderName = absoluteFolderName.substring(absoluteFolderName
                    .lastIndexOf("\\") + 1);

            writeToFile(folderName, outFile);
            for (String fileName : (ArrayList<String>) folderMap
                    .get(absoluteFolderName)) {
                writeToFile(fileName, outFile);
            }
        }

        if (outFile != null) {
            outFile.close();
        }
    }

    private static void writeCDCoverInHTML() throws IOException {

        String[][] arr = get2DArray();

        FileOutputStream outFile = new FileOutputStream(new File("cdcover.htm"));

        writeToFile(
                "<html><head><title>CD Cover generated by ...</title></head><body><font size=\"1\">",
                outFile);
        writeToFile("<table style=\"border-collapse:collapse\">", outFile);

        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            strb.append("<tr>");
            for (int j = 0; j < arr[i].length; j++) {
                strb.append("<td>").append("<font size=\"1\">").append(
                        arr[i][j]).append("</font>").append("</td>");
            }
            strb.append("</tr>");

            writeToFile(strb.toString(), outFile);
            strb.delete(0, strb.length());
        }

        writeToFile("</table>", outFile);
        writeToFile("</font></body></html>", outFile);

        if (outFile != null) {
            outFile.close();
        }
    }

    /**
     * Depending on the parameter, decided if the data is to written to each folder, a single list
     * @throws IOException
     */
    public static void writeDirListing() throws IOException {
        FileOutputStream outFile = new FileOutputStream(new File(
                "dirlisting.txt"));

        for (String absoluteFolderName : folderMap.keySet()) {
            writeToFile(absoluteFolderName, outFile);
            for (String fileName : (ArrayList<String>) folderMap
                    .get(absoluteFolderName)) {
                writeToFile(fileName, outFile);
            }
        }

        if (outFile != null) {
            outFile.close();
        }
    }

    private static void writeToFile(String str, FileOutputStream outFile)
            throws IOException {
        outFile.write(str.getBytes());
        outFile.write("\n".getBytes());
    }

}

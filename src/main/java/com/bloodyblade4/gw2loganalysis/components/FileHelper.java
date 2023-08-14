package com.bloodyblade4.gw2loganalysis.components;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * Contains class's/functions to assist in parsing/reading files.
 */
public class FileHelper {
    static final Logger logger = Logger.getLogger(FileHelper.class);

    //Given a directory and the file extension you're looking for, will return the number of those types of files in the directory.
    public static String getFileCount(File directoryFile, String suffix) {
        var total = Arrays.asList(directoryFile.list())
                .stream()
                .filter(x -> x.contains(suffix))
                .collect(Collectors.counting());
        return total.toString();
    }

    public static FileFilter getFileFilterByExtension(String extension) {
        return new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(extension);
            }
        };
    }

    public static FileFilter getFileFilterExcludeExtension(String extension) {
        return new FileFilter() {
            public boolean accept(File f) {
                return !f.getName().endsWith(extension);
            }
        };
    }

    public static void deleteAllInDirectory(File directory) {
        for (File f : directory.listFiles()) {
            try {
                f.delete();
            } catch (Exception e) {
                logger.warn("Unable to delete a file in directory \"" + directory.getName() + "\". " + e);
            }

        }
    }

    public static void deleteAllInDirectory(File directory, FileFilter filter) {
        for (File f : directory.listFiles(filter)) {
            try {
                f.delete();
            } catch (Exception e) {
                logger.warn("Unable to delete a file in directory \"" + directory.getName() + "\". " + e);
            }
        }
    }


    public static File selectFile(String openingDirectory, String title, String extDetail, String extName) {
        File directory = (openingDirectory == null) ? new File(System.getProperty("user.dir"))
                : new File(openingDirectory);
        JFileChooser chooser = new JFileChooser(directory);
        chooser.setDialogTitle(title);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(extDetail, extName));
        chooser.setAcceptAllFileFilterUsed(true);


        int option = chooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        return null;
    }

    public static String selectDirectory(String extDetail, String extName) {
        File directory = new File(System.getProperty("user.dir"));
        JFileChooser chooser = new JFileChooser(directory) {
            private static final long serialVersionUID = 1L;

            public void approveSelection() {
                if (getSelectedFile().isFile()) {
                    String name = getSelectedFile().getAbsolutePath();
                    int p = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
                    super.setSelectedFile(new File(name.substring(0, p)));
                }
                super.approveSelection();
            }
        };
        chooser.setDialogTitle("Select a directory.");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (extDetail != null && extName != null)
            chooser.setFileFilter(new FileNameExtensionFilter(extDetail, extName));
        int option = chooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            return path;
        }
        return null;
    }
}

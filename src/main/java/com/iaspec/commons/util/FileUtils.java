package com.pt.commons.util;


import org.apache.commons.io.filefilter.*;

import java.io.*;

/**
 * Class description goes here.
 * User: Peter
 *
 * @author <a href="mailto:yang.li@iaspec.com">Peter</a>
 * @version 1.00 13-5-29
 */
public final class FileUtils
{
    /**
     * Copy file or directory.
     *
     * @param resFilePath resource file path
     * @param distFolder  dist folder path
     * @IOException
     */
    public static void copyFile(String resFilePath, String distFolder) throws IOException
    {
        File resFile = new File(resFilePath);
        File distFile = new File(distFolder);
        if (resFile.isDirectory()) {
            org.apache.commons.io.FileUtils.copyDirectoryToDirectory(resFile, distFile);
        }
        else if (resFile.isFile()) {
            org.apache.commons.io.FileUtils.copyFileToDirectory(resFile, distFile, true);
        }
    }

    /**
     * Delete file or directory.
     *
     * @param targetPath the path of target file/directory.
     * @IOException
     */
    public static void deleteFile(String targetPath) throws IOException
    {
        File targetFile = new File(targetPath);
        if (targetFile.isDirectory()) {
            org.apache.commons.io.FileUtils.deleteDirectory(targetFile);
        }
        else if (targetFile.isFile()) {
            targetFile.delete();
        }
    }

    /**
     * move file or directory, create new one if the target directory is not exist.
     *
     * @param resFilePath resource file path
     * @param distFolder  target directory path
     * @IOException
     */
    public static void moveFile(String resFilePath, String distFolder) throws IOException
    {
        File resFile = new File(resFilePath);
        File distFile = new File(distFolder);
        if (resFile.isDirectory()) {
            org.apache.commons.io.FileUtils.moveDirectoryToDirectory(resFile, distFile, true);
        }
        else if (resFile.isFile()) {
            org.apache.commons.io.FileUtils.moveFileToDirectory(resFile, distFile, true);
        }
    }


    /**
     * Rename file or directory.
     *
     * @param resFilePath the resource file path
     * @param newFileName the new file name
     * @return true if success
     */
    public static boolean renameFile(String resFilePath, String newFileName)
    {
        String newFilePath = new File(resFilePath).getParent() + File.separator + newFileName;
        File resFile = new File(resFilePath);
        File newFile = new File(newFilePath);
        return resFile.renameTo(newFile);
    }

    /**
     * Get size of file/directory.
     *
     * @param distFilePath the target file path.
     * @return size, return -1 if failed.
     */
    public static long getFileSize(String distFilePath)
    {
        File distFile = new File(distFilePath);
        if (distFile.isFile()) {
            return distFile.length();
        }
        else if (distFile.isDirectory()) {
            return org.apache.commons.io.FileUtils.sizeOfDirectory(distFile);
        }
        return -1L;
    }

    /**
     * Check whether file exist or not.
     *
     * @param filePath file path
     * @return true if existed, otherwise false
     */
    public static boolean isExist(String filePath)
    {
        return new File(filePath).exists();
    }

    /**
     * Get the list of file name which under the folder (without recursive).
     *
     * @param folder source folder.
     * @param suffix  the suffix of file (such as .mov.xml)
     * @return array of files name.
     */
    public static String[] listFileBySuffix(String folder, String suffix)
    {
        IOFileFilter fileFilter1 = new SuffixFileFilter(suffix);
        IOFileFilter fileFilter2 = new NotFileFilter(DirectoryFileFilter.INSTANCE);
        FilenameFilter filenameFilter = new AndFileFilter(fileFilter1, fileFilter2);
        return new File(folder).list(filenameFilter);
    }
}

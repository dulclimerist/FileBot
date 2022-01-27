package com.ing.recruitment.task.util;

import com.ing.recruitment.task.model.Matcher;
import com.ing.recruitment.task.model.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileTaskExecutor {
    private String workingDirectory;
    private Stream<Path> filesStream;
    private Path workingDirectoryPath;

    public FileTaskExecutor(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        try {
            workingDirectoryPath = Paths.get(workingDirectory);
            filesStream = Files.walk(workingDirectoryPath);
        } catch (IOException e) {
            System.out.println("Incorrect working directory: " + e.getMessage());
        }
    }

    public void execute(Task task) {
        System.out.println("Task: " + task.getName() + " started.");
        try {
            List<File> matchedFiles = this.matchFiles(task.getMatchers());
            switch (task.getAction().getActionName()) {
                case delete:
                    matchedFiles.forEach(this::deleteFile);
                    break;
                case copyTo:
                    matchedFiles.forEach(x -> copyFile(x, task.getAction().getActionParam()));
                    break;
                case moveTo:
                    matchedFiles.forEach(x -> moveFile(x, task.getAction().getActionParam()));
                    break;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Incorrect date format in task " + task.getName());
            System.out.println("Error message: " + e.getMessage());
        } catch (InvalidPathException e) {
            System.out.println("Incorrect destination directory: " + task.getAction().getActionParam());
        }
    }

    private void deleteFile(File file) {
        try {
            FileUtils.delete(file);
        } catch (IOException e) {
            System.out.println("Unable to delete file: " + file.getName() + "Error: " + e.getMessage());
        }
    }

    private void copyFile(File file, String copyDirectory) throws InvalidPathException {
        try {
            FileUtils.copyFile(file, createDestFile(file, copyDirectory));
        } catch (IOException e) {
            System.out.println("Unable to copy file: " + file.getName() + "Error: " + e.getMessage());

        }
    }

    private void moveFile(File file, String copyDirectory) throws InvalidPathException {
        try {
            FileUtils.moveFile(file, createDestFile(file, copyDirectory));
        } catch (IOException e) {
            System.out.println("Unable to move file: " + file.getName() + "Error: " + e.getMessage());
        }
    }

    private File createDestFile(File file, String copyDirectory) throws InvalidPathException {
        Path copyPath = Paths.get(copyDirectory);
        Path filePath = Paths.get(file.getPath());
        Path relativePath = workingDirectoryPath.relativize(filePath);
        return copyPath.resolve(relativePath).toFile();
    }

    private List<File> matchFiles(List<Matcher> matchers) throws DateTimeParseException {
        List<File> result = filesStream.filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for (Matcher matcher : matchers) {
            switch (matcher.getMatcherRule()) {
                case extensionIsNot:
                    result = result.stream()
                            .filter(x -> !FilenameUtils.isExtension(x.getName(), matcher.getParam()))
                            .collect(Collectors.toList());
                    break;
                case extensionIs:
                    result = result.stream()
                            .filter(x -> FilenameUtils.isExtension(x.getName(), matcher.getParam()))
                            .collect(Collectors.toList());
                    break;
                case nameContains:
                    result = result.stream()
                            .filter(x -> x.getName().contains(matcher.getParam()))
                            .collect(Collectors.toList());
                    break;
                case modifiedDateLessThen:
                    long dateInMilli = LocalDate.parse(matcher.getParam(), DateTimeFormatter.BASIC_ISO_DATE)
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli();
                    result = result.stream()
                            .filter(x -> x.lastModified() < dateInMilli)
                            .collect(Collectors.toList());
                    break;
                case modifiedDateGreaterThen:
                    long dateInMilli1 = LocalDate.parse(matcher.getParam(), DateTimeFormatter.BASIC_ISO_DATE)
                            .plusDays(1)
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli();
                    result = result.stream()
                            .filter(x -> x.lastModified() > dateInMilli1)
                            .collect(Collectors.toList());
                    break;
            }
        }
        return result;
    }
}

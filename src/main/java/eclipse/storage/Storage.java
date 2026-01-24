package eclipse.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import eclipse.exceptions.EclipseException;
import eclipse.task.Task;

public class Storage {
    private final StorageParser storageParser;
    private final String dirPathString;
    private final File taskStorageFile;

    public Storage(StorageParser parser, String dirPathString) {
        this.storageParser = parser;
        this.dirPathString = dirPathString;
        this.taskStorageFile = new File(dirPathString + "/tasks.txt");
    }

    public void storeTasks(List<Task> tasks) throws EclipseException {
        try {
            taskStorageFile.createNewFile();
        } catch (IOException e) {
            throw new EclipseException(
                    "Failed to create a new file with path" + taskStorageFile.getAbsolutePath(),
                    e);
        }

        try (FileWriter fileWriter = new FileWriter(this.taskStorageFile)) {
            fileWriter.write("");
        } catch (IOException e) {
            throw new EclipseException(
                    "Failed to clear the content in the file: " + taskStorageFile.getAbsolutePath(),
                    e);
        }

        try (FileWriter fileWriter = new FileWriter(this.taskStorageFile, true)) {
            for (Task task : tasks) {
                fileWriter.write(task.toStorageString() + "\n");
            }
        } catch (IOException e) {
            throw new EclipseException(
                    "Failed when trying to write to the file: " + taskStorageFile.getAbsolutePath(),
                    e);
        }
    }

    public List<Task> readTasks() throws EclipseException {
        List<Task> newTasks = new ArrayList<>();

        Scanner s;
        Path dirPath = Path.of(this.dirPathString);

        // If the directory does not exist
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new EclipseException("Failed to create directory with path: " + dirPath, e);
        }

        try {
            // create if the file does not exist
            // return false if it already exists
            taskStorageFile.createNewFile();
        } catch (IOException e) {
            throw new EclipseException("Failed to create a file to hold the tasks: " + taskStorageFile, e);
        }

        try {
            s = new Scanner(this.taskStorageFile);
        } catch (FileNotFoundException e) {
            throw new EclipseException("Failed to create directory with path: " + dirPath, e);
        }

        while (s.hasNext()) {
            String currLine = s.nextLine();

            if (currLine.trim().equals("")) {
                continue;
            }

            Task currTask = this.storageParser.parseStoredLine(currLine);
            newTasks.add(currTask);
        }
        return newTasks;
    }
}

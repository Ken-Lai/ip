package tasket.storage;

import static tasket.commons.Messages.MESSAGE_FILE_NOT_FOUND;
import static tasket.commons.Messages.MESSAGE_UNABLE_RETRIEVE_FILE;
import static tasket.commons.Messages.MESSAGE_UNABLE_SAVE_FILE;
import static tasket.commons.Messages.MESSAGE_UNABLE_SAVE_TASK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import tasket.data.TaskList;
import tasket.exception.TasketException;
import tasket.parser.Parser;
import tasket.task.Task;

/**
 * The class for storage.
 */
public class Storage implements StorageInterface {

    private final String filePath;
    private final File dataFile;

    /**
     * Constructs a storage.
     *
     * @param filePath The file path for saving the tasks.
     */
    public Storage(String filePath) {
        assert !filePath.isEmpty();
        this.filePath = filePath;
        dataFile = new File(filePath);
    }

    /**
     * {@inheritDoc}
     * If the file is corrupted, remove the corrupted tasks.
     *
     * @return A list of saved tasks.
     * @throws TasketException If the file cannot be found.
     */
    @Override
    public ArrayList<Task> load() throws TasketException {
        ArrayList<Task> taskList = new ArrayList<>();
        boolean isFileCorrupted = false;
        Scanner sc = null;

        validateSaveFile();

        try {
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                // Parse the text to tasks
                // If the text is corrupted, skip the text and mark the file as corrupted
                try {
                    String str = sc.nextLine();
                    taskList.add(Parser.parseSaveString(str));
                } catch (TasketException e) {
                    isFileCorrupted = true;
                }
            }

        } catch (FileNotFoundException e) {
            throw new TasketException(MESSAGE_FILE_NOT_FOUND);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        // If file is corrupted, rewrite save file to remove corrupted tasks.
        if (isFileCorrupted) {
            rewriteSaveFile(new TaskList(taskList));
        }

        return taskList;
    }

    /**
     * {@inheritDoc}
     * This is done when adding a new task.
     *
     * @param taskSave The task to be saved in save format.
     * @throws TasketException If IO error occurs.
     */
    @Override
    public void append(String taskSave) throws TasketException {
        assert !taskSave.isEmpty();

        FileWriter fw = null;

        try {
            try {
                fw = new FileWriter(dataFile, true);
                fw.write(taskSave + System.lineSeparator());
            } catch (IOException e) {
                throw new TasketException(MESSAGE_UNABLE_SAVE_TASK);
            } finally {
                if (fw != null) {
                    fw.close();
                }
            }
        } catch (IOException e) {
            throw new TasketException(MESSAGE_UNABLE_SAVE_FILE);
        }
    }

    /**
     * {@inheritDoc}
     * This is done whenever there is mark, unmark and delete operations.
     *
     * @param taskList The task list to get the tasks from.
     * @throws TasketException If IO error occurs.
     */
    @Override
    public void rewriteSaveFile(TaskList taskList) throws TasketException {
        assert taskList != null;

        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            try {
                fw = new FileWriter(dataFile, false);
                bw = new BufferedWriter(fw);

                for (int i = 0; i < taskList.size(); i++) {
                    bw.write(taskList.getTaskSaveString(i) + System.lineSeparator());
                }

            } catch (IOException e) {
                throw new TasketException(MESSAGE_UNABLE_SAVE_FILE);
            } finally {
                if (fw != null && bw != null) {
                    bw.close();
                    fw.close();
                }
            }

        } catch (IOException e) {
            throw new TasketException(MESSAGE_UNABLE_SAVE_FILE);
        }
    }

    /**
     * Creates a save file if it does not exist.
     * The parent folders are also created in this process.
     *
     * @throws TasketException If IO error occurs.
     */
    private void validateSaveFile() throws TasketException {
        try {
            Files.createDirectories(Path.of(filePath.substring(0, filePath.lastIndexOf("/"))));

            if (!dataFile.exists()) {
                Files.createFile(Path.of(filePath));
            }
        } catch (IOException e) {
            throw new TasketException(MESSAGE_UNABLE_RETRIEVE_FILE);
        }
    }
}

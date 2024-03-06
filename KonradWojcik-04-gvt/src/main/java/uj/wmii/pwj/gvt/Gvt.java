package uj.wmii.pwj.gvt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Gvt {
    private static int activeVersion;
    private final ExitHandler exitHandler;

    public Gvt(ExitHandler exitHandler) {
        this.exitHandler = exitHandler;
    }

    public static void main(String... args) {
        Gvt gvt = new Gvt(new ExitHandler());
        gvt.mainInternal(args);
    }

    private void init(String... args) {
        if (new File(".gvt").exists())
            exitHandler.exit(10, "Current directory is already initialized.");
        else try {
            activeVersion = 0;
            new File(String.format(".gvt/%s", activeVersion)).mkdirs();
            createMessageFile(new File(String.format(".gvt/%s", activeVersion)), "GVT initialized.");
            exitHandler.exit(0, "Current directory initialized successfully.");
        } catch (Exception e) {
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }

    private void add(String... args) {
        if (args.length == 1)
            exitHandler.exit(20, "Please specify file to add.");
        else if (!new File(args[1]).exists())
            exitHandler.exit(21, (String.format("File not found. File: %s", args[1])));
        else if (new File(String.format(".gvt/%s/%s", activeVersion, args[1])).exists())
            exitHandler.exit(0, String.format("File already added. File: %s", args[1]));
        else try {
                File currentPath = new File(String.format(".gvt/%s", activeVersion));
                File newPath = new File(String.format(".gvt/%s", activeVersion + 1));
                newPath.mkdir();
                copyFilesToDestination(currentPath, newPath);
                copyFileToDestination(new File(args[1]), newPath);
                String newFileMessage = messageFile(args);
                createMessageFile(newPath, newFileMessage);
                activeVersion++;
                exitHandler.exit(0, String.format("File added successfully. File: %s", args[1]));
            } catch (IOException e) {
                exitHandler.exit(22, String.format("File cannot be detached, see ERR for details. File: %s", args[1]));
            }

    }

    private void commit(String... args) {
        if (args.length == 1)
            exitHandler.exit(50, "Please specify file to commit.");
        else if (!new File(args[1]).exists())
            exitHandler.exit(51, "File not found. File: " + args[1]);
        else if (!new File(String.format(".gvt/%s/%s", activeVersion, args[1])).exists())
            exitHandler.exit(0, "File is not added to gvt. File: " + args[1]);
        else try {
                File newPath = new File(String.format(".gvt/%s", activeVersion + 1));
                newPath.mkdir();
                copyFileToDestination(new File(args[1]), newPath);
                String newFileMessage = messageFile(args);
                createMessageFile(newPath, newFileMessage);
                activeVersion++;
                exitHandler.exit(0, "File committed successfully. File: " + args[1]);
            } catch (IOException e) {
                exitHandler.exit(52, "File cannot be committed, see ERR for details. File: " + args[1]);
            }
    }

    private void detach(String... args) {
        if (args.length == 1)
            exitHandler.exit(30, "Please specify file to detach.");
        else if (!new File(String.format(".gvt/%s/%s", activeVersion, args[1])).exists())
            exitHandler.exit(0, String.format("File is not added to gvt. File: %s", args[1]));
        else try {
                File currentPath = new File(String.format(".gvt/%s", activeVersion));
                File newPath = new File(String.format(".gvt/%s", activeVersion + 1));
                newPath.mkdir();
                copyFilesToDestination(currentPath, newPath);
                new File(String.format(".gvt/%s/%s", activeVersion + 1, args[1])).delete();
                String newFileMessage = messageFile(args);
                activeVersion++;
                createMessageFile(newPath, newFileMessage);
                exitHandler.exit(0, String.format("File detached successfully. File: %s", args[1]));
            } catch (Exception e) {
                exitHandler.exit(31, String.format("File cannot be detached, see ERR for details. File: %s", args[1]));
            }
    }

    private void checkout(String... args) {
        if (args.length == 1 || !new File(String.format(".gvt/%s", args[1])).exists())
            exitHandler.exit(60, String.format("Invalid version number: %s", args[1]));
        else try {
            copyFilesToDestination(new File(String.format(".gvt/%s", args[1])), new File("."));
            exitHandler.exit(0, "Checkout successful for version: " + args[1]);
        } catch (IOException e) {
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }

    private void version(String... args) {
        if (args.length != 1 && !new File(String.format(".gvt/%s", args[1])).exists())
            exitHandler.exit(60, "Invalid version number: " + args[1]);
        else {
            int version;
            if (args.length == 1)
                version = activeVersion;
            else
                version = Integer.parseInt(args[1]);
            String output = "Version: " + version;
            String message = readFile(new File(String.format(".gvt/%s/%s", version, "message.txt")));
            if (message != "")
                output += message;
            exitHandler.exit(0, output);
        }
    }

    private void history(String... args) {
        int lastVersion = 0;
        if (args.length == 3 && (args[2].matches("[0-9]+")) && Integer.parseInt(args[2]) <= activeVersion)
            lastVersion = activeVersion - Integer.parseInt(args[2]) + 1;
        String output = "";
        for (int i = activeVersion; i >= lastVersion; i--) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(".gvt/" + i + "/message.txt"));
                output += i + ": ";
                String temp;
                if ((temp = br.readLine()) != null)
                    output += temp + "\n";
            } catch (IOException e) {
                exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
            }
        }
        exitHandler.exit(0, output);
    }

    private String messageFile(String... args) {
        String message = "";
        if (args.length == 4)
            message = args[3];
        else if (args[0] == "add")
            message = String.format("File added successfully. File: %s", args[1]);
        else if (args[0] == "detach")
            message = String.format("File detached successfully. File: %s", args[1]);
        else if (args[0] == "commit")
            message = String.format("File committed successfully. File: %s", args[1]);
        return message;
    }

    private void copyFilesToDestination(File source, File destination) throws IOException {
        if (source.list() != null)
            for (String file : source.list())
                Files.copy(Path.of(source.getPath() + "/" + file), Path.of(destination.getPath() + "/" + file), StandardCopyOption.REPLACE_EXISTING);
    }

    private void copyFileToDestination(File file, File destination) throws IOException {
        Files.copy(file.toPath(), Path.of(destination.toPath() + "/" + file), StandardCopyOption.REPLACE_EXISTING);
    }

    private void createMessageFile(File directory, String message) {
        try {
            File file = new File(directory.getPath() + "/message.txt");
            file.createNewFile();
            Files.writeString(file.toPath(), message);
        } catch (Exception e) {
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }

    private String readFile(File file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp;
            while ((temp = br.readLine()) != null)
                result += "\n" + temp;
        } catch (IOException e) {
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
        return result;
    }

    void mainInternal(String... args) {

        if (args.length == 0)
            exitHandler.exit(1, "Please specify command.");
        else {
            String commend = args[0];
            if (commend == "init")
                init(args);
            else if (!new File(".gvt").exists())
                exitHandler.exit(-2, "Current directory is not initialized. Please use init command to initialize.");
            else if (commend == "add")
                add(args);
            else if (commend == "commit")
                commit(args);
            else if (commend == "detach")
                detach(args);
            else if (commend == "checkout")
                checkout(args);
            else if (commend == "version")
                version(args);
            else if (commend == "history")
                history(args);
            else
                exitHandler.exit(1, "Unknown command.");
        }
    }
}

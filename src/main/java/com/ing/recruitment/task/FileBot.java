package com.ing.recruitment.task;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ing.recruitment.task.model.Scripts;
import com.ing.recruitment.task.util.CommandLineArgsParser;
import com.ing.recruitment.task.util.FileTaskExecutor;

import java.io.FileReader;
import java.io.IOException;


public class FileBot {
    public static void main(String[] args) {

        CommandLineArgsParser argsParser = new CommandLineArgsParser();
        Gson gson = new Gson();
        if (argsParser.parse(args)) {
            FileTaskExecutor executor = new FileTaskExecutor(argsParser.getWorkingDirectory());
            try (FileReader scriptsFile = new FileReader(argsParser.getScriptsFile())) {
                Scripts scripts = gson.fromJson(scriptsFile, Scripts.class);
                if (scripts.getScripts() == null) {
                    System.out.println("No executable task in your script file. Please check file correctness");
                }
                scripts.getScripts().forEach(executor::execute);
            } catch (IOException e) {
                System.out.println("Scripts file cannot be open: " + e.getMessage());
            } catch (JsonSyntaxException e) {
                System.out.println("Scripts file is incorrect: " + e.getMessage());
            }
        }
    }
}
